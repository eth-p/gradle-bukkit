package dev.ethp.bukkit.gradle.extension;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.gradle.api.InvalidUserDataException;

public class CommandExtension {

	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: name
	// Required.
	//
	// This represents the name of the command.
	// The name must not have any whitespace, and should not include the leading slash.
	//
	// command {
	//     name 'help'
	//     name = 'help'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (name.isEmpty()) throw new InvalidUserDataException("Command name cannot be empty.");
		if (!Patterns.VALID_COMMAND_NAME.matcher(name).find())
			throw new InvalidUserDataException("Command name cannot contain whitespace.");

		this.name = name;
	}

	public void name(String name) {
		if (name == null) throw new InvalidUserDataException("Command name cannot be null.");
		if (this.name != null)
			throw new InvalidUserDataException("Command name cannot be specified multiple times.");

		this.setName(name);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: description
	// Optional.
	//
	// A short description of what the command does.
	//
	// command {
	//     ...
	//     description 'Replies with pong.'
	//     description = 'Replies with pong.'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String description;

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void description(String description) {
		if (description == null) throw new InvalidUserDataException("Command description cannot be null.");
		if (this.description != null)
			throw new InvalidUserDataException("Command description cannot be specified multiple times.");

		setDescription(description);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: usageMessage
	// Optional.
	//
	// A short message explaining how to use the command.
	//
	// command {
	//     ...
	//     usage 'Usage: /ping'
	//     usageMessage 'Usage: /ping'
	//     usageMessage = 'Usage: /ping'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String usageMessage;

	public String getUsageMessage() {
		return this.usageMessage;
	}

	public void setUsageMessage(String message) {
		this.usageMessage = message;
	}

	public void usageMessage(String message) {
		if (message == null) throw new InvalidUserDataException("Command usage message cannot be null.");
		if (this.usageMessage != null)
			throw new InvalidUserDataException("Command usage message cannot be specified multiple times.");

		setUsageMessage(message);
	}

	public void usage(String message) {
		this.usageMessage(message);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: permissionMessage
	// Optional.
	//
	// A message to show the user when they don't have permission to use the command.
	//
	// command {
	//     ...
	//     permissionMessage 'Usage: /ping'
	//     permissionMessage = 'Usage: /ping'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String permissionMessage;

	public String getPermissionMessage() {
		return this.permissionMessage;
	}

	public void setPermissionMessage(String message) {
		this.permissionMessage = message;
	}

	public void permissionMessage(String message) {
		if (message == null) throw new InvalidUserDataException("Command permission message cannot be null.");
		if (this.permissionMessage != null)
			throw new InvalidUserDataException("Command permission message cannot be specified multiple times.");

		setUsageMessage(message);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: permission
	// Optional.
	//
	// The most basic permission node required to use the command.
	//
	// command {
	//     ...
	//     permission 'command.ping'
	//     description = 'command.ping'
	// }
	//
	// -------------------------------------------------------------------------------------------------------------

	private String permission;

	public String getPermission() {
		return this.permission;
	}

	public void setPermission(String permission) {
		if (permission != null) {
			if (permission.startsWith("-"))
				throw new InvalidUserDataException("Command permission cannot start with a hyphen.");
			if (permission.matches("^\\S*(?:\\s\\S*)+$"))
				throw new InvalidUserDataException("Command permission cannot contain whitespace.");
		}

		this.permission = permission;
	}

	public void permission(String permission) {
		if (permission == null) throw new InvalidUserDataException("Command name cannot be null.");
		if (this.permission != null)
			throw new InvalidUserDataException("Command permission cannot be specified multiple times.");

		this.setPermission(permission);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: aliases
	// Optional.
	//
	// A list of alternate command names a user may use instead.
	//
	// command {
	//     ...
	//     aliases = ['ping-pong']
	//     alias 'ping-pong-command'
	// }
	//
	// -------------------------------------------------------------------------------------------------------------

	private Set<String> aliases;

	public Set<String> getAliases() {
		return this.aliases;
	}

	public void setAliases(Collection<String> aliases) {
		this.aliases = new HashSet<>();
		aliases.forEach(this::alias);
		if (Patterns.VALID_COMMAND_NAME.matcher(name).find())
			throw new InvalidUserDataException("Command name cannot contain whitespace.");

		this.permission = permission;
	}

	public void alias(String alias) {
		if (this.aliases == null) this.aliases = new HashSet<>();

		if (alias == null) throw new InvalidUserDataException("Command alias cannot be null.");
		if (!Patterns.VALID_COMMAND_NAME.matcher(name).find())
			throw new InvalidUserDataException("Command alias cannot contain whitespace.");
		if (this.aliases.contains(alias))
			throw new InvalidUserDataException(String.format("An alias named '%s' is already specified.", alias));

		this.aliases.add(alias);
	}


	// -------------------------------------------------------------------------------------------------------------
	// Overrides
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public int hashCode() {
		if (this.getName() == null) return 0;
		return this.getName().hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof CommandExtension) {
			if (this.getName() == null) return false;
			return this.getName().equals(((CommandExtension) object).getName());
		}

		return this == object;
	}

}
