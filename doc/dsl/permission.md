# Permission DSL (`permission`)

## Properties

|Property|Type|Description|
|:--|:--|:--|
|`name`\*|String|The permission name.|
|`value`\*|`ALL` \| `OPERATOR` \| `NOT_OPERATOR` \| `NONE`|The default permission value.|
|`description`|String|A short description of what this permission allows.|
|`children`|ChildPermission|The child permissions of the permission.|

\* is a required property.



## Enum Constants

**Groups:**

|Constant|Description|
|:--|:--|
|`ALL`|All players.|
|`NONE`|No players.|
|`OPERATOR`|Only players who have been given /op.|
|`NOT_OPERATOR`|Only players who have not been given /op.|

**Inheritance:**

|Constant|Description|
|:--|:--|
|`INHERIT`|Inherits the parent permission.|
|`INVERSE`|Inherits the inverse value of the parent permission.|



## Methods

<details>
<summary><strong><code>allow GROUP</code></strong></summary><div>

Allows a group of players the permission.

By default, all players are given a permission.
This is only really useful after explicitly setting the value to `NONE`. 

```groovy
dependency {
    // ...
    value = NONE
    allow NOT_OPERATOR
}
```
</div></details>


<details>
<summary><strong><code>deny GROUP</code></strong></summary><div>
Denies a group of players the permission.

By default, all players are given a permission.
This is good for excluding non-operator players. 

```groovy
dependency {
    // ...
    deny NOT_OPERATOR
}
```
</div></details>


<details>
<summary><strong><code>child 'permission', INHERITANCE</code></strong></summary><div>

Defines a child permission.

Child permissions can either have an inheritance of `INHERIT`, which copies the parent's value;
or an inheritance of `INVERT`, which inverts the parent's value.

```groovy
dependency {
    // ...
    child 'my.permission.child', INHERIT
}
```
</div></details>
