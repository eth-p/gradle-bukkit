package dev.ethp.bukkit.gradle;

import groovy.lang.Closure;

import dev.ethp.bukkit.gradle.extension.BukkitExtension;

import dev.ethp.bukkit.gradle.function.BukkitApi;
import dev.ethp.bukkit.gradle.function.SpigotApi;
import dev.ethp.bukkit.gradle.function.VaultApi;
import dev.ethp.bukkit.gradle.function.PaperApi;
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
		target.getExtensions().add("bukkitApi", new BukkitApi(target));
		target.getExtensions().add("spigotApi", new SpigotApi(target));
		target.getExtensions().add("paperApi", new PaperApi(target));
		target.getExtensions().add("vaultApi", new VaultApi(target));

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

}
