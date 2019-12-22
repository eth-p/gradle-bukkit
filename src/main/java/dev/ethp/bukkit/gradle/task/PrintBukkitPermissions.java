package dev.ethp.bukkit.gradle.task;

import java.util.Optional;

import dev.ethp.bukkit.gradle.extension.PermissionExtension;
import dev.ethp.bukkit.gradle.extension.PermissionValue;
import org.gradle.api.tasks.Internal;

/**
 * A task that prints Bukkit plugin permissions.
 */
public class PrintBukkitPermissions extends AbstractTask {

	public static String NAME = "printBukkitPermissions";

	// -------------------------------------------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------------------------------------------

	protected void printPermission(PermissionExtension permission) {
		this.printHeader(String.format("Permission: %s", permission.getName()));
		this.printProperty("description", Optional.ofNullable(permission.getDescription()));
		this.printProperty("default", Optional.ofNullable(permission.getValue()).map(PermissionValue::getString));
		this.printProperty("children", Optional.ofNullable(permission.getChildren()).map(Object::toString));
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
		return "Displays all registered Bukkit plugin permissions";
	}

	@Override
	void exec() {
		if (extension.getPermissions() == null) {
			this.printInfo("The plugin does not register any permissions.");
			return;
		}

		boolean first = true;
		for (PermissionExtension permission : extension.getPermissions()) {
			if (!first) this.printSpacer();
			if (first) first = false;
			this.printPermission(permission);
		}
	}


}