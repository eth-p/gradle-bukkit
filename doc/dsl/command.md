# Command DSL (`command`)

## Properties

|Property|Type|Description|
|:--|:--|:--|
|`name`\*|String|The name of the command.|
|`aliases`|String\[\]|A list of alternate command names a user may use instead.|
|`description`|String|A short description of what the command does.|
|`permission`|String|The most basic permission node required to use the command.|
|`permissionMessage`|String|A message to show the user when they don't have permission to use the command.|
|`usageMessage`|String|A short message explaining how to use the command.|

\* is a required property.



## Methods

<details>
<summary><strong><code>alias 'myalias'</code></strong></summary><div>
Adds an alias to the current list of aliases.

```groovy
command {
    // ...
    alias 'tpaccept' // aliases = ['tpaccept']
    alias 'tpyes'    // aliases = ['tpaccept', 'tpyes']
}
```
</div></details>


<details>
<summary><strong><code>usage 'str'</code></strong></summary><div>

This is shorthand for `usageMessage 'str'`. 

```groovy
command {
    // ...
    usage 'Usage: do nothing' // usageMessage = 'Usage: do nothing'
}
```
</div></details>