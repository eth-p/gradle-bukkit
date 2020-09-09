# Gradle for Bukkit
A small, no-frills Gradle plugin that helps simplify Bukkit plugin development.

## Features
- Generate the plugin manifest using a `build.gradle` DSL.
- Easily include common API dependencies (and their repositories).

## Example
[We have an example repo that you can fork!](https://github.com/eth-p/bukkit-example)

If you would prefer to start from scratch, the following build.gradle file will get you started:

```groovy
plugins {
    id 'java'
    id 'dev.ethp.bukkit' version '0.2'
}

dependencies {
    bukkitApi() // or spigotApi()
    vaultApi()
}

version = '1.0'
sourceCompatibility = '1.8'
targetCompatibility = '1.8'

bukkit {
    name 'my-plugin'
    main 'com.example.BukkitPlugin'

    description 'A simple plugin.'

    author 'John'
    author 'Sally'

    api {
        version '1.15'
    }

    command 'ping', {
        permission 'my-plugin.ping'
        description 'Replies with "Pong!"'
        usage 'Usage: /ping'
    }
    
    permission 'my-plugin.ping', {
        description 'Gives access to /ping'
    }
    
    dependency 'Vault', OPTIONAL
    dependency 'WorldEdit', REQUIRED
}
```

For a working "hello world" plugin that uses Gradle for Bukkit, check out [https://github.com/eth-p/bukkit-example](https://github.com/eth-p/bukkit-example).


## Dependency Functions

Gradle for Bukkit is capable of automatically resolving common plugin APIs.
All you need to do is call the function inside the `build.gradle` dependency block. 

### APIs
The following APIs are supported:

|API|Function|
|:--|:--|
|Paper\*|`paperApi()`|
|Spigot\*|`spigotApi()`|
|Bukkit\*|`bukkitApi()`|
|[Vault](https://www.spigotmc.org/resources/vault.34315/)|`vaultApi('version')`|
|[PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)|`placeholderApi()`|

\* The API version used is determined by the API version specified in the `api` DSL block.

### Libraries
|Library|Function|
|[BKCommonLib](https://www.spigotmc.org/resources/bkcommonlib.39590/)|`libBkCommon()`|
|[ACF](https://www.spigotmc.org/threads/acf-beta-annotation-command-framework.234266/#post-2366730)|`libACF()`|


## Documentation

**DSL:**  
Documentation of Gradle for Bukkit DSL. 
Examples use the Groovy DSL, but the Kotlin DSL should also work.

- [Bukkit DSL](./doc/dsl/bukkit.md)
- [Bukkit API DSL](./doc/dsl/bukkit-api.md)
- [Bukkit Logger DSL](./doc/dsl/bukkit-logger.md)
- [Command DSL](./doc/dsl/command.md)
- [Dependency DSL](./doc/dsl/dependency.md)
- [Permission DSL](./doc/dsl/permission.md)

**Resources:**  
Other documentation that might be useful.

- [Shadow Transformer](./doc/shadow.md)


## Alternatives

- [BukkitGradle](https://github.com/EndlessCodeGroup/BukkitGradle)
