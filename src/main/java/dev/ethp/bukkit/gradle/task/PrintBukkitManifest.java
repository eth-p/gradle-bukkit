package dev.ethp.bukkit.gradle.task;

import java.util.Objects;
import java.util.Optional;

import dev.ethp.bukkit.gradle.extension.DependencyExtension;
import dev.ethp.bukkit.gradle.extension.DependencyType;
import dev.ethp.bukkit.gradle.extension.LoadType;
import dev.ethp.bukkit.gradle.extension.LoggerExtension;
import org.gradle.api.tasks.Internal;

/**
 * A task that prints Bukkit plugin manifest information.
 */
public class PrintBukkitManifest extends AbstractTask {

	public static String NAME = "printBukkitManifest";

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	@Internal
	public String getGroup() {
		return "Help";
	}

	@Override
	@Internal
	public String getDescription() {
		return "Displays all Bukkit plugin manifest information";
	}

	@Override
	void exec() {
		this.printHeader("Plugin Manifest:");
		if (extension.getTemplate()) {
			this.printInfo("This is a template. plugin.yml will not be generated.");
			this.printSpacer();
		}

		this.printProperty("name", Optional.ofNullable(extension.getName()));
		this.printProperty("version", Optional.ofNullable(extension.getVersion()));
		this.printSpacer();
		this.printProperty("main", Optional.ofNullable(extension.getMainClass()));
		this.printProperty("load", Optional.ofNullable(extension.getLoadAt()).map(LoadType::getString));
		this.printProperty("api-version", Optional.ofNullable(extension.getApi().getManifestVersion()));
		this.printProperty("database", Optional.ofNullable(extension.getApi().getDatabaseSupport()).map(Object::toString));
		this.printProperty("prefix", Optional.ofNullable(extension.getLogger()).map(LoggerExtension::getPrefix));
		this.printProperty("loadbefore", Optional.ofNullable(extension.getDeferredPlugins()).map(Objects::toString));

		this.printSpacer();
		this.printHeader("Plugin Description:");
		this.printProperty(null, Optional.ofNullable(extension.getDescription()));

		this.printSpacer();
		this.printHeader("Plugin Information:");
		this.printProperty("authors", Optional.ofNullable(extension.getAuthors()).map(Objects::toString));
		this.printProperty("website", Optional.ofNullable(extension.getWebsite()));

		if (extension.getDependencies() != null && !extension.getDependencies().isEmpty()) {
			this.printSpacer();
			this.printHeader("Dependencies:");

			if (extension.getDependencies() != null) {
				for (DependencyExtension dependency : extension.getDependencies()) {
					this.printItem((dependency.getType() == DependencyType.OPTIONAL)
							? String.format("%s (optional)", dependency.getName())
							: dependency.getName()
					);
				}
			}
		}
	}


}
