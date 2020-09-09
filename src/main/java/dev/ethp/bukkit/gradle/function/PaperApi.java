package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.CommonRepository;
import dev.ethp.bukkit.gradle.dependency.DependencySpec;
import org.gradle.api.Project;

import static dev.ethp.bukkit.gradle.dependency.DependencySpec.*;

public class PaperApi extends AbstractDependencyFunction {

	static public String FUNCTION = "paperApi";

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
	public DependencySpec[] getDependencies() {
		return new DependencySpec[]{
				compileOnly("com.destroystokyo.paper:paper-api:" + this.getVersion()),
		};
	}

	@Override
	public CommonRepository[] getRepositories() {
		return new CommonRepository[]{
				CommonRepository.PAPER
		};
	}

	@Override
	public String getDefaultVersion() {
		return this.getExtension().getApi().getLibraryVersion();
	}

}
