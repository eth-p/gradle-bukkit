package dev.ethp.bukkit.gradle.function;

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

	public VaultApi(Project project, Repository repo) {
		super(project, repo);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public Dependency createDependency(DependencyHandler handler) {
		return handler.create("com.github.MilkBowl:VaultAPI:" + this.version);
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