package dev.ethp.bukkit.gradle.extension;

import java.util.*;

import groovy.lang.Closure;

import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;
import org.gradle.util.ConfigureUtil;

public class BukkitExtension {

	static public String NAME = "bukkit";


	// -------------------------------------------------------------------------------------------------------------
	// Constants
	// -------------------------------------------------------------------------------------------------------------

	static public final DependencyType REQUIRED = DependencyType.REQUIRED;
	static public final DependencyType OPTIONAL = DependencyType.OPTIONAL;

	static public final LoadType STARTUP = LoadType.STARTUP;
	static public final LoadType POSTWORLD = LoadType.POSTWORLD;


	// -------------------------------------------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------------------------------------------

	private Project project;


	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public BukkitExtension(Project project) {
		this.project = project;
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: name
	// Required.
	//
	// This represents the name of the plugin.
	// The name can only be alphanumeric with hyphens or underscores.
	//
	// If a name is not provided, this will attempt to generate one from the project name.
	// It is highly recommended to explicitly specify a name, however.
	//
	// bukkit {
	//     name 'my-plugin'
	//     name = 'my plugin'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String name;

	public String getName() {
		if (this.name != null) return this.name;
		return this.project.getName().replaceAll("[^A-Za-z_\\-]+", "_");
	}

	public void setName(String name) {
		if (name == null) throw new InvalidUserDataException("Plugin name cannot be null.");
		if (name.isEmpty()) throw new InvalidUserDataException("Plugin name cannot be empty.");
		if (!Patterns.VALID_PLUGIN_NAME_PATTERN.matcher(name).find()) {
			throw new InvalidUserDataException("Plugin name only be alphanumeric and hyphens/underscores.");
		}

		this.name = name;
	}

	public void name(String name) {
		if (this.name != null)
			throw new InvalidUserDataException("Plugin name cannot be specified multiple times.");

		this.setName(name);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: version
	// Required.
	//
	// The plugin version.
	//
	// bukkit {
	//     ...
	//     version '1.0'
	//     version = '1.0'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String version;

	public String getVersion() {
		if (this.version != null) return this.version;
		if (!this.project.getVersion().toString().equals("unspecified")) return this.project.getVersion().toString();
		return this.version;
	}

	public void setVersion(String version) {
		if (version == null) throw new InvalidUserDataException("Plugin version cannot be null.");
		this.version = version;
	}

	public void version(String version) {
		if (this.version != null)
			throw new InvalidUserDataException("Plugin version cannot be specified multiple times.");

		setVersion(version);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: mainClass
	// Required.
	//
	// The main class of the plugin.
	// If provided, this will disable the 'template' property.
	//
	// bukkit {
	//     ...
	//     main 'com.example.bukkit.BukkitPlugin'
	//     mainClass = 'com.example.bukkit.BukkitPlugin'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String mainClass;

	public String getMainClass() {
		return this.mainClass;
	}

	public void setMainClass(String mainClass) {
		if (mainClass == null) throw new InvalidUserDataException("Plugin main class cannot be null.");

		this.mainClass = mainClass;
		this.setTemplate(false);
	}

	public void mainClass(String mainClass) {
		if (this.mainClass != null)
			throw new InvalidUserDataException("Plugin main class cannot be specified multiple times.");

		setMainClass(mainClass);
	}

	public void main(String mainClass) {
		mainClass(mainClass);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: api
	// Required.
	//
	// The API information.
	//
	// bukkit {
	//     ...
	//     api {
	//         ...
	//     }
	// }
	// -------------------------------------------------------------------------------------------------------------

	private BukkitApiExtension api;

	public BukkitApiExtension getApi() {
		return this.api;
	}

	public void setApi(BukkitApiExtension api) {
		if (api == null) throw new InvalidUserDataException("Plugin API information cannot be null.");
		this.api = api;
	}

	public void api(Closure closure) {
		this.api = new BukkitApiExtension();
		ConfigureUtil.configure(closure, this.api);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: description
	// Optional.
	//
	// A short human-friendly description of the plugin.
	//
	// bukkit {
	//     ...
	//     description 'My awesome plugin.'
	//     description = 'My awesome plugin.'
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
		if (description == null) throw new InvalidUserDataException("Plugin description cannot be null.");
		if (this.description != null)
			throw new InvalidUserDataException("Plugin description cannot be specified multiple times.");

		setDescription(description);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: website
	// Optional.
	//
	// The plugin or plugin author's website.
	// If no other option is available, a link to the Bukkit or Spigot plugin page is recommended.
	//
	// bukkit {
	//     ...
	//     website 'https://example.com/'
	//     website = 'https://example.com/'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String website;

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public void website(String website) {
		if (website == null) throw new InvalidUserDataException("Plugin website cannot be null.");
		if (this.website != null)
			throw new InvalidUserDataException("Plugin website cannot be specified multiple times.");

		setWebsite(website);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: authors
	// Optional.
	//
	// The plugin author(s).
	//
	// bukkit {
	//     ...
	//     authors = ['john', 'sally']
	//     author 'tim'
	//     author 'jane'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private Set<String> authors;

	public Set<String> getAuthors() {
		return this.authors;
	}

	public void setAuthors(Collection<String> authors) {
		if (authors == null) {
			this.authors = null;
			return;
		}

		this.authors = new HashSet<>();
		this.authors.addAll(authors);
	}

	public void author(String author) {
		if (author == null) throw new InvalidUserDataException("The plugin author cannot be null.");
		if (this.authors == null) this.authors = new HashSet<>();

		// Cleanup.
		author = author.trim();

		// Validate.
		if (this.authors.contains(author)) {
			throw new InvalidUserDataException(String.format("An author named '%s' is already in the list.", author));
		}

		// Add.
		this.authors.add(author);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: logger
	// Optional.
	//
	// The logger information.
	//
	// bukkit {
	//     ...
	//     logger {
	//         ...
	//     }
	// }
	// -------------------------------------------------------------------------------------------------------------

	private LoggerExtension logger;

	public LoggerExtension getLogger() {
		return this.logger;
	}

	public void setLogger(LoggerExtension logger) {
		if (logger == null) throw new InvalidUserDataException("Plugin logger information cannot be null.");
		this.logger = logger;
	}

	public void logger(Closure closure) {
		this.logger = new LoggerExtension();
		ConfigureUtil.configure(closure, this.logger);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: permissions
	// Optional.
	//
	// The permissions registered by the plugin.
	//
	// bukkit {
	//     ...
	//     permission 'my.permission'
	//     permission {
	//         ...
	//     }
	// }
	// -------------------------------------------------------------------------------------------------------------

	private Set<PermissionExtension> permissions;

	public Set<PermissionExtension> getPermissions() {
		return this.permissions;
	}

	public void setPermissions(Collection<PermissionExtension> permissions) {
		if (permissions == null) {
			this.permissions = null;
			return;
		}

		this.permissions = new HashSet<>();
		this.permissions.addAll(permissions);
	}

	public void permission(PermissionExtension permission) {
		if (this.permissions == null) this.permissions = new HashSet<>();

		// Validate.
		if (permission == null) throw new InvalidUserDataException("The permission cannot be null.");
		if (permission.getName() == null) throw new InvalidUserDataException("The permission requires a name.");
		if (this.permissions.contains(permission)) {
			throw new InvalidUserDataException(String.format("A permission named '%s' is already registered.", permission.getName()));
		}

		// Add.
		this.permissions.add(permission);
	}

	public void permission(Closure closure) {
		PermissionExtension permission = new PermissionExtension();
		ConfigureUtil.configure(closure, permission);
		permission(permission);
	}

	public void permission(String name, Closure closure) {
		PermissionExtension permission = new PermissionExtension();
		permission.setName(name);
		ConfigureUtil.configure(closure, permission);
		permission(permission);
	}

	public void permission(String name) {
		PermissionExtension permission = new PermissionExtension();
		permission.setName(name);
		permission(permission);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: commands
	// Optional.
	//
	// The commands registered by the plugin.
	//
	// bukkit {
	//     ...
	//     command 'mycommand'
	//     command {
	//         ...
	//     }
	// }
	// -------------------------------------------------------------------------------------------------------------

	private Set<CommandExtension> commands;

	public Set<CommandExtension> getCommands() {
		return this.commands;
	}

	public void setCommands(Collection<CommandExtension> commands) {
		if (commands == null) {
			this.commands = null;
			return;
		}

		this.commands = new HashSet<>();
		this.commands.addAll(commands);
	}

	public void command(CommandExtension command) {
		if (this.commands == null) this.commands = new HashSet<>();

		if (command == null) throw new InvalidUserDataException("The command cannot be null.");
		if (command.getName() == null) throw new InvalidUserDataException("The command requires a name.");
		if (this.commands.contains(command)) {
			throw new InvalidUserDataException(String.format("A command named '%s' is already registered.", command.getName()));
		}

		this.commands.add(command);
	}

	public void command(String name, Closure closure) {
		CommandExtension command = new CommandExtension();
		command.setName(name);
		ConfigureUtil.configure(closure, command);
		command(command);
	}

	public void command(Closure closure) {
		CommandExtension command = new CommandExtension();
		ConfigureUtil.configure(closure, command);
		command(command);
	}

	public void command(String name) {
		CommandExtension command = new CommandExtension();
		command.setName(name);
		command(command);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: dependencies
	// Optional.
	//
	// The dependencies needed by the plugin.
	//
	// bukkit {
	//     ...
	//     dependency R {
	//         ...
	//     }
	// }
	// -------------------------------------------------------------------------------------------------------------

	private Set<DependencyExtension> dependencies;

	public Set<DependencyExtension> getDependencies() {
		return this.dependencies;
	}

	public void setDependencies(Collection<DependencyExtension> dependencies) {
		if (dependencies == null) {
			this.dependencies = null;
			return;
		}

		this.dependencies = new HashSet<>();
		this.dependencies.addAll(dependencies);
	}

	public void dependency(Closure closure) {
		if (this.dependencies == null) this.dependencies = new HashSet<>();

		// Configure.
		DependencyExtension dependency = new DependencyExtension();
		ConfigureUtil.configure(closure, dependency);

	}

	public void dependency(DependencyExtension dependency) {
		if (this.dependencies == null) this.dependencies = new HashSet<>();

		if (dependency == null) throw new InvalidUserDataException("The dependency cannot be null.");
		if (dependency.getName() == null) throw new InvalidUserDataException("The dependency requires a name.");
		if (this.dependencies.contains(dependency)) {
			throw new InvalidUserDataException(String.format("A plugin named '%s' is already a dependency.", dependency.getName()));
		}

		// Add.
		this.dependencies.add(dependency);
	}

	public void dependency(String name, DependencyType type) {
		DependencyExtension dependency = new DependencyExtension();
		dependency.setName(name);
		dependency.setType(type);
		dependency(dependency);
	}

	public void dependency(String name) {
		dependency(name, DependencyType.REQUIRED);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: deferredPlugins
	// Optional.
	//
	// A list of plugins that should be loaded after this plugin.
	//
	// bukkit {
	//     ...
	//     defers 'other-plugin'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private Set<String> deferredPlugins;

	public Set<String> getDeferredPlugins() {
		return this.deferredPlugins;
	}

	public void setDeferredPlugins(Collection<String> plugins) {
		if (plugins == null) {
			this.deferredPlugins = null;
			return;
		}

		this.deferredPlugins = new HashSet<>();
		this.deferredPlugins.addAll(plugins);
	}

	public void defers(String plugin) {
		if (this.deferredPlugins == null) this.deferredPlugins = new HashSet<>();

		if (plugin == null) throw new InvalidUserDataException("Deferred plugin name cannot be null.");
		if (plugin.isEmpty()) throw new InvalidUserDataException("Deferred plugin name cannot be empty.");
		if (!Patterns.VALID_PLUGIN_NAME_PATTERN.matcher(plugin).find()) {
			throw new InvalidUserDataException("Deferred plugin name only be alphanumeric and hyphens/underscores.");
		}
		if (this.deferredPlugins.contains(plugin)) {
			throw new InvalidUserDataException(String.format("A deferred plugin named '%s' is already registered.", plugin));
		}

		// Add.
		this.deferredPlugins.add(plugin);
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: loadAt
	// Optional.
	//
	// The loading type.
	// This specifies when the server should load the plugin.
	// By default, it will be loaded after all the worlds have been loaded.
	//
	// bukkit {
	//     loadAt STARTUP
	//     loadAt = POSTWORLD
	// }
	// -------------------------------------------------------------------------------------------------------------

	private LoadType loadAt;

	public LoadType getLoadAt() {
		return this.loadAt;
	}

	public void setLoadAt(LoadType type) {
		this.loadAt = type;
	}

	public void loadAt(LoadType type) {
		if (type == null) throw new InvalidUserDataException("Load type cannot be null.");
		if (this.loadAt != null)
			throw new InvalidUserDataException("Load type cannot be specified multiple times.");

		setLoadAt(type);
	}



	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: template
	// Optional.
	//
	// Specifies that this should be treated as a template.
	// This prevents verification of certain attributes and disables plugin.yml generation.
	//
	// If the main class is specified, this will be disabled.
	//
	// bukkit {
	//     ...
	//     template()
	// }
	// -------------------------------------------------------------------------------------------------------------

	private boolean template;

	public boolean getTemplate() {
		return this.template;
	}

	public void setTemplate(boolean template) {
		this.template = template;
	}

	public void template() {
		this.template = true;
	}

}
