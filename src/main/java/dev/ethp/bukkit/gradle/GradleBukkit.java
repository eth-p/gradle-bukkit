package dev.ethp.bukkit.gradle;

import groovy.lang.Closure;

import dev.ethp.bukkit.gradle.extension.BukkitExtension;

import dev.ethp.bukkit.gradle.function.DependencyFunction;
import dev.ethp.bukkit.gradle.function.DependencyFunction.Repository;
import dev.ethp.bukkit.gradle.hook.ValidateExtensions;
import dev.ethp.bukkit.gradle.task.*;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

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
		Repository spigot = new Repository("spigot-repo", "https://hub.spigotmc.org/nexus/content/repositories/snapshots/");

		target.getExtensions().add("bukkitApi", new DependencyFunction(target, spigot, (p, e) -> "org.bukkit:bukkit:" + e.getApi().getLibraryVersion()));
		target.getExtensions().add("spigotApi", new DependencyFunction(target, spigot, (p, e) -> "org.spigotmc:spigot-api:" + e.getApi().getLibraryVersion()));

		// Add maven central repository:
		target.getRepositories().mavenCentral();

		// Add hooks:
		target.afterEvaluate(new ValidateExtensions(target));

		target.getTasks().getByName("check", new Closure(this) {
			public void doCall(Task task) {
				task.dependsOn(checkBukkitManifest);
			}
		});

		target.getTasks().getByName("assemble", new Closure(this) {
			public void doCall(Task task) {
				task.dependsOn(generateBukkitManifest);
			}
		});
	}

}
