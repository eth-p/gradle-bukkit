package dev.ethp.bukkit.gradle.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.ethp.bukkit.gradle.extension.*;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.yaml.snakeyaml.Yaml;

/**
 * A task that generates Bukkit plugin manifest files.
 */
public class GenerateBukkitManifest extends AbstractTask {

	public static String NAME = "generateBukkitManifest";

	// -------------------------------------------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------------------------------------------

	public void put(Map<String, Object> map, String key, Optional<?> value) {
		if (!value.isPresent()) return;

		Object unwrapped = value.get();
		if (unwrapped instanceof Set<?>) {
			unwrapped = new LinkedList<Object>((Set<?>) unwrapped);
		}

		map.put(key, unwrapped);
	}

	protected void addManifest(Map<String, Object> map, BukkitExtension extension) {
		put(map, "name", Optional.of(extension.getName()));
		put(map, "version", Optional.of(extension.getVersion()));
		put(map, "main", Optional.of(extension.getMainClass()));
		put(map, "description", Optional.ofNullable(extension.getDescription()));
		put(map, "website", Optional.ofNullable(extension.getWebsite()));
		put(map, "load", Optional.ofNullable(extension.getLoadAt()).map(LoadType::getString));
		put(map, "prefix", Optional.ofNullable(extension.getLogger()).map(LoggerExtension::getPrefix));
		put(map, "database", Optional.ofNullable(extension.getApi().getDatabaseSupport()));
		put(map, "loadbefore", Optional.ofNullable(extension.getDeferredPlugins()));
	}

	protected void addDependencies(Map<String, Object> map, BukkitExtension extension) {
		if (extension.getDependencies() == null) return;

		List<String> soft = extension.getDependencies().stream()
				.filter(dep -> dep.getType() == DependencyType.OPTIONAL)
				.map(DependencyExtension::getName)
				.collect(Collectors.toList());

		List<String> hard = extension.getDependencies().stream()
				.filter(dep -> dep.getType() == DependencyType.REQUIRED)
				.map(DependencyExtension::getName)
				.collect(Collectors.toList());

		if (soft.size() > 0) put(map, "softdepend", Optional.of(soft));
		if (hard.size() > 0) put(map, "depend", Optional.of(hard));
	}

	protected void addAuthors(Map<String, Object> map, BukkitExtension extension) {
		if (extension.getAuthors() == null || extension.getAuthors().isEmpty()) return;
		if (extension.getAuthors().size() == 1) {
			put(map, "author", Optional.of(extension.getAuthors().iterator().next()));
		} else {
			put(map, "authors", Optional.of(extension.getAuthors()));
		}
	}

	protected void addCommands(Map<String, Object> parent, BukkitExtension extension) {
		if (extension.getCommands() == null || extension.getCommands().isEmpty()) return;

		HashMap<String, Map<String, Object>> map = new HashMap<>();
		parent.put("commands", map);

		extension.getCommands().forEach(command -> this.addCommand(map, command));
	}

	protected void addCommand(Map<String, Map<String, Object>> parent, CommandExtension command) {
		if (command.getName() == null) return;

		HashMap<String, Object> map = new HashMap<>();
		put(map, "description", Optional.ofNullable(command.getDescription()));
		put(map, "permission", Optional.ofNullable(command.getPermission()));
		put(map, "permission-message", Optional.ofNullable(command.getPermissionMessage()));
		put(map, "usage", Optional.ofNullable(command.getUsageMessage()));
		put(map, "aliases", Optional.ofNullable(command.getAliases())
				.map(Collection::stream)
				.map(stream -> stream.collect(Collectors.toList()))
		);

		parent.put(command.getName(), map);
	}

	protected void addPermissions(Map<String, Object> parent, BukkitExtension extension) {
		if (extension.getPermissions() == null || extension.getPermissions().isEmpty()) return;

		HashMap<String, Map<String, Object>> map = new HashMap<>();
		parent.put("permissions", map);

		extension.getPermissions().forEach(permission -> this.addPermission(map, permission));
	}

	protected void addPermission(Map<String, Map<String, Object>> parent, PermissionExtension permission) {
		if (permission.getName() == null) return;

		HashMap<String, Object> map = new HashMap<>();
		put(map, "description", Optional.ofNullable(permission.getDescription()));
		put(map, "default", Optional.ofNullable(permission.getValue()).map(PermissionValue::getString));

		if (permission.getChildren() != null && !permission.getChildren().isEmpty()) {
			HashMap<String, Object> children = new HashMap<>();
			map.put("children", children);

			for (ChildPermissionExtension child : permission.getChildren()) {
				String inheritance = child.getInheritance() == null ? "true" : child.getInheritance().getString();
				children.put(child.getName(), Boolean.parseBoolean(inheritance));
			}
		}

		parent.put(permission.getName(), map);
	}

	protected void save(String file, String data) {
		// Find an appropriate source set for generated data.
		SourceSetContainer sourceSets = (SourceSetContainer) this.getProject().getProperties().get("sourceSets");
		SourceSet targetSet = null;
		for (SourceSet sourceSet : sourceSets) {
			if (targetSet == null) targetSet = sourceSet;
			if (sourceSet.getName().equals("main")) targetSet = sourceSet;
			if (sourceSet.getName().equals("generated")) {
				targetSet = sourceSet;
				break;
			}
		}

		// Save the file.
		try {
			File targetFile = new File(targetSet.getOutput().getResourcesDir(), file);
			targetFile.getParentFile().mkdirs();
			BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
			writer.write(data);
			writer.close();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	// -------------------------------------------------------------------------------------------------------------
	// Override
	// -------------------------------------------------------------------------------------------------------------

	@Override
	@Internal
	public String getGroup() {
		return "Build";
	}

	@Override
	@Internal
	public String getDescription() {
		return "Generates a Bukkit plugin.yml";
	}

	@Override
	void exec() {
		HashMap<String, Object> root = new HashMap<>();

		addManifest(root, extension);
		addAuthors(root, extension);
		addDependencies(root, extension);

		addCommands(root, extension);
		addPermissions(root, extension);

		// Serialize.
		Yaml serializer = new Yaml();
		StringBuilder serialized = new StringBuilder();

		serialized.append("# File generated using gradle-bukkit (https://github.com/eth-p/gradle-bukkit)\n");
		serialized.append(serializer.dump(root));

		// Write to resources.
		save("plugin.yml", serialized.toString());
	}


}