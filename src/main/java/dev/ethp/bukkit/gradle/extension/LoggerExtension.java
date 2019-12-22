package dev.ethp.bukkit.gradle.extension;

import org.gradle.api.InvalidUserDataException;

public class LoggerExtension {

	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: prefix
	// Optional.
	//
	// Overrides the prefix used in plugin log messages.
	//
	// logger {
	//     prefix = 'MyPlugin'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String prefix;

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void prefix(String prefix) {
		if (this.prefix != null)
			throw new InvalidUserDataException("Logger prefix cannot be specified multiple times.");

		setPrefix(prefix);
	}


}
