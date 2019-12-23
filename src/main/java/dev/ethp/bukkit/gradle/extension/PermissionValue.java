package dev.ethp.bukkit.gradle.extension;

public enum PermissionValue {

	// -------------------------------------------------------------------------------------------------------------
	// Values
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * A non-operator player.
	 */
	PLAYER(0b01, "not op"),

	/**
	 * An operator player.
	 */
	OPERATOR(0b10, "op"),

	/**
	 * All players.
	 */
	ALL(0b11, "true"),

	/**
	 * No players.
	 */
	NONE(0b00, "false");

	// -------------------------------------------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------------------------------------------

	private int mask;
	private String string;

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	PermissionValue(int mask, String string) {
		this.mask = mask;
		this.string = string;
	}

	// -------------------------------------------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * Gets the permission mask.
	 * This is used to perform bitwise arithmetic.
	 *
	 * @return The permission mask.
	 */
	public int getMask() {
		return mask;
	}

	/**
	 * Gets the permission value string.
	 * This is used in the plugin manifest.
	 *
	 * @return The permission value string.
	 */
	public String getString() {
		return this.string;
	}

	/**
	 * Gets a PermissionValue that corresponds to a specific mask.
	 * It would be advisable to use {@code mask & ALL.getMask()} to ensure it falls into a correct range.
	 *
	 * @param mask The permission mask.
	 * @return The corresponding permission value.
	 * @throws IllegalArgumentException When the mask is invalid.
	 */
	public static PermissionValue valueOfMask(int mask) throws IllegalArgumentException {
		for (PermissionValue value : PermissionValue.values()) {
			if (value.mask == mask) return value;
		}
		throw new IllegalArgumentException("Invalid mask: " + mask);
	}

}
