package dev.ethp.bukkit.gradle.dependency;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.dsl.DependencyHandler;

/**
 * A dependency that is located in one of the project repositories.
 */
public class ArtifactDependency extends AbstractDependency {

	public final String artifact;

	/**
	 * Creates a new artifact dependency.
	 *
	 * @param artifact The Maven artifact notation of the dependency.
	 */
	public ArtifactDependency(String artifact) {
		this.artifact = artifact;
	}

	@Override
	public Dependency create(Project project) {
		return project.getDependencies().create(this.artifact);
	}
	
}
