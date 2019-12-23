package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.CommonRepository;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;

public class BukkitApi extends AbstractDependencyFunction {

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public BukkitApi(Project project) {
		super(project);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public Dependency getDependency(DependencyHandler handler) {
		return handler.create("org.bukkit:bukkit:" + this.getExtension().getApi().getLibraryVersion());
	}

	@Override
	public CommonRepository[] getRepositories() {
		return new CommonRepository[]{
				CommonRepository.SPIGOT
		};
	}

	public void doCall() {
		this.inject();
	}

}