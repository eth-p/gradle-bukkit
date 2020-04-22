package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.CommonRepository;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;

public class PaperApi extends AbstractDependencyFunction {

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public PaperApi(Project project) {
		super(project);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public Dependency getDependency(DependencyHandler handler) {
		return handler.create("com.destroystokyo.paper:paper-api:" + this.getExtension().getApi().getLibraryVersion());
	}

	@Override
	public CommonRepository[] getRepositories() {
		return new CommonRepository[]{
				CommonRepository.PAPER
		};
	}

	public void doCall() {
		this.inject();
	}

}
