# Dependency: Bukkit API

The heart of every Bukkit plugin is the Bukkit API.

Currently, there are 3 different versions of the API that may be available depending on what server software is used:

- [Bukkit](https://bukkit.org/) (via `bukkitApi`)
- [Spigot](https://www.spigotmc.org/) (via `spigotApi`)
- [Paper](https://papermc.io/) (via `paperApi`)

As a general rule of thumb, unless you use features only available in Paper or Spigot, you want to use the Bukkit API.



## Bukkit API

This is the most widely available API, and should be available on any version of CraftBukkit, Spigot, or Paper.

**Usage:**

```groovy
dependencies {
  bukkitApi()
}
```

**Properties:**

| Property  | Type   | Description                                                  |
| :-------- | :----- | :----------------------------------------------------------- |
| `version` | String | The dependency version.<br />By default, this is determined from the `api.version` manifest value. |



## Spigot API

The Spigot API is a superset of the Bukkit API with more features and listeners.
*Everything available in the Bukkit API is available in the Spigot API.*

**Usage:**

```groovy
dependencies {
  spigotApi()
}
```

**Properties:**

| Property  | Type   | Description                                                  |
| :-------- | :----- | :----------------------------------------------------------- |
| `version` | String | The dependency version.<br />By default, this is determined from the `api.version` manifest value. |



## Paper API

The PaperMC API is a superset of the Spigot API with even more features and listeners.
*Everything available in the Bukkit API and Spigot API is available in the PaperMC API.*

**Usage:**

```groovy
dependencies {
  paperApi()
}
```

**Properties:**

| Property  | Type   | Description                                                  |
| :-------- | :----- | :----------------------------------------------------------- |
| `version` | String | The dependency version.<br />By default, this is determined from the `api.version` manifest value. |

