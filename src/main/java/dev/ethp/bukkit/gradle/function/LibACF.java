package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.CommonRepository;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;

import static dev.ethp.bukkit.gradle.AbstractDependencyFunction.Dependency.*;

public class LibACF extends AbstractDependencyFunction {

	static public String FUNCTION = "libACF";

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public LibACF(Project project) {
		super(project);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public Dependency[] getDependency() {
		return new Dependency[]{
				compileOnly("co.aikar:acf-" + this.getPlatform() + ":" + this.getVersion()),
		};
	}

	@Override
	public CommonRepository[] getRepositories() {
		return new CommonRepository[]{
				CommonRepository.AIKAR
		};
	}

	@Override
	protected String getDefaultVersion() {
		return "0.5.0-SNAPSHOT";
	}

	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: platform
	// Option.
	//
	// The ACF platform.
	// Available options: 'bukkit', 'paper'
	//
	// dependencies {
	//     ...
	//     libACF({
	//         platform = "paper"
	//     })
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String platform = "bukkit";

	public String getPlatform() {
		return this.platform;
	}

	public void setPlatform(String platform) {
		if (platform == null) throw new InvalidUserDataException("ACF platform cannot be null.");
		this.platform = platform;
		this.configured();
	}

	public void platform(String platform) {
		this.setPlatform(platform);
	}


}