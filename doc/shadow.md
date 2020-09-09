# Gradle Shadow Support
If you have a modular Bukkit plugin built from independent submodules, Gradle for Bukkit includes a shadow transformer that combines multiple `plugin.yml` files together.

```groovy
shadowJar {
	transform(new dev.ethp.bukkit.gradle.transformer.PluginYamlTransformer())
}
```
