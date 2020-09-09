package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.CommonRepository;
import org.gradle.api.Project;

import static dev.ethp.bukkit.gradle.AbstractDependencyFunction.Dependency.*;

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
	public Dependency[] getDependency() {
		return new Dependency[]{
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
