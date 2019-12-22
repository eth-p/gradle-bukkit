package dev.ethp.bukkit.gradle.task;

import java.util.Optional;

import dev.ethp.bukkit.gradle.extension.CommandExtension;
import org.gradle.api.tasks.Internal;

/**
 * A task that prints Bukkit plugin commands.
 */
public class PrintBukkitCommands extends AbstractTask {

	public static String NAME = "printBukkitCommands";

	// -------------------------------------------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------------------------------------------

	protected void printCommand(CommandExtension command) {
		this.printHeader(String.format("Command: /%s", command.getName()));
		this.printProperty("description", Optional.ofNullable(command.getDescription()));
		this.printProperty("aliases", Optional.ofNullable(command.getAliases()).map(Object::toString));
		this.printProperty("permission", Optional.ofNullable(command.getPermission()));
		this.printProperty("permission-message", Optional.ofNullable(command.getPermissionMessage()));
		this.printProperty("usage", Optional.ofNullable(command.getUsageMessage()));
	}

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
		return "Displays all registered Bukkit plugin commands";
	}

	@Override
	void exec() {
		if (extension.getCommands() == null) {
			this.printInfo("The plugin does not register any commands.");
			return;
		}

		boolean first = true;
		for (CommandExtension command : extension.getCommands()) {
			if (!first) this.printSpacer();
			if (first) first = false;
			this.printCommand(command);
		}
	}


}