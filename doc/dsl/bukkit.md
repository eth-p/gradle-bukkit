# Bukkit DSL (`bukkit`)

## Properties

|Property|Type|Description|
|:--|:--|:--|
|`api`*|[Bukkit API](./bukkit-api.md)|The Bukkit API information.|
|`mainClass`*|String|The main class of the plugin.|
|`name`*|String|The name of the plugin|
|`version`*|String|The plugin version.|
|`authors`|String\[\]|The plugin author(s).|
|`commands`|[Command](./command.md)\[\]|The commands registered by the plugin.|
|`deferredPlugins`|String\[\]|A list of plugins that should be loaded after this plugin. (i.e. the `loadbefore` manifest value)|
|`dependencies`|[Dependency](./dependency.md)\[\]|The dependencies of the plugin.|
|`description`|String|A short human-friendly description of the plugin.|
|`loadAt`|`STARTUP` \| `POSTWORLD`|When the server should load the plugin.|
|`logger`|[Logger](./logger.md)|The logger information.|
|`permissions`|[Permission](./permission.md)\[\]|The permissions registered by the plugin.|
|`website`|String|The plugin or plugin author's website.|

\* is a required property.

**Meta-Properties:**  
These properties only apply to Gradle for Bukkit.

|Property|Type|Description|
|:--|:--|:--|
|`warnings`|Boolean|Allows disabling of manifest verification errors.|
|`generateManifest`|Boolean|Allows disabling of `plugin.yml` generation.|


## Methods

<details>
<summary><strong><code>author 'str'</code></strong></summary><div>

Adds an author to the current list of authors.

```groovy
bukkit {
    // ...
    author 'Jane' // authors = ['Jane']
    author 'Jim'  // authors = ['Jane', 'Jim']
}
```
</div></details>


<details>
<summary><strong><code>command { /* ... */}</code></strong></summary><div>

Defines a command registered by the plugin using the [Command DSL](./command.md).
The command name must be specified inside the block.

```groovy
bukkit {
    // ...
    command {
        name 'ping'
        // ...
    }
}
```
</div></details>


<details>
<summary><strong><code>command 'name', { /* ... */}</code></strong></summary><div>

Defines a command registered by the plugin using the [Command DSL](./command.md).

```groovy
bukkit {
    // ...
    command 'ping', {
        // ...
    }
}
```
</div></details>


<details>
<summary><strong><code>dependency 'plugin'</code></strong></summary><div>

Specifies a required dependency.

```groovy
bukkit {
    // ...
    dependency 'other-plugin', OPTIONAL
}
```
</div></details>


<details>
<summary><strong><code>dependency 'plugin', TYPE</code></strong></summary><div>

Specifies a dependency. Types can be `OPTIONAL` or `REQUIRED`.

```groovy
bukkit {
    // ...
    dependency 'other-plugin', OPTIONAL
}
```
</div></details>


<details>
<summary><strong><code>dependency { /* ... */ }</code></strong></summary><div>

Specifies a required dependency using the [Dependency DSL](./dependency.md).
The dependency name and type must be specified inside the block.

```groovy
bukkit {
    // ...
    dependency {
        name 'other-plugin'
        required
    }
}
```
</div></details>


<details>
<summary><strong><code>main 'str'</code></strong></summary><div>

This is shorthand for `mainClass 'str'`. 

```groovy
bukkit {
    // ...
    main 'com.example.MyPlugin' // mainClass = 'com.example.MyPlugin'
}
```
</div></details>


<details>
<summary><strong><code>permission { /* ... */}</code></strong></summary><div>

Defines a permission node to be registered by the plugin using the [Permission DSL](./permission.md).
The permission name must be set inside the block.

```groovy
bukkit {
    // ...
    permission {
        name 'my.permission'
        // ...
    }
}
```
</div></details>


<details>
<summary><strong>Method: <code>permission 'my.permission', { /* ... */}</code></strong></summary><div>

Defines a permission node to be registered by the plugin using the [Permission DSL](./permission.md).

```groovy
bukkit {
    // ...
    permission 'my.permission', {
        // ...
    }
}
```
</div></details>
