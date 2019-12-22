package dev.ethp.bukkit.gradle.function;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.BiFunction;

import groovy.lang.Closure;

import dev.ethp.bukkit.gradle.extension.BukkitExtension;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencyResolutionListener;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.ResolvableDependencies;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;

public class DependencyFunction extends Closure<Project> implements DependencyResolutionListener {

	// -------------------------------------------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------------------------------------------

	private Repository repository;
	private BiFunction<Project, BukkitExtension, String> mapper;

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public DependencyFunction(Project project, Repository repo, BiFunction<Project, BukkitExtension, String> dependency) {
		super(project);
		this.repository = repo;
		this.mapper = dependency;
	}

	protected void injectRepository() {
		Project project = (Project) this.getDelegate();
		if (project.getRepositories().findByName(this.repository.name) != null) return;
		project.getRepositories().add(project.getRepositories().maven(new Closure(this) {
			public void doCall() {
				MavenArtifactRepository repository = (MavenArtifactRepository) this.getDelegate();
				repository.setName(DependencyFunction.this.repository.getName());
				repository.setUrl(DependencyFunction.this.repository.getUrl());
			}
		}));
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	public void doCall() {
		this.injectRepository();
		Project project = (Project) this.getDelegate();
		project.getGradle().addListener(this);
	}

	@Override
	public void beforeResolve(ResolvableDependencies resolvableDependencies) {
		Project project = (Project) this.getDelegate();
		BukkitExtension extension = project.getExtensions().getByType(BukkitExtension.class);

		DependencySet dependencies = project.getConfigurations().getByName("compileOnly").getDependencies();
		Dependency dependency = project.getDependencies().create(this.mapper.apply(project, extension));
		dependencies.add(dependency);

		project.getGradle().removeListener(this);
	}

	@Override
	public void afterResolve(ResolvableDependencies resolvableDependencies) {}


	// -------------------------------------------------------------------------------------------------------------
	// Classes
	// -------------------------------------------------------------------------------------------------------------

	public static class Repository {

		private final String name;
		private final URL url;

		public Repository(String name, String url) {
			try {
				this.name = name;
				this.url = new URL(url);
			} catch (MalformedURLException ex) {
				throw new RuntimeException(ex);
			}
		}

		public String getName() {
			return this.name;
		}

		public URL getUrl() {
			return this.url;
		}

	}

}