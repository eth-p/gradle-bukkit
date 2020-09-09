package dev.ethp.bukkit.gradle.function;

import dev.ethp.bukkit.gradle.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.CommonRepository;
import dev.ethp.bukkit.gradle.dependency.DependencySpec;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;

import static dev.ethp.bukkit.gradle.dependency.DependencySpec.*;

public class LibACF extends AbstractDependencyFunction {

	static public String FUNCTION = "libACF";
	static public String NAME = "The Bukkit API";

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
	public DependencySpec[] getDependencies() {
		String dependency = "co.aikar:acf-" + this.getPlatform() + ":" + this.getVersion();
		if (this.getRelocate()) {
			return new DependencySpec[]{implementation(dependency)};
		} else {
			return new DependencySpec[]{compileOnly(dependency)};
		}
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

	@Override
	protected boolean isRelocatable() {
		return true;
	}

	@Override
	protected boolean isRelocatedByDefault() {
		return true;
	}

	@Override
	protected String[] getRelocatedPackages() {
		return new String[]{
				"co.aikar.commands",
				"co.aikar.locales",
		};
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
	//     libACF {
	//         platform = "paper"
	//     }
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
