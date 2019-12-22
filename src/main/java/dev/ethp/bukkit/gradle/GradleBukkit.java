package dev.ethp.bukkit.gradle;

import groovy.lang.Closure;

import dev.ethp.bukkit.gradle.extension.BukkitExtension;

import dev.ethp.bukkit.gradle.function.AbstractDependencyFunction;
import dev.ethp.bukkit.gradle.function.AbstractDependencyFunction.Repository;
import dev.ethp.bukkit.gradle.function.BukkitApi;
import dev.ethp.bukkit.gradle.function.SpigotApi;
import dev.ethp.bukkit.gradle.function.VaultApi;
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
		Repository jitpack = new Repository("jitpack-repo", "https://jitpack.io/");

		target.getExtensions().add("bukkitApi", new BukkitApi(target, spigot));
		target.getExtensions().add("spigotApi", new SpigotApi(target, spigot));
		target.getExtensions().add("vaultApi", new VaultApi(target, jitpack));

		// Add maven central repository:
		target.getRepositories().mavenCentral();

		// Add hooks:
		target.afterEvaluate(new ValidateExtensions(target));

		target.getTasks().getByName("check", new Closure(this) {
			public void doCall(Task task) {
				task.dependsOn(checkBukkitManifest);
			}
		});

		target.getTasks().getByName("jar", new Closure(this) {
			public void doCall(Task task) {
				task.dependsOn(generateBukkitManifest);
			}
		});
	}

}
