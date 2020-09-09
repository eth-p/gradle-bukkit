package dev.ethp.bukkit.gradle.dependency;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.internal.impldep.org.apache.commons.io.IOUtils;

/**
 * A dependency that is located at some URL.
 * This is a horrible hack that is required for libraries that aren't hosted on repositories.
 */
public class RemoteDependency extends AbstractDependency {

	public final URL url;

	/**
	 * Creates a new remote dependency.
	 *
	 * @param url The download URL of the dependency.
	 */
	public RemoteDependency(String url) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException ex) {
			throw new RuntimeException("Malformed url: " + url, ex);
		}
	}

	@Override
	public Dependency create(Project project) {
		String filename = new File(this.url.getPath()).getName();
		File destination = new File(new File(project.getBuildDir(), "downloads"), filename);

		// Add the dependency to a download task.
		Extension downloads = project.getExtensions().getByType(Extension.class);
		downloads.dependencies.put(destination, this.url);

		// Create the dependency.
		ConfigurableFileCollection files = project.getObjects().fileCollection();
		files.setFrom(destination);

		Dependency dependency = project.getDependencies().create(files);
		dependency.because(this.url.toString());

		return dependency;
	}

	/**
	 * An extension class used to keep track of what needs to be downloaded.
	 */
	static public class Extension {
		static public String NAME = "__internal__gradleBukkit_RemoteDependency";

		public Extension(Project project) {
		}

		private Map<File, URL> dependencies = new HashMap<>();

		public Map<File, URL> getDependencies() {
			return this.dependencies;
		}
	}

	/**
	 * A class that downloads remote URLs into local directories.
	 * This is used to download files from {@link RemoteDependency} dependencies.
	 */
	static public class Downloader {

		private Extension extension;
		private boolean requiresDownloadChecked;
		private boolean requiresDownload;

		public Downloader(Project project) {
			this.extension = project.getExtensions().getByType(Extension.class);
		}

		/**
		 * Checks if all the resources are cached.
		 *
		 * @return True if everything is cached.
		 */
		public boolean isCached() {
			if (this.requiresDownloadChecked) {
				return !this.requiresDownload;
			}

			this.check();
			return !this.requiresDownload;
		}

		/**
		 * Downloads all the uncached dependencies.
		 *
		 * @param statusPrinter A function that prints status messages about what's being downloaded.
		 * @param errorPrinter  A function that prints the error messages.
		 * @return True if all downloads were successful.
		 */
		public boolean download(Consumer<URL> statusPrinter, BiConsumer<URL, Exception> errorPrinter) {
			LinkedList<DownloaderTask> tasks = new LinkedList<>();
			for (File destination : this.extension.dependencies.keySet()) {
				if (!destination.exists()) {
					tasks.add(new DownloaderTask(this.extension.dependencies.get(destination), destination));
				}
			}

			// Start the download tasks.
			for (DownloaderTask task : tasks) {
				statusPrinter.accept(task.source);
				task.start();
			}

			// Wait for the download tasks.
			boolean success = true;
			for (DownloaderTask task : tasks) {
				try {
					task.join();
				} catch (InterruptedException ex) {
				}

				if (!task.wasSuccessful()) {
					success = false;
					errorPrinter.accept(task.source, task.getError());
				}
			}

			return success;
		}

		/**
		 * Checks if any resources need downloading.
		 */
		private void check() {
			for (File dependency : this.extension.dependencies.keySet()) {
				if (!dependency.exists()) {
					this.requiresDownload = true;
					this.requiresDownloadChecked = true;
					return;
				}
			}

			this.requiresDownload = false;
			this.requiresDownloadChecked = true;
		}

	}

	/**
	 * A thread used to download a file from a remote URL.
	 */
	static private class DownloaderTask extends Thread {

		private File destination;
		private URL source;
		private Exception error;

		public DownloaderTask(URL source, File destination) {
			this.source = source;
			this.destination = destination;
		}

		@Override
		public void run() {
			try {
				if (!this.destination.getParentFile().exists()) {
					this.destination.getParentFile().mkdirs();
				}

				URLConnection srcConnection = this.source.openConnection();
				srcConnection.setDoInput(true);
				srcConnection.setDoOutput(false);

				OutputStream destOut = new FileOutputStream(this.destination);
				InputStream srcIn = srcConnection.getInputStream();

				IOUtils.copyLarge(srcIn, destOut); // UNSAFE: Relies on Gradle internals.

				srcIn.close();
				destOut.close();
			} catch (IOException ex) {
				this.error = ex;
			}
		}

		/**
		 * Checks if the task was successful.
		 *
		 * @return Whether the task was successful.
		 */
		public boolean wasSuccessful() {
			return this.error == null;
		}

		/**
		 * Gets the last error thrown by this task.
		 *
		 * @return The exception.
		 */
		public Exception getError() {
			return this.error;
		}

	}


}
