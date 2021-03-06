package dev.ethp.bukkit.gradle.extension;

public enum LoadType {

	// -------------------------------------------------------------------------------------------------------------
	// Values
	// -------------------------------------------------------------------------------------------------------------

	STARTUP("STARTUP"),
	POSTWORLD("POSTWORLD");

	// -------------------------------------------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------------------------------------------

	private String string;

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	LoadType(String string) {
		this.string = string;
	}

	// -------------------------------------------------------------------------------------------------------------
	// Getters
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * Gets the load type value string.
	 * This is used in the plugin manifest.
	 *
	 * @return The load type value string.
	 */
	public String getString() {
		return this.string;
	}

}
