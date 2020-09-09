# Dependency Function DSL

Dependencies can be quickly added to a project using one of the available dependency functions listed on the main project page.

When added this way, a required dependency will automatically be added to the `plugin.yml`. If you need an optional dependency instead, you can manually specify the dependency in the `bukkit` DSL block.



## Function DSL

The simplest way to use a dependency function is to call it from within the `dependencies` block.
When used this way, an appropriate version of the dependency is selected for you. 

```groovy
dependencies {
  bukkitApi()
}
```

If you need to specify a specific version of the dependency, you can pass it as a parameter.

```groovy
dependencies {
  bukkitApi("1.16.2-R0.1-SNAPSHOT")
}
```



## Closure DSL

This is the quirky but more powerful form of the dependency function.
With closure dependency functions, you can set configurable properties for the dependency.

**If you do not set a property, the dependency will not be added to the project.**

```groovy
dependencies {
  libACF {
    version "0.5.0-SNAPSHOT"
    platform = "paper"
  }
}
```

