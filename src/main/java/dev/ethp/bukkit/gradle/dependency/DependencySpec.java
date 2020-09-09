package dev.ethp.bukkit.gradle.dependency;

import org.gradle.api.Project;
import org.gradle.api.artifacts.DependencySet;

/**
 * Information about a dependency that needs to be injected into a Gradle project.
 */
public class DependencySpec {

	public final String configuration;
	private final AbstractDependency generator;

	private DependencySpec(String configuration, AbstractDependency dependency) {
		this.configuration = configuration;
		this.generator = dependency;
	}

	/**
	 * Injects the dependency into a project.
	 * @param project The project.
	 */
	public void inject(Project project) {
		DependencySet dependencySet = project.getConfigurations().getByName(this.configuration).getDependencies();
		dependencySet.add(this.generator.create(project));
	}

	/**
	 * Creates a dependency that is only used for compiler symbols.
	 * This dependency will not be included at runtime.
	 *
	 * @param notation The dependency notation string.
	 * @return The dependency object.
	 */
	static public DependencySpec compileOnly(String notation) {
		return new DependencySpec("compileOnly", new ArtifactDependency(notation));
	}
	
	/**
	 * Creates a dependency that is only used for compiler symbols.
	 * This dependency will not be included at runtime.
	 *
	 * @param dependency The dependency builder.
	 * @return The dependency object.
	 */
	static public DependencySpec compileOnly(AbstractDependency dependency) {
		return new DependencySpec("compileOnly", dependency);
	}

	/**
	 * Creates a dependency that is included at runtime, but not re-exported to dependents.
	 *
	 * @param notation The dependency notation string.
	 * @return The dependency object.
	 */
	static public DependencySpec implementation(String notation) {
		return new DependencySpec("implementation", new ArtifactDependency(notation));
	}

	/**
	 * Creates a dependency that is included at runtime, but not re-exported to dependents.
	 *
	 * @param dependency The dependency builder.
	 * @return The dependency object.
	 */
	static public DependencySpec implementation(AbstractDependency dependency) {
		return new DependencySpec("implementation", dependency);
	}
	
}
