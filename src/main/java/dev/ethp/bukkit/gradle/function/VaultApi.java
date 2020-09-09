package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.CommonRepository;
import org.gradle.api.Project;

import static dev.ethp.bukkit.gradle.AbstractDependencyFunction.Dependency.*;

public class VaultApi extends AbstractDependencyFunction {

	static public String FUNCTION = "vaultApi";
	
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
	public Dependency[] getDependency() {
		return new Dependency[]{
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
	protected String getDefaultVersion() {
		return "1.7";
	}

}
