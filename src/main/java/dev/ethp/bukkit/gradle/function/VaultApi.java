package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.CommonRepository;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;

public class VaultApi extends AbstractDependencyFunction {

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
	public Dependency getDependency(DependencyHandler handler) {
		return handler.create("com.github.MilkBowl:VaultAPI:" + this.version);
	}

	@Override
	public CommonRepository[] getRepositories() {
		return new CommonRepository[]{
				CommonRepository.JITPACK
		};
	}

	public void doCall(String version) {
		this.version = version;
		this.inject();
	}

	public void doCall() {
		this.version = "1.7";
		this.inject();
	}

}