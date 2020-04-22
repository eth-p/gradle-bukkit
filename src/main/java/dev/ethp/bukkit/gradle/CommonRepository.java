package dev.ethp.bukkit.gradle;

import java.net.MalformedURLException;
import java.net.URL;

import groovy.lang.Closure;

import dev.ethp.bukkit.gradle.function.AbstractDependencyFunction;
import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;


/**
 * A class containing common Maven repositories used for Bukkit API dependencies.
 */
public class CommonRepository {

	// -------------------------------------------------------------------------------------------------------------
	// Constants
	// -------------------------------------------------------------------------------------------------------------

	public static final CommonRepository PAPER = new CommonRepository("papermc", "https://papermc.io/repo/repository/maven-public/");
	public static final CommonRepository SPIGOT = new CommonRepository("spigot-repo", "https://hub.spigotmc.org/nexus/content/repositories/snapshots/");
	public static final CommonRepository JITPACK = new CommonRepository("jitpack", "https://jitpack.io/");
	public static final CommonRepository SONATYPE = new CommonRepository("sonatype", "https://oss.sonatype.org/content/repositories/snapshots/");

	// -------------------------------------------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------------------------------------------

	private final String name;
	private final URL url;


	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * Creates a new common repository.
	 *
	 * @param name The repository name. This is used to prevent duplicate entries.
	 * @param url  The repository URL.
	 * @throws RuntimeException If the URL cannot be parsed.
	 */
	private CommonRepository(String name, String url) {
		try {
			this.name = name;
			this.url = new URL(url);
		} catch (MalformedURLException ex) {
			throw new RuntimeException(ex);
		}
	}


	// -------------------------------------------------------------------------------------------------------------
	// Accessors
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * Gets the repository name.
	 *
	 * @return The repository name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the repository URL.
	 *
	 * @return The repository URL.
	 */
	public URL getUrl() {
		return this.url;
	}

	/**
	 * Gets the repository object.
	 *
	 * @param project The project that it should belong to.
	 * @return The repository.
	 */
	public ArtifactRepository getRepository(Project project) {
		return project.getRepositories().maven(new Closure(this) {
			public void doCall() {
				MavenArtifactRepository repository = (MavenArtifactRepository) this.getDelegate();
				repository.setName(CommonRepository.this.getName());
				repository.setUrl(CommonRepository.this.getUrl());
			}
		});
	}

	// -------------------------------------------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * Injects the repository into the project.
	 *
	 * @param project The project to inject.
	 */
	public void inject(Project project) {
		if (project.getRepositories().findByName(this.getName()) == null) {
			project.getRepositories().add(this.getRepository(project));
		}
	}

}
