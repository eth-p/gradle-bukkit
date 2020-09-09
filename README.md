# Gradle for Bukkit
A small, no-frills Gradle plugin that helps simplify Bukkit plugin development.

## Features
- Generate the plugin manifest using a `build.gradle` DSL.
- Easily include common API dependencies (and their repositories).

## Example
For a working "hello world" plugin that uses Gradle for Bukkit, check out [https://github.com/eth-p/bukkit-example](https://github.com/eth-p/bukkit-example).

If you would prefer to start from scratch however, the following build.gradle file will get you started:

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


## Dependency Functions

Gradle for Bukkit is capable of automatically resolving common plugin APIs.
All you need to do is call the function inside the `build.gradle` dependency block. 

### APIs
The following APIs are supported:

|API|Function|
|:--|:--|
|[Bukkit](./doc/dependency/bukkit-api.md#bukkit-api)\*|`bukkitApi()`|
|[Spigot](./doc/dependency/bukkit-api.md#spigot-api)\*|`spigotApi()`|
|[Paper](./doc/dependency/bukkit-api.md#paper-api)\*|`paperApi()`|
|[Vault](./doc/dependency/vault.md)|`vaultApi()`|
|[PlaceholderAPI](./doc/dependency/placeholderapi.md)|`placeholderApi()`|

\* The API version used is determined by the API version specified in the `api` DSL block.

### Libraries
|Library|Function|
|:--|:--|
|[BKCommonLib](./doc/dependency/bkcommonlib.md)|`libBkCommon()`|
|[ACF](./doc/dependency/acf.md)|`libACF()`|


## Documentation

**DSL:**  
Documentation of Gradle for Bukkit DSL. 
Examples use the Groovy DSL, but the Kotlin DSL should also work.

- [Bukkit DSL](./doc/dsl/bukkit.md)
- [Bukkit API DSL](./doc/dsl/bukkit-api.md)
- [Bukkit Logger DSL](./doc/dsl/bukkit-logger.md)
- [Command DSL](./doc/dsl/command.md)
- [Dependency DSL](./doc/dsl/dependency.md)
- [Dependency Function DSL](./doc/dsl/dependency-function.md)
- [Permission DSL](./doc/dsl/permission.md)

**Resources:**  
Other documentation that might be useful.

- [Shadow Transformer](./doc/shadow.md)

## Alternatives

Is there something that you need which Gradle for Bukkit isn't able to provide?
Try one of these alternatives! 

- [BukkitGradle](https://github.com/EndlessCodeGroup/BukkitGradle)

