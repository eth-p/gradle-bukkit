package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.CommonRepository;
import dev.ethp.bukkit.gradle.dependency.DependencySpec;
import org.gradle.api.Project;

import static dev.ethp.bukkit.gradle.dependency.DependencySpec.*;

public class BukkitApi extends AbstractDependencyFunction {

	static public String FUNCTION = "bukkitApi";
	static public String NAME = "The Bukkit API";

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public BukkitApi(Project project) {
		super(project);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public DependencySpec[] getDependencies() {
		return new DependencySpec[]{
				compileOnly("org.bukkit:bukkit:" + this.getVersion()),
		};
	}

	@Override
	public CommonRepository[] getRepositories() {
		return new CommonRepository[]{
				CommonRepository.SPIGOT
		};
	}

	@Override
	protected String[] getPluginDependencies() {
		return new String[0];
	}

	@Override
	public String getDefaultVersion() {
		return this.getExtension().getApi().getLibraryVersion();
	}

}
