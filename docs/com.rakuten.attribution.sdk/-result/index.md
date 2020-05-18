[radattribution-sdk-android](../../index.md) / [com.rakuten.attribution.sdk](../index.md) / [Result](./index.md)

# Result

`sealed class Result<out T>`

Container to return result of asynchronous methods

### Parameters

`T` - type of result of asynchronous operation

### Types

| Name | Summary |
|---|---|
| [Error](-error/index.md) | `class Error : `[`Result`](./index.md)`<`[`Nothing`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)`>` |
| [Success](-success/index.md) | `class Success<T> : `[`Result`](./index.md)`<`[`T`](-success/index.md#T)`>` |

### Functions

| Name | Summary |
|---|---|
| [dealWithError](deal-with-error.md) | `fun dealWithError(onError: (`[`Error`](-error/index.md)`) -> `[`Nothing`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)`): `[`T`](index.md#T)<br>util method to handle errors |

### Inheritors

| Name | Summary |
|---|---|
| [Error](-error/index.md) | `class Error : `[`Result`](./index.md)`<`[`Nothing`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)`>` |
| [Success](-success/index.md) | `class Success<T> : `[`Result`](./index.md)`<`[`T`](-success/index.md#T)`>` |
