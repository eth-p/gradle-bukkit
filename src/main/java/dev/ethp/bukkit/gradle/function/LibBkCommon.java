package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.CommonRepository;
import dev.ethp.bukkit.gradle.dependency.DependencySpec;
import dev.ethp.bukkit.gradle.dependency.RemoteDependency;
import org.gradle.api.Project;

import static dev.ethp.bukkit.gradle.dependency.DependencySpec.*;

public class LibBkCommon extends AbstractDependencyFunction {

	static public String FUNCTION = "libBkCommon";
	static public String NAME = "BKCommonLib";

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
						"https://ci.mg-dev.eu/job/BKCommonLib/" + this.getVersion() + "/artifact/target/BKCommonLib-1.16.2-v2-SNAPSHOT.jar"
				)),
		};
	}

	@Override
	public CommonRepository[] getRepositories() {
		return new CommonRepository[0];
	}

	@Override
	protected String[] getPluginDependencies() {
		return new String[]{
				"BKCommonLib"
		};
	}

	@Override
	protected String getDefaultVersion() {
		return "899";
	}

	@Override
	protected boolean canBeOptional() {
		return true;
	}
	
}
