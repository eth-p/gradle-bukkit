package dev.ethp.bukkit.gradle.function;

import java.util.Arrays;

import groovy.lang.Closure;

import dev.ethp.bukkit.gradle.CommonRepository;
import dev.ethp.bukkit.gradle.extension.BukkitExtension;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencyResolutionListener;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.ResolvableDependencies;
import org.gradle.api.artifacts.dsl.DependencyHandler;

public abstract class AbstractDependencyFunction extends Closure<Project> implements DependencyResolutionListener {

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
	 * Injects the dependency into the project's compile dependencies.
	 */
	protected void inject() {
		Project project = this.getProject();
		Arrays.asList(this.getRepositories()).forEach(repo -> repo.inject(project));
		project.getGradle().addListener(this);
	}

	/**
	 * Gets the BukkitExtension object for the plugin.
	 * @return The extension.
	 */
	protected BukkitExtension getExtension() {
		return this.getProject().getExtensions().getByType(BukkitExtension.class);
	}

	/**
	 * Gets the Project object.
	 * @return The project.
	 */
	protected Project getProject() {
		return (Project) this.getDelegate();
	}

	// -------------------------------------------------------------------------------------------------------------
	// Abstract
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * Creates the dependency object to add.
	 *
	 * @param handler The dependency handler.
	 * @return The dependency object.
	 */
	public abstract Dependency getDependency(DependencyHandler handler);

	/**
	 * Gets the repositories that this dependency requires.
	 *
	 * @return The repositories.
	 */
	public abstract CommonRepository[] getRepositories();

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public void beforeResolve(ResolvableDependencies resolvableDependencies) {
		Project project = this.getProject();

		DependencySet dependencies = project.getConfigurations().getByName("compileOnly").getDependencies();
		Dependency dependency = getDependency(project.getDependencies());
		dependencies.add(dependency);

		project.getGradle().removeListener(this);
	}

	@Override
	public void afterResolve(ResolvableDependencies resolvableDependencies) {
	}

}