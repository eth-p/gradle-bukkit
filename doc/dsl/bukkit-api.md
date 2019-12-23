# Bukkit API DSL (`api`)

## Properties

|Property|Type|Description|
|:--|:--|:--|
|`manifestVersion`\*|String|The plugin manifest API version.|
|`libraryVersion`\*|String|The artifact version of the Bukkit/Spigot API library.|
|`databaseSupport`|Boolean|Whether or not the plugin requires access to the Bukkit database API.|

\* is a required property.



## Methods

<details>
<summary><strong><code>version '1.14.3'</code></strong></summary><div>

Sets both the library version and manifest version based on a Minecraft version string.

```groovy
api {
    version '1.14.4'
}
```
</div></details>
