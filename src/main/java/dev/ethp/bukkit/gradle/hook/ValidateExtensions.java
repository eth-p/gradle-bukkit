package dev.ethp.bukkit.gradle.hook;

import groovy.lang.Closure;

import dev.ethp.bukkit.gradle.extension.BukkitExtension;

import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;

public class ValidateExtensions extends Closure<Project> {

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public ValidateExtensions(Project project) {
		super(project);
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	public void doCall(Object... args) {
		BukkitExtension extension = ((Project) this.getDelegate()).getExtensions().getByType(BukkitExtension.class);

		if (!extension.getTemplate()) {
			if (extension.getName() == null) throw new InvalidUserDataException("The 'bukkit.name' property is required, but not set.");
			if (extension.getMainClass() == null) throw new InvalidUserDataException("The 'bukkit.main' property is required, but not set.");
			if (extension.getVersion() == null) throw new InvalidUserDataException("The 'bukkit.version' property is required, but not set.");
		}

		if (extension.getApi() == null) throw new InvalidUserDataException("The 'bukkit.api' property is required, but not set.");
		if (extension.getApi().getManifestVersion() == null) throw new InvalidUserDataException("The 'bukkit.api.manifestVersion' property is required, but not set.");
		if (extension.getApi().getLibraryVersion() == null) throw new InvalidUserDataException("The 'bukkit.api.libraryVersion' property is required, but not set.");
	}

}
