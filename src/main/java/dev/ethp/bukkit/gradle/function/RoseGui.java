package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.CommonRepository;
import dev.ethp.bukkit.gradle.dependency.DependencySpec;
import org.gradle.api.Project;
import static dev.ethp.bukkit.gradle.dependency.DependencySpec.implementation;

public class RoseGui extends AbstractDependencyFunction {

	static public String FUNCTION = "roseGui";
	static public String NAME = "GuiFramework";
	static public String REPO = "https://github.com/Rosewood-Development/GuiFramework";

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public RoseGui(Project project) {
		super(project);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public DependencySpec[] getDependencies() {
		return new DependencySpec[]{
				implementation("dev.rosewood:guiframework:" + this.getVersion()),
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
		return "1.1.1";
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
	protected String[] getRelocatedPackages() {
		return new String[]{
				"dev.rosewood.guiframework.gui"
		};
	}
}
