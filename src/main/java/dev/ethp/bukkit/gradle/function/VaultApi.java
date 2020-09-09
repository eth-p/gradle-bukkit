package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.CommonRepository;
import dev.ethp.bukkit.gradle.dependency.DependencySpec;
import org.gradle.api.Project;

import static dev.ethp.bukkit.gradle.dependency.DependencySpec.*;

public class VaultApi extends AbstractDependencyFunction {

	static public String FUNCTION = "vaultApi";
	static public String NAME = "Vault API";

	// -------------------------------------------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------------------------------------------

	private String version;

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public VaultApi(Project project) {
		super(project);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public DependencySpec[] getDependencies() {
		return new DependencySpec[]{
				compileOnly("com.github.MilkBowl:VaultAPI:" + this.getVersion()),
		};
	}

	@Override
	public CommonRepository[] getRepositories() {
		return new CommonRepository[]{
				CommonRepository.JITPACK
		};
	}

	@Override
	protected String[] getPluginDependencies() {
		return new String[]{
				"Vault"
		};
	}

	@Override
	protected String getDefaultVersion() {
		return "1.7";
	}

}
