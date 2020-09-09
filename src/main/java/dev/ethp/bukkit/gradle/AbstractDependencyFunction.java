package dev.ethp.bukkit.gradle;

import java.util.Arrays;
import java.util.List;

import groovy.lang.Closure;

import dev.ethp.bukkit.gradle.extension.BukkitExtension;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencyResolutionListener;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.ResolvableDependencies;
import org.gradle.api.artifacts.dsl.DependencyHandler;

public abstract class AbstractDependencyFunction extends Closure<Project> {

	private boolean injectComplete = false;
	private boolean injectWanted = false;

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public AbstractDependencyFunction(Project project) {
		super(project);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * Gets the BukkitExtension object for the plugin.
	 *
	 * @return The extension.
	 */
	protected BukkitExtension getExtension() {
		return this.getProject().getExtensions().getByType(BukkitExtension.class);
	}

	/**
	 * Gets the Project object.
	 *
	 * @return The project.
	 */
	protected Project getProject() {
		return (Project) this.getDelegate();
	}

	/**
	 * Marks the extension as configured.
	 * This tells bukkit-gradle to inject it.
	 */
	protected void configured() {
		this.injectWanted = true;
	}
	
	/**
	 * Called when the dependency should be injected.
	 */
	protected void onInject() {
	}

	// -------------------------------------------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * Injects the dependency into the project's compile dependencies.
	 */
	void inject() {
		if (this.injectComplete) {
			throw new RuntimeException("This plugin dependency was already injected.");
		}

		Project project = this.getProject();
		Arrays.asList(this.getRepositories()).forEach(repo -> repo.inject(project));
		project.getGradle().addListener(new AbstractDependencyInjector(
				this.getProject(),
				Arrays.asList(this.getDependency())
		));

		this.onInject();
		this.injectComplete = true;
	}

	/**
	 * Checks if the dependency was already injected.
	 *
	 * @return Whether the dependency was injected.
	 */
	boolean isInjected() {
		return this.injectComplete;
	}

	/**
	 * Checks if the dependency should be injected.
	 * This will only work with closures if a field has been configured!
	 *
	 * @return Whether the dependency was injected.
	 */
	boolean isConfigured() {
		return this.injectWanted;
	}

	// -------------------------------------------------------------------------------------------------------------
	// Abstract
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * Creates the dependency objects to add.
	 * @return An array of dependencies to add.
	 */
	protected abstract Dependency[] getDependency();
	
	/**
	 * Gets the repositories that this dependency requires.
	 *
	 * @return The repositories.
	 */
	protected abstract CommonRepository[] getRepositories();

	/**
	 * Gets the default version of this dependency.
	 * @return The default version.
	 */
	protected abstract String getDefaultVersion();

	// -------------------------------------------------------------------------------------------------------------
	// Classes
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * A class that contains information about a dependency that needs to be injected.
	 */
	public static class Dependency {

		public final String configuration;
		public final String artifact;

		private Dependency(String configuration, String artifact) {
			this.configuration = configuration;
			this.artifact = artifact;
		}

		/**
		 * Creates a dependency that is only used for compiler symbols.
		 * This dependency will not be included at runtime.
		 *
		 * @param artifact The dependency notation string.
		 * @return The dependency object.
		 */
		static public Dependency compileOnly(String artifact) {
			return new Dependency("compileOnly", artifact);
		}

	}

	// -------------------------------------------------------------------------------------------------------------
	// Default
	// -------------------------------------------------------------------------------------------------------------

	public final void doCall() {
		this.injectWanted = true;
	}

	public final void doCall(String version) {
		this.injectWanted = true;
		this.setVersion(version);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: version
	// Option.
	//
	// The dependency version.
	//
	// dependencies {
	//     ...
	//     myDependency {
	//         version = "0.5.0-SNAPSHOT"
	//     }
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String version = null;

	public final String getVersion() {
		return this.version == null ? this.getDefaultVersion() : this.version;
	}

	public void setVersion(String version) {
		if (version == null) throw new InvalidUserDataException("Dependency version cannot be null.");
		this.configured();
		this.version = version;
	}

	public final void version(String version) {
		this.setVersion(version);
	}

}

/**
 * The class that listens for and injects dependencies into a Gradle project.
 */
class AbstractDependencyInjector implements DependencyResolutionListener {

	private final Project project;
	private final List<AbstractDependencyFunction.Dependency> dependencies;

	public AbstractDependencyInjector(Project project, List<AbstractDependencyFunction.Dependency> dependencies) {
		this.project = project;
		this.dependencies = dependencies;
	}

	@Override
	public void beforeResolve(ResolvableDependencies resolvableDependencies) {
		DependencyHandler handler = this.project.getDependencies();
		
		for (AbstractDependencyFunction.Dependency dependencySpec : this.dependencies) {
			DependencySet dependencySet = this.project.getConfigurations().getByName(dependencySpec.configuration).getDependencies();
			Dependency dependency = handler.create(dependencySpec.artifact);
			dependencySet.add(dependency);
		}
		
		this.project.getGradle().removeListener(this);
	}

	@Override
	public void afterResolve(ResolvableDependencies resolvableDependencies) {
	}

}
