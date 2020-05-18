[radattribution-sdk-android](../../index.md) / [com.rakuten.attribution.sdk](../index.md) / [Configuration](./index.md)

# Configuration

`data class Configuration`

A type that provides an ability to configure SDK

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Configuration(appId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, privateKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, isManualAppLaunch: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`)`<br>A type that provides an ability to configure SDK |

### Properties

| Name | Summary |
|---|---|
| [appId](app-id.md) | `val appId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>android application id |
| [isManualAppLaunch](is-manual-app-launch.md) | `val isManualAppLaunch: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>if application opened from link with the associated domain - `false`, otherwise `true` |
| [privateKey](private-key.md) | `val privateKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>RS256 private to sign requests |
