package dev.ethp.bukkit.gradle.extension;

import org.gradle.api.InvalidUserDataException;

public class ChildPermissionExtension {

	// -------------------------------------------------------------------------------------------------------------
	// Constants
	// -------------------------------------------------------------------------------------------------------------

	public static final ChildPermissionInheritance INHERIT = ChildPermissionInheritance.INHERIT;
	public static final ChildPermissionInheritance INVERT = ChildPermissionInheritance.INVERT;


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: name
	// Required.
	//
	// This represents the name of the child permission node.
	// The name must not have any whitespace, and cannot start with a hyphen.
	//
	// permission {
	//     name 'my.permission'
	//     name = 'my.permission'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (name != null) {
			if (name.isEmpty()) throw new InvalidUserDataException("Permission name cannot be empty.");
			if (name.startsWith("-")) throw new InvalidUserDataException("Permission name cannot start with a hyphen.");
			if (!Patterns.VALID_PERMISSION_NAME.matcher(name).find())
				throw new InvalidUserDataException("Permission name cannot contain whitespace.");
		}

		this.name = name;
	}

	public void name(String name) {
		if (name == null) throw new InvalidUserDataException("Permission name cannot be null.");
		if (this.name != null)
			throw new InvalidUserDataException("Permission name cannot be specified multiple times.");

		this.setName(name);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: inheritance
	// Optional.
	//
	// The inheritance value of the child permission node.
	//
	// child {
	//     ...
	//     invert
	//     inherit
	//     inheritance = INHERIT
	// }
	// -------------------------------------------------------------------------------------------------------------

	private ChildPermissionInheritance inheritance;

	public ChildPermissionInheritance getInheritance() {
		return this.inheritance;
	}

	public void setInheritance(ChildPermissionInheritance inheritance) {
		this.inheritance = inheritance;
	}

	public void inherit() {
		if (this.inheritance != null)
			throw new InvalidUserDataException("Permission inheritance cannot be specified multiple times.");

		this.inheritance = ChildPermissionInheritance.INHERIT;
	}

	public void invert() {
		if (this.inheritance != null)
			throw new InvalidUserDataException("Permission inheritance cannot be specified multiple times.");

		this.inheritance = ChildPermissionInheritance.INVERT;
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
		if (object instanceof ChildPermissionExtension) {
			if (this.getName() == null) return false;
			return this.getName().equals(((ChildPermissionExtension) object).getName());
		}

		return this == object;
	}

	@Override
	public String toString() {
		return (this.inheritance == ChildPermissionInheritance.INVERT ? "~" : "") + this.name;
	}

}
