# Dependency: Annotation Command Framework

[Annotation Command Framework (ACF)](https://www.spigotmc.org/threads/acf-beta-annotation-command-framework.234266/#post-2366730) is an annotation framework that significantly helps reduce the boilerplate and verbosity of creating commands in Bukkit Plugins. 



**Usage:**

```groovy
dependencies {
  libBkCommon()
}
```

**Properties:**

| Property   | Type    | Description                                                  |
| :--------- | :------ | :----------------------------------------------------------- |
| `version`  | String  | The dependency version.<br />By default, this is `0.5.0-SNAPSHOT`. |
| `relocate` | Boolean | Whether or not to relocate and shadow the dependency.<br />This is enabled by default. |

**Note:**

The ACF author highly recommends projects that use this framework relocate the framework classes to inside the plugin jar itself. Gradle for Bukkit will do this by default, but it requires the [shadow](https://github.com/johnrengelman/shadow) plugin to do it.

