package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.CommonRepository;
import dev.ethp.bukkit.gradle.dependency.DependencySpec;
import dev.ethp.bukkit.gradle.dependency.RemoteDependency;
import org.gradle.api.Project;

import static dev.ethp.bukkit.gradle.dependency.DependencySpec.*;

public class LibBkCommon extends AbstractDependencyFunction {

	static public String FUNCTION = "libBkCommon";

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
	public DependencySpec[] getDependencies() {
		return new DependencySpec[]{
				compileOnly(new RemoteDependency(
						"https://ci.mg-dev.eu/job/BKCommonLib/lastSuccessfulBuild/artifact/target/BKCommonLib-1.16.2-v2-SNAPSHOT.jar"
				)),
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
		return "084534104dc924f84132a661218039796b8c6960";
	}

}
