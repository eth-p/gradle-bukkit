package dev.ethp.bukkit.gradle.function;

import java.net.MalformedURLException;
import java.net.URL;

import groovy.lang.Closure;

import dev.ethp.bukkit.gradle.extension.BukkitExtension;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencyResolutionListener;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.ResolvableDependencies;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;

public abstract class AbstractDependencyFunction extends Closure<Project> implements DependencyResolutionListener {

	// -------------------------------------------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------------------------------------------

	private Repository repository;

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public AbstractDependencyFunction(Project project, Repository repo) {
		super(project);
		this.repository = repo;
	}

	// -------------------------------------------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------------------------------------------

	private void injectRepository() {
		Project project = this.getProject();
		if (project.getRepositories().findByName(this.repository.name) != null) return;
		project.getRepositories().add(project.getRepositories().maven(new Closure(this) {
			public void doCall() {
				MavenArtifactRepository repository = (MavenArtifactRepository) this.getDelegate();
				repository.setName(AbstractDependencyFunction.this.repository.getName());
				repository.setUrl(AbstractDependencyFunction.this.repository.getUrl());
			}
		}));
	}

	protected void inject() {
		this.injectRepository();
		this.getProject().getGradle().addListener(this);
	}

	protected BukkitExtension getExtension() {
		return this.getProject().getExtensions().getByType(BukkitExtension.class);
	}

	protected Project getProject() {
		return (Project) this.getDelegate();
	}

	// -------------------------------------------------------------------------------------------------------------
	// Abstract
	// -------------------------------------------------------------------------------------------------------------

	public abstract Dependency createDependency(DependencyHandler handler);

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public void beforeResolve(ResolvableDependencies resolvableDependencies) {
		Project project = this.getProject();

		DependencySet dependencies = project.getConfigurations().getByName("compileOnly").getDependencies();
		Dependency dependency = createDependency(project.getDependencies());
		dependencies.add(dependency);

		project.getGradle().removeListener(this);
	}

	@Override
	public void afterResolve(ResolvableDependencies resolvableDependencies) {
	}


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