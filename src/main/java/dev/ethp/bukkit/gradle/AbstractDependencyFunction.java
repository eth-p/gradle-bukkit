package dev.ethp.bukkit.gradle;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import groovy.lang.Closure;

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar;
import dev.ethp.bukkit.gradle.dependency.DependencySpec;
import dev.ethp.bukkit.gradle.extension.BukkitExtension;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;
import org.gradle.api.UnknownTaskException;
import org.gradle.api.artifacts.DependencyResolutionListener;
import org.gradle.api.artifacts.ResolvableDependencies;

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
	protected final BukkitExtension getExtension() {
		return this.getProject().getExtensions().getByType(BukkitExtension.class);
	}

	/**
	 * Gets the Project object.
	 *
	 * @return The project.
	 */
	protected final Project getProject() {
		return (Project) this.getDelegate();
	}

	/**
	 * Marks the extension as configured.
	 * This tells bukkit-gradle to inject it.
	 */
	protected final void configured() {
		this.injectWanted = true;
	}

	/**
	 * Gets the name of this dependency.
	 * This will default to the function name, if the name could not be found.
	 *
	 * @return The dependency name.
	 */
	protected final String getDependencyName() {
		try {
			try {
				Field nameField = this.getClass().getField("NAME");
				return (String) nameField.get(null);
			} catch (NoSuchFieldException ignored) {
			}

			Field functionField = this.getClass().getField("FUNCTION");
			return (String) functionField.get(null);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
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

		// Inject the repositories.
		Arrays.asList(this.getRepositories()).forEach(repo -> repo.inject(project));

		// Inject the dependencies.
		project.getGradle().addListener(new AbstractDependencyInjector(
				this.getProject(),
				Arrays.asList(this.getDependencies())
		));

		// Relocate the dependencies.
		if (this.getRelocate()) {
			try {
				this.getProject().getTasks().getByName("shadowJar").configure(new Closure(this) {
					public void doCall(ShadowJar task) {
						String destination = AbstractDependencyFunction.this.getRelocatePackage();
						for (String original : AbstractDependencyFunction.this.getRelocatedPackages()) {
							task.relocate(original, destination + "." + original);
						}
					}
				});
				
				this.getProject().getTasks().getByName("build").dependsOn("shadowJar");
			} catch (NoClassDefFoundError | UnknownTaskException err) {
				System.err.println("Dependency relocation for " + this.getDependencyName() + " is not possible.");
				System.err.println("Could not find 'com.github.johnrengelman.shadow plugin'.");
				System.err.println();
				System.err.println("You can include the plugin like this:");
				System.err.println();
				System.err.println("    plugins {");
				System.err.println("        id 'com.github.johnrengelman.shadow' version '6.0.0'");
				System.err.println("    }");
				System.err.println();
				throw new RuntimeException("Unable to find com.github.johnrengelman.shadow plugin.");
			} catch (Exception ex) {
				System.err.println("Dependency relocation is not possible.");
				System.err.println("An incompatible version of 'com.github.johnrengelman.shadow plugin was used.");
				System.err.println("Tested versions are '5.2.0' and '6.0.0'.");
				throw new RuntimeException("Unable to configure com.github.johnrengelman.shadow plugin.");
			}
		}

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
	 *
	 * @return An array of dependencies to add.
	 */
	protected abstract DependencySpec[] getDependencies();

	/**
	 * Gets the repositories that this dependency requires.
	 *
	 * @return The repositories.
	 */
	protected abstract CommonRepository[] getRepositories();

	/**
	 * Gets the default version of this dependency.
	 *
	 * @return The default version.
	 */
	protected abstract String getDefaultVersion();

	/**
	 * Gets whether or not the dependency can be relocated by the shadow plugin.
	 *
	 * @return Whether the dependency is relocatable.
	 */
	protected boolean isRelocatable() {
		return false;
	}

	/**
	 * Gets whether or not the dependency is relocated by default.
	 *
	 * @return Whether the dependency is relocated by default.
	 */
	protected boolean isRelocatedByDefault() {
		return false;
	}

	/**
	 * Gets a list of the packages that should be relocated.
	 *
	 * @return A list of packages to relocate.
	 */
	protected String[] getRelocatedPackages() {
		throw new UnsupportedOperationException("Dependency does not override getRelocatePackages()");
	}

	/**
	 * Called when the dependency should be injected.
	 */
	protected void onInject() {
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
		if (version == null) throw new InvalidUserDataException("Bukkit: " + this.getDependencyName() + " version cannot be null.");
		this.configured();
		this.version = version;
	}

	public final void version(String version) {
		this.setVersion(version);
	}

	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: relocate
	// Option.
	//
	// Use the shadow plugin (must be added in plugins section) to rename a dependency and package it inside the plugin. 
	//
	// dependencies {
	//     ...
	//     myDependency {
	//         relocate "com.example.bukkit._internal.com.example.lib"
	//         relocate = false // Disable relocation.
	//     }
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String relocatePackage = null;
	private Optional<Boolean> relocate = Optional.empty();

	public final boolean getRelocate() {
		return this.relocate.orElseGet(this::isRelocatedByDefault);
	}

	public final String getRelocatePackage() {
		if (this.relocatePackage != null) return this.relocatePackage;

		// Calculate the plugin package from the main class.
		String main = this.getExtension().getMainClass();
		LinkedList<String> mainPath = new LinkedList<String>(Arrays.asList(main.split("\\.")));
		mainPath.removeLast();
		String mainPackage = String.join(".", mainPath);

		// Return the package + "_internal".
		return mainPackage + "._internal";
	}

	public void setRelocate(boolean enabled) {
		if (enabled && !this.isRelocatable()) throw new RuntimeException("Bukkit: " + this.getDependencyName() + " cannot be relocated.");
		this.configured();
		this.relocate = Optional.of(enabled);
	}

	public void setRelocate(String to) {
		if (to == null) {
			this.setRelocate(false);
			return;
		}

		this.setRelocate(true);
		this.relocatePackage = to;
	}

	public final void relocate(String to) {
		this.setRelocate(true);
		this.setRelocate(to);
	}

}

/**
 * The class that listens for and injects dependencies into a Gradle project.
 */
class AbstractDependencyInjector implements DependencyResolutionListener {

	private final Project project;
	private final List<DependencySpec> dependencies;

	public AbstractDependencyInjector(Project project, List<DependencySpec> dependencies) {
		this.project = project;
		this.dependencies = dependencies;
	}

	@Override
	public void beforeResolve(ResolvableDependencies resolvableDependencies) {
		for (DependencySpec dependencySpec : this.dependencies) {
			dependencySpec.inject(project);
		}

		this.project.getGradle().removeListener(this);
	}

	@Override
	public void afterResolve(ResolvableDependencies resolvableDependencies) {
	}

}
