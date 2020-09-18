package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.CommonRepository;
import dev.ethp.bukkit.gradle.dependency.DependencySpec;
import org.gradle.api.Project;
import static dev.ethp.bukkit.gradle.dependency.DependencySpec.implementation;

public class RoseGarden extends AbstractDependencyFunction {

	static public String FUNCTION = "roseGarden";
	static public String NAME = "RoseGarden";
	static public String REPO = "https://github.com/Rosewood-Development/RoseGarden";

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public RoseGarden(Project project) {
		super(project);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public DependencySpec[] getDependencies() {
		return new DependencySpec[]{
				implementation("dev.rosewood:rosegarden:" + this.getVersion()),
		};
	}

	@Override
	public CommonRepository[] getRepositories() {
		return new CommonRepository[]{
				CommonRepository.ROSEWOOD,
				CommonRepository.SPIGOT,
		};
	}

	@Override
	protected String[] getPluginDependencies() {
		return new String[0];
	}

	@Override
	public String getDefaultVersion() {
		return "1.0.4-SNAPSHOT";
	}

	@Override
	protected boolean isRelocatable() {
		return true;
	}

	@Override
	protected boolean isRelocatedByDefault() {
		return true;
	}

	@Override
	protected boolean isMinimizable() {
		return true;
	}

	@Override
	protected boolean isMinimizedByDefault() {
		return true;
	}

	@Override
	protected String[] getRelocatedPackages() {
		return new String[]{
				"dev.rosewood.rosegarden"
		};
	}
}
