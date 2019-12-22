package dev.ethp.bukkit.gradle.extension;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import groovy.lang.Closure;

import org.gradle.api.InvalidUserDataException;
import org.gradle.util.ConfigureUtil;

public class PermissionExtension {

	// -------------------------------------------------------------------------------------------------------------
	// Constants
	// -------------------------------------------------------------------------------------------------------------

	public static final PermissionValue OP = PermissionValue.OPERATOR;
	public static final PermissionValue OPERATOR = PermissionValue.OPERATOR;
	public static final PermissionValue NOT_OP = PermissionValue.PLAYER;
	public static final PermissionValue NOT_OPERATOR = PermissionValue.PLAYER;
	public static final PermissionValue ALL = PermissionValue.ALL;


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: name
	// Required.
	//
	// This represents the name of the permission node.
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
	// PROPERTY: description
	// Optional.
	//
	// A short description of what this permission allows.
	//
	// permission {
	//     ...
	//     description 'Allows you to use /ping'
	//     description = 'Allows you to use /ping'
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
		if (description == null) throw new InvalidUserDataException("Permission description cannot be null.");
		if (this.description != null)
			throw new InvalidUserDataException("Permission description cannot be specified multiple times.");

		setDescription(description);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: default
	// Required.
	//
	// The default value of the permission node.
	//
	// permission {
	//     ...
	//     allow GROUP
	//     deny GROUP
	// }
	//
	// The following groups are supported:
	//
	//     OPERATORS   => Players who are server operators.
	//     PLAYERS     => Players who are not server operators.
	//     ALL         => All players.
	//
	// When specifying `allow`/`deny` actions, they will be applied in the order that they are written.
	// The default value is `deny ALL`.
	// -------------------------------------------------------------------------------------------------------------

	private PermissionValue value;

	public PermissionValue getValue() {
		return this.value;
	}

	public void setValue(PermissionValue value) {
		if (value == null) throw new InvalidUserDataException("Permission value cannot be null.");
		this.value = value;
	}

	public void allow(PermissionValue group) {
		if (group == null) throw new InvalidUserDataException("Permission group cannot be null.");

		PermissionValue current = this.value != null ? this.value : PermissionValue.NONE;
		PermissionValue updated = PermissionValue.valueOfMask(current.getMask() | group.getMask());

		this.value = updated;
	}

	public void deny(PermissionValue group) {
		if (group == null) throw new InvalidUserDataException("Permission group cannot be null.");

		PermissionValue current = this.value != null ? this.value : PermissionValue.NONE;
		PermissionValue updated = PermissionValue.valueOfMask(current.getMask() & group.getMask());

		this.value = updated;
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: children
	// Optional.
	//
	// The child permissions of the permission.
	//
	// permission {
	//     ...
	//     children = []
	//     child {
	//         ...
	//     }
	// }
	// -------------------------------------------------------------------------------------------------------------

	private Set<ChildPermissionExtension> children;

	public Set<ChildPermissionExtension> getChildren() {
		return this.children;
	}

	public void setChildren(Collection<ChildPermissionExtension> children) {
		if (children == null) {
			this.children = null;
			return;
		}

		this.children = new HashSet<>();
		this.children.addAll(children);
	}

	public void child(ChildPermissionExtension permission) {
		if (this.children == null) this.children = new HashSet<>();

		// Validate.
		if (permission == null) throw new InvalidUserDataException("The permission cannot be null.");
		if (permission.getName() == null) throw new InvalidUserDataException("The permission requires a name.");
		if (this.children.contains(permission)) {
			throw new InvalidUserDataException(String.format("A permission named '%s' is already registered.", permission.getName()));
		}

		// Add.
		this.children.add(permission);
	}

	public void child(Closure closure) {
		ChildPermissionExtension permission = new ChildPermissionExtension();
		ConfigureUtil.configure(closure, permission);
		child(permission);
	}

	public void child(String name) {
		ChildPermissionExtension permission = new ChildPermissionExtension();
		permission.setName(name);
		child(permission);
	}

	public void child(String name, ChildPermissionInheritance inheritance) {
		ChildPermissionExtension permission = new ChildPermissionExtension();
		permission.setName(name);
		permission.setInheritance(inheritance);
		child(permission);
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
		if (object instanceof PermissionExtension) {
			if (this.getName() == null) return false;
			return this.getName().equals(((PermissionExtension) object).getName());
		}

		return this == object;
	}

}
