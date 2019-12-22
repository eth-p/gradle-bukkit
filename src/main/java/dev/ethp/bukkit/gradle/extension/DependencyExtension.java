package dev.ethp.bukkit.gradle.extension;

import org.gradle.api.InvalidUserDataException;

public class DependencyExtension {

	// -------------------------------------------------------------------------------------------------------------
	// Constants
	// -------------------------------------------------------------------------------------------------------------

	public static final DependencyType REQUIRED = DependencyType.REQUIRED;
	public static final DependencyType OPTIONAL = DependencyType.OPTIONAL;


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: name
	// Required.
	//
	// This represents the name of the dependency.
	// It is essentially just the plugin name of some other plugin.
	//
	// dependency {
	//     name 'your-plugin'
	//     name = 'your permission'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (name == null) throw new InvalidUserDataException("Dependency name cannot be null.");
		if (name.isEmpty()) throw new InvalidUserDataException("Dependency name cannot be empty.");
		if (!Patterns.VALID_PLUGIN_NAME_PATTERN.matcher(name).find()) {
			throw new InvalidUserDataException("Dependency name can only be alphanumeric and hyphens/underscores.");
		}

		this.name = name;
	}

	public void name(String name) {
		if (this.name != null)
			throw new InvalidUserDataException("Dependency name cannot be specified multiple times.");

		this.setName(name);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: type
	// Optional.
	//
	// The dependency type.
	// This specifies if its required (hard) or optional (soft).
	// By default, it will be considered a hard dependency.
	//
	// dependency {
	//     ...
	//     required
	//     type = REQUIRED
	// }
	// -------------------------------------------------------------------------------------------------------------

	private DependencyType type;

	public DependencyType getType() {
		if (this.type != null) return this.type;
		return DependencyType.REQUIRED;
	}

	public void setType(DependencyType type) {
		this.type = type;
	}

	public void required() {
		if (this.type != null)
			throw new InvalidUserDataException("Dependency type cannot be specified multiple times.");

		setType(DependencyType.REQUIRED);
	}

	public void optional() {
		if (this.type != null)
			throw new InvalidUserDataException("Dependency type cannot be specified multiple times.");

		setType(DependencyType.OPTIONAL);
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
		if (object instanceof DependencyExtension) {
			if (this.getName() == null) return false;
			return this.getName().equals(((DependencyExtension) object).getName());
		}

		return this == object;
	}

}
