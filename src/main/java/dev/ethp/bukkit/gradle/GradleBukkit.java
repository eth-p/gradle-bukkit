package dev.ethp.bukkit.gradle;

import groovy.lang.Closure;

import dev.ethp.bukkit.gradle.dependency.RemoteDependency;
import dev.ethp.bukkit.gradle.extension.BukkitExtension;
import dev.ethp.bukkit.gradle.function.*;
import dev.ethp.bukkit.gradle.hook.ValidateExtensions;
import dev.ethp.bukkit.gradle.task.*;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.UnknownTaskException;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.jvm.tasks.Jar;

public class GradleBukkit implements Plugin<Project> {

	@Override
	public void apply(Project target) {
		// Add extensions:
		target.getExtensions().create(BukkitExtension.NAME, BukkitExtension.class, target);
		target.getExtensions().create(RemoteDependency.Extension.NAME, RemoteDependency.Extension.class, target);

		// Add tasks:
		TaskProvider<PrintBukkitManifest> printBukkitManifest = target.getTasks().register(PrintBukkitManifest.NAME, PrintBukkitManifest.class);
		TaskProvider<PrintBukkitCommands> printBukkitCommands = target.getTasks().register(PrintBukkitCommands.NAME, PrintBukkitCommands.class);
		TaskProvider<PrintBukkitPermissions> printBukkitPermissions = target.getTasks().register(PrintBukkitPermissions.NAME, PrintBukkitPermissions.class);
		TaskProvider<CheckBukkitManifest> checkBukkitManifest = target.getTasks().register(CheckBukkitManifest.NAME, CheckBukkitManifest.class);
		TaskProvider<GenerateBukkitManifest> generateBukkitManifest = target.getTasks().register(GenerateBukkitManifest.NAME, GenerateBukkitManifest.class);
		TaskProvider<DownloadLibraries> downloadLibraries = target.getTasks().register(DownloadLibraries.NAME, DownloadLibraries.class);

		// Add dependency functions:
		addDependencyFunction(target, BukkitApi.class);
		addDependencyFunction(target, SpigotApi.class);
		addDependencyFunction(target, PaperApi.class);
		addDependencyFunction(target, VaultApi.class);
		addDependencyFunction(target, PlaceholderApi.class);
		addDependencyFunction(target, LibBkCommon.class);
		addDependencyFunction(target, LibACF.class);
		addDependencyFunction(target, LibCommandApi.class);
		addDependencyFunction(target, RoseGarden.class);
		addDependencyFunction(target, RoseGui.class);

		// Add maven central repository:
		target.getRepositories().mavenCentral();

		// Add hooks:
		target.afterEvaluate(new ValidateExtensions(target));

		// Add task hooks:
		target.getPlugins().whenPluginAdded(new Closure(this) {
			public void doCall(Plugin plugin) {
				TaskContainer tasks = target.getTasks();
				
				if (plugin instanceof JavaPlugin) {
					tasks.getByName("check").dependsOn(checkBukkitManifest);
					tasks.getByName("jar").dependsOn(generateBukkitManifest);
					tasks.getByName("compileJava").dependsOn(downloadLibraries);
					
					((Jar) tasks.getByName("jar")).from(generateBukkitManifest);
				}
				
				try {
					tasks.getByName("compileKotlin").dependsOn(downloadLibraries);
				} catch (UnknownTaskException ex) {
					// This is fine.
				}
			}
		});
	}
	
	private void addDependencyFunction(Project target, Class<? extends AbstractDependencyFunction> function) {
		try {
			AbstractDependencyFunction instance = function.getConstructor(Project.class).newInstance(target);

			String identifier = (String) function.getField("FUNCTION").get(null);
			ExtensionContainer container = target.getDependencies().getExtensions();
			
			container.add(identifier, instance);
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
