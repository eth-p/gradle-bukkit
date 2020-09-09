package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.CommonRepository;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;

public class LibBkCommon extends AbstractDependencyFunction {

	// -------------------------------------------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------------------------------------------

	private String version;

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public LibBkCommon(Project project) {
		super(project);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public Dependency getDependency(DependencyHandler handler) {
		return handler.create("com.github.bergerhealer:BKCommonLib:" + this.version);
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
		this.version = "master-SNAPSHOT";
		this.inject();
	}

}
