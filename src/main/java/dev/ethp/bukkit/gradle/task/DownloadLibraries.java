package dev.ethp.bukkit.gradle.task;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import dev.ethp.bukkit.gradle.dependency.RemoteDependency;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.StopActionException;

/**
 * A task that verifies the Bukkit plugin's main class exists.
 * This can help make sure plugins are built with a main class.
 */
public class DownloadLibraries extends AbstractTask {

	public static String NAME = "downloadBukkitLibraries";

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public DownloadLibraries() {
//		this.dependsOn("configurations");
	}
	
	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	@Internal
	public String getGroup() {
		return null;
	}

	@Override
	@Internal
	public String getDescription() {
		return "Downloads plugin dependencies that aren't available on Maven";
	}

	@Override
	void exec() {
		// Force dependencies to be configured.
		// This is needed for the dependency downloader to find dependencies properly. 
		this.getProject().getConfigurations().getByName("compileClasspath").forEach(dep -> { });
		
		// Download dependencies (if needed).
		RemoteDependency.Downloader downloader = new RemoteDependency.Downloader(this.getProject());
		if (!downloader.isCached()) {
			this.printInfo("Some Bukkit dependencies aren't hosted on Maven and need to be downloaded locally.");
			this.printInfo("This might take a while, but it will only happen once. Please be patient.");
			this.printInfo("");
			
			boolean success = downloader.download(download -> {
				this.printItem(download.toString());
			}, (url, error) -> {
				StringWriter sw = new StringWriter();
				error.printStackTrace(new PrintWriter(sw));
				
				this.printError("\nCould not download " + url.toString());
				this.printError(sw.toString());
			});
//			downloader.download((url, err) -> {
//				System.err.println("- Failed to download file from: " + url.toString());
//				System.err.println(err.toString());
//			});
		}

	}


}
