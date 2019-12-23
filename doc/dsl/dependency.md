# Dependency DSL (`dependency`)

## Properties

|Property|Type|Description|
|:--|:--|:--|
|`name`\*|String|The dependency (plugin) name.|
|`type`\*|`REQUIRED` \| `OPTIONAL`|The dependency type.|

\* is a required property.


## Enum Constants

|Constant|Description|
|:--|:--|
|`REQUIRED`|The dependency is required for the plugin to load.|
|`OPTIONAL`|The dependency enables additional plugin functionality.|


## Methods

<details>
<summary><strong><code>required</code></strong></summary><div>

Marks the dependency as required. This sets the type to `REQUIRED`.

```groovy
dependency {
    // ...
    required
}
```
</div></details>


<details>
<summary><strong><code>optional</code></strong></summary><div>

Marks the dependency as optional. This sets the type to `OPTIONAL`.

```groovy
dependency {
    // ...
    optional
}
```
</div></details>