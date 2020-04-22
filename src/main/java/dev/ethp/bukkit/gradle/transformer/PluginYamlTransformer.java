package dev.ethp.bukkit.gradle.transformer;

import com.github.jengelman.gradle.plugins.shadow.transformers.Transformer;
import com.github.jengelman.gradle.plugins.shadow.transformers.TransformerContext;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.StopActionException;
import shadow.org.apache.tools.zip.ZipEntry;
import shadow.org.apache.tools.zip.ZipOutputStream;
import org.gradle.api.file.DuplicatesStrategy;
import org.gradle.api.file.FileTreeElement;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

class PluginYamlTransformer implements Transformer {

	// -------------------------------------------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------------------------------------------

	private Map<String, Object> data;
	private Yaml yaml = new Yaml();

	public DuplicatesStrategy strategy = DuplicatesStrategy.FAIL;
	public String resource = "plugin.yml";


	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public PluginYamlTransformer() {
	}

	public PluginYamlTransformer(String resource) {
		this();
		this.resource = resource;
	}

	public PluginYamlTransformer(DuplicatesStrategy strategy) {
		this();
		this.strategy = strategy;
	}

	public PluginYamlTransformer(String resource, DuplicatesStrategy strategy) {
		this();
		this.resource = resource;
		this.strategy = strategy;
	}


	// -------------------------------------------------------------------------------------------------------------
	// Accessors
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * Gets the path of the resource to transform.
	 * This will be plugin.yml by default.
	 *
	 * @return The resource path.
	 */
	@Input
	public String getResource() {
		return this.resource;
	}

	/**
	 * Sets the path of the resource to transform.
	 *
	 * @param resource The resource path.
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}

	/**
	 * Gets the duplicates strategy for un-mergeable properties.
	 */
	public DuplicatesStrategy getDuplicatesStrategy() {
		return this.strategy;
	}

	/**
	 * Sets the duplicates strategy for un-mergeable properties.
	 *
	 * @param strategy The strategy to use.
	 */
	public void setDuplicatesStrategy(DuplicatesStrategy strategy) {
		this.strategy = strategy;
	}


	// -------------------------------------------------------------------------------------------------------------
	// Transformer
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public boolean canTransformResource(FileTreeElement element) {
		return element.getRelativePath().getPathString().equals("plugin.yml");
	}

	@Override
	public void transform(TransformerContext context) throws RuntimeException {
		Yaml yaml = new Yaml();
		Map<String, Object> parsed = yaml.load(context.getIs());

		try {
			context.getIs().close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		// The first one found is the default.
		if (this.data == null) {
			this.data = parsed;
			return;
		}

		// Merge the manifests.
		PluginYamlTransformer.merge(this.data, parsed, this.strategy);
	}

	@Override
	public boolean hasTransformedResource() {
		return this.data != null;
	}

	@Override
	public void modifyOutputStream(ZipOutputStream os, boolean preserveFileTimestamps) {
		if (this.data == null) {
			return;
		}

		// Write the entry.
		ZipEntry entry = new ZipEntry(resource);
		entry.setTime(TransformerContext.getEntryTimestamp(preserveFileTimestamps, entry.getTime()));

		try {
			os.putNextEntry(entry);

			OutputStreamWriter writer = new OutputStreamWriter(os);
			writer.write("# File generated using gradle-bukkit (https://github.com/eth-p/gradle-bukkit)\n");
			this.yaml.dump(this.data, writer);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}


	// -------------------------------------------------------------------------------------------------------------
	// Merge Utils
	// -------------------------------------------------------------------------------------------------------------

	interface MergeFn<T, R> {
		R merge(String key, T existingValue, T newValue, DuplicatesStrategy strategy);
	}

	static public void merge(Map<String, Object> into, Map<String, Object> data, DuplicatesStrategy strategy) {
		doMerge(PluginYamlTransformer::mergeUnmergable, "name", into, data, strategy);
		doMerge(PluginYamlTransformer::mergeUnmergable, "main", into, data, strategy);
		doMerge(PluginYamlTransformer::mergeUnmergable, "version", into, data, strategy);
		doMerge(PluginYamlTransformer::mergeUnmergable, "description", into, data, strategy);
		doMerge(PluginYamlTransformer::mergeUnmergable, "api-version", into, data, strategy);
		doMerge(PluginYamlTransformer::mergeUnmergable, "website", into, data, strategy);
		doMerge(PluginYamlTransformer::mergeUnmergable, "database", into, data, strategy);
		doMerge(PluginYamlTransformer::mergeUnmergable, "prefix", into, data, strategy);
		doMerge(PluginYamlTransformer::mergeUnmergable, "load", into, data, strategy);
		doMerge(PluginYamlTransformer::mergeSet, "depend", into, data, strategy);
		doMerge(PluginYamlTransformer::mergeSet, "softdepend", into, data, strategy);
		doMerge(PluginYamlTransformer::mergeSet, "loadbefore", into, data, strategy);
		doMerge(PluginYamlTransformer::mergeCommands, "commands", into, data, strategy);
		doMerge(PluginYamlTransformer::mergePermissions, "permissions", into, data, strategy);

		mergeAuthors(into, data, strategy);
	}

	/**
	 * A utility method that handles common merging logic.
	 *
	 * <ul>
	 * <li>If a value exists in neither, nothing happens.</li>
	 * <li>If a value exists in one but not the other, that is used.</li>
	 * </ul>
	 * <p>
	 * If a value exists in both maps, this will call the merge function.
	 *
	 * @param lambda   The merge function.
	 * @param key      The property to merge.
	 * @param into     The map to merge into.
	 * @param data     The map to merge from.
	 * @param strategy The merge strategy.
	 * @param <T>      The expected type of the map values.
	 */
	@SuppressWarnings("unchecked")
	static private <T> void doMerge(MergeFn<T, Object> lambda, String key, Map<String, Object> into, Map<String, Object> data, DuplicatesStrategy strategy) {
		Object intoValue = into.get(key);
		Object dataValue = data.get(key);

		if (intoValue == null && dataValue == null) return;
		if (intoValue == null) {
			into.put(key, dataValue);
			return;
		}

		if (!intoValue.equals(dataValue)) {
			into.put(key, lambda.merge(key, (T) intoValue, (T) dataValue, strategy));
		}
	}

	// -------------------------------------------------------------------------------------------------------------
	// Merge Property Functions
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * Merges the 'authors' property.
	 * This combines all the authors into a single list.
	 *
	 * @param into     The map to merge into.
	 * @param data     The map to merge from.
	 * @param strategy The merge strategy (ignored for this).
	 */
	@SuppressWarnings("unchecked")
	static private void mergeAuthors(Map<String, Object> into, Map<String, Object> data, DuplicatesStrategy strategy) {
		Set<String> authors = new HashSet<>();

		// Merge 'author' key.
		if (into.containsKey("author")) authors.add(into.get("author").toString());
		if (data.containsKey("author")) authors.add(data.get("author").toString());

		// Merge 'authors' key.
		if (into.containsKey("authors")) authors.addAll((List<String>) into.get("authors"));
		if (data.containsKey("authors")) authors.addAll((List<String>) data.get("authors"));

		// Write back.
		into.remove("author");
		into.remove("authors");

		if (authors.size() > 1) {
			into.put("authors", new ArrayList<>(authors));
		} else if (authors.size() == 1) {
			into.put("author", authors.iterator().next());
		}
	}

	/**
	 * Merges a set of strings.
	 * This combines all the strings into a single list.
	 *
	 * @param existing The existing values.
	 * @param merging  The new values.
	 * @param strategy The merge strategy (ignored for this).
	 * @return The merged set (technically, list).
	 */
	static private Object mergeSet(String key, List<String> existing, List<String> merging, DuplicatesStrategy strategy) {
		Set<String> merged = new HashSet<>();
		merged.addAll(existing);
		merged.addAll(merging);
		return new ArrayList<>(merged);
	}

	/**
	 * Merges objects that are expected to be unmergeable.
	 * This uses the duplicate strategy logic.
	 *
	 * @param existing The existing value.
	 * @param merging  The new value.
	 * @param strategy The merge strategy.
	 * @return The chosen value.
	 */
	static private Object mergeUnmergable(String key, Object existing, Object merging, DuplicatesStrategy strategy) {
		switch (strategy) {
			case EXCLUDE:
				return existing;

			case INCLUDE:
				return merging;

			case FAIL:
				throw new StopActionException("Cannot merge multiple '" + key + "' properties in plugin manifest.");

			default:
				throw new StopActionException("Unsupported merge strategy: " + strategy.name());
		}
	}

	/**
	 * Merges a map of commands.
	 * This uses the duplicate strategy logic.
	 *
	 * @param existing The existing commands.
	 * @param merging  The new commands.
	 * @param strategy The merge strategy.
	 * @return The merged map (technically, the original map).
	 */
	static private Object mergeCommands(String key, Map<String, Object> existing, Map<String, Object> merging, DuplicatesStrategy strategy) {
		for (String command : merging.keySet()) {
			doMerge(PluginYamlTransformer::mergeUnmergable, command, existing, merging, strategy);
		}

		return existing;
	}

	/**
	 * Merges a map of permissions.
	 * This uses the duplicate strategy logic.
	 *
	 * @param existing The existing permissions.
	 * @param merging  The new permissions.
	 * @param strategy The merge strategy.
	 * @return The merged map (technically, the original map).
	 */
	static private Object mergePermissions(String key, Map<String, Object> existing, Map<String, Object> merging, DuplicatesStrategy strategy) {
		for (String command : merging.keySet()) {
			doMerge(PluginYamlTransformer::mergeUnmergable, command, existing, merging, strategy);
		}

		return existing;
	}

}
