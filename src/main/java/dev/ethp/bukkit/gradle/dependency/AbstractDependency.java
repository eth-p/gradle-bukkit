package dev.ethp.bukkit.gradle.dependency;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;

/**
 * An abstract dependency builder.
 */
public abstract class AbstractDependency {

	/**
	 * Creates the Gradle dependency object for this dependency.
	 *
	 * @param project The project that this dependency applies to.
	 * @return The dependency object.
	 */
	public abstract Dependency create(Project project);

}
