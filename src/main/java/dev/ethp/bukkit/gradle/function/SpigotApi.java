package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.CommonRepository;
import dev.ethp.bukkit.gradle.dependency.DependencySpec;
import org.gradle.api.Project;

import static dev.ethp.bukkit.gradle.dependency.DependencySpec.*;

public class SpigotApi extends AbstractDependencyFunction {

	static public String FUNCTION = "spigotApi";

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public SpigotApi(Project project) {
		super(project);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public DependencySpec[] getDependencies() {
		return new DependencySpec[]{
				compileOnly("org.spigotmc:spigot-api:" + this.getVersion()),
		};
	}

	@Override
	public CommonRepository[] getRepositories() {
		return new CommonRepository[]{
				CommonRepository.SPIGOT,
				CommonRepository.SONATYPE
		};
	}

	@Override
	public String getDefaultVersion() {
		return this.getExtension().getApi().getLibraryVersion();
	}

}
