package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.CommonRepository;
import dev.ethp.bukkit.gradle.dependency.DependencySpec;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;
import static dev.ethp.bukkit.gradle.dependency.DependencySpec.implementation;

public class LibCommandApi extends AbstractDependencyFunction {

	static public String FUNCTION = "libCommandApi";
	static public String NAME = "CommandApi";
	static public String REPO = "https://github.com/JorelAli/CommandAPI";

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public LibCommandApi(Project project) {
		super(project);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public DependencySpec[] getDependencies() {
		String dependency = "dev.jorel:commandapi-core:" + this.getVersion();
		return new DependencySpec[]{implementation(dependency)};
	}

	@Override
	public CommonRepository[] getRepositories() {
		return new CommonRepository[]{
				CommonRepository.JOREL_ALI,
				CommonRepository.CODEMC
		};
	}

	@Override
	protected String[] getPluginDependencies() {
		return new String[0];
	}

	@Override
	protected String getDefaultVersion() {
		return "4.2";
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
				"dev.jorel.commandapi",
		};
	}

}
