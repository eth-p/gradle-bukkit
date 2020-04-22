package dev.ethp.bukkit.gradle.task;

import java.io.File;

import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.StopActionException;

/**
 * A task that verifies the Bukkit plugin's main class exists.
 * This can help make sure plugins are built with a main class.
 */
public class CheckBukkitManifest extends AbstractTask {

	public static String NAME = "checkBukkitManifest";

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public CheckBukkitManifest() {
		this.dependsOn("classes");
	}

	// -------------------------------------------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------------------------------------------

	boolean checkMain() {
		String main = this.extension.getMainClass();
		String file = main.replace(".", File.separator) + ".class";
		SourceSetContainer sourceSets = (SourceSetContainer) this.getProject().getProperties().get("sourceSets");
		for (SourceSet sourceSet : sourceSets) {
			for (File outputDir : sourceSet.getOutput().getClassesDirs().getFiles()) {
				if (new File(outputDir, file).exists()) {
					return true;
				}
			}
		}

		this.printError(String.format("The plugin main class '%s' does not exist.", main));
		return false;
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	@Internal
	public String getGroup() {
		return "Verification";
	}

	@Override
	@Internal
	public String getDescription() {
		return "Checks and validates the Bukkit plugin manifest";
	}

	@Override
	void exec() {
		if (extension.getTemplate()) return;

		boolean failure = false;

		// Checks:
		if (!checkMain()) failure = true;

		// Throw:
		if (failure) {
			throw new StopActionException();
		}
	}


}
