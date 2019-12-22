package dev.ethp.bukkit.gradle.function;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;

public class BukkitApi extends AbstractDependencyFunction {

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public BukkitApi(Project project, Repository repo) {
		super(project, repo);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public Dependency createDependency(DependencyHandler handler) {
		return handler.create("org.bukkit:bukkit:" + this.getExtension().getApi().getLibraryVersion());
	}

	public void doCall() {
		this.inject();
	}

}