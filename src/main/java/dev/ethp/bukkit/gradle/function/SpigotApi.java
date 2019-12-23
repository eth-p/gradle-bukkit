package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.CommonRepository;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;

public class SpigotApi extends AbstractDependencyFunction {

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public SpigotApi(Project project) {
		super(project);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public Dependency getDependency(DependencyHandler handler) {
		return handler.create("org.spigotmc:spigot-api:" + this.getExtension().getApi().getLibraryVersion());
	}

	@Override
	public CommonRepository[] getRepositories() {
		return new CommonRepository[]{
				CommonRepository.SPIGOT,
				CommonRepository.SONATYPE
		};
	}

	public void doCall() {
		this.inject();
	}

}