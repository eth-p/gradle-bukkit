# Gradle for Bukkit
A Gradle plugin that helps simplify Bukkit plugin development.

## Features
- Generate the plugin manifest using a `build.gradle` DSL.
- Easily include common API dependencies.

## Example
```groovy
plugins {
    id 'java'
    id 'dev.ethp.bukkit' version '0.1.6'
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
The following APIs are supported:

|API|Function|
|:--|:--|
|Paper\*|`paperApi()`|
|Spigot\*|`spigotApi()`|
|Bukkit\*|`bukkitApi()`|
|Vault|`vaultApi('version')`|

\* The API version used is determined by the API version specified in the `api` DSL block.


## Gradle DSL

- [Bukkit DSL](./doc/dsl/bukkit.md)
- [Bukkit API DSL](./doc/dsl/bukkit-api.md)
- [Bukkit Logger DSL](./doc/dsl/bukkit-logger.md)
- [Command DSL](./doc/dsl/command.md)
- [Dependency DSL](./doc/dsl/dependency.md)
- [Permission DSL](./doc/dsl/permission.md)
