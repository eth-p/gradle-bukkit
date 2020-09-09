package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.CommonRepository;
import org.gradle.api.Project;

import static dev.ethp.bukkit.gradle.AbstractDependencyFunction.Dependency.*;

public class PlaceholderApi extends AbstractDependencyFunction {
	
	static public String FUNCTION = "placeholderApi";

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public PlaceholderApi(Project project) {
		super(project);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public Dependency[] getDependency() {
		return new Dependency[]{
				compileOnly("me.clip:placeholderapi:" + this.getVersion()),
		};
	}

	@Override
	public CommonRepository[] getRepositories() {
		return new CommonRepository[]{
				CommonRepository.PLACEHOLDERAPI
		};
	}
	
	@Override
	protected String getDefaultVersion() {
		return "2.10.9";
	}
	
}
