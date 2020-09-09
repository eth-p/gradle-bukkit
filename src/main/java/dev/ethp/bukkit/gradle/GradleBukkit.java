package dev.ethp.bukkit.gradle;

import groovy.lang.Closure;

import dev.ethp.bukkit.gradle.extension.BukkitExtension;

import dev.ethp.bukkit.gradle.function.*;
import dev.ethp.bukkit.gradle.hook.ValidateExtensions;
import dev.ethp.bukkit.gradle.task.*;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPlugin;

public class GradleBukkit implements Plugin<Project> {

	@Override
	public void apply(Project target) {
		// Add extensions:
		target.getExtensions().create(BukkitExtension.NAME, BukkitExtension.class, target);

		// Add tasks:
		Task printBukkitManifest = target.getTasks().create(PrintBukkitManifest.NAME, PrintBukkitManifest.class);
		Task printBukkitCommands = target.getTasks().create(PrintBukkitCommands.NAME, PrintBukkitCommands.class);
		Task printBukkitPermissions = target.getTasks().create(PrintBukkitPermissions.NAME, PrintBukkitPermissions.class);
		Task checkBukkitManifest = target.getTasks().create(CheckBukkitManifest.NAME, CheckBukkitManifest.class);
		Task generateBukkitManifest = target.getTasks().create(GenerateBukkitManifest.NAME, GenerateBukkitManifest.class);

		// Add dependency functions:
		addDependencyFunction(target, BukkitApi.class);
		addDependencyFunction(target, SpigotApi.class);
		addDependencyFunction(target, PaperApi.class);

		addDependencyFunction(target, VaultApi.class);
		addDependencyFunction(target, PlaceholderApi.class);

		addDependencyFunction(target, LibBkCommon.class);
		addDependencyFunction(target, LibACF.class);

		// Add maven central repository:
		target.getRepositories().mavenCentral();

		// Add hooks:
		target.afterEvaluate(new ValidateExtensions(target));

		// Add task hooks:
		target.getPlugins().whenPluginAdded(new Closure(this) {
			public void doCall(Plugin plugin) {
				if (plugin instanceof JavaPlugin) {
					target.getTasks().getByName("check").dependsOn(checkBukkitManifest);
					target.getTasks().getByName("jar").dependsOn(generateBukkitManifest);
				}
			}
		});
	}

	private void addDependencyFunction(Project target, Class<? extends AbstractDependencyFunction> function) {
		try {
			String identifier = (String) function.getField("FUNCTION").get(null);
			AbstractDependencyFunction instance = (AbstractDependencyFunction) function.getConstructor(Project.class).newInstance(target);

			target.getExtensions().add(identifier, instance);
			target.afterEvaluate(new Closure(this) {
				public void doCall() {
					if (instance.isConfigured() && !instance.isInjected()) {
						instance.inject();
					}
				}
			});
		} catch (Exception e) {
			throw new RuntimeException("Invalid gradle-bukkit dependency function.", e);
		}
	}

}
