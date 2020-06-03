[radattribution-sdk-android](../../index.md) / [com.rakuten.attribution.sdk](../index.md) / [RakutenAdvertisingAttribution](./index.md)

# RakutenAdvertisingAttribution

`object RakutenAdvertisingAttribution`

An object that encapsulates various features of RAdAttribution SDK,
like sending events and links resolving

### Properties

| Name | Summary |
|---|---|
| [eventSender](event-sender.md) | `val eventSender: `[`EventSender`](../-event-sender/index.md)<br>instance of EventSender class with the ability to send events |
| [linkResolver](link-resolver.md) | `val linkResolver: `[`LinkResolver`](../-link-resolver/index.md)<br>instance of LinkResolver class with the ability to resolve links |

### Functions

| Name | Summary |
|---|---|
| [setup](setup.md) | `fun setup(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, configuration: `[`Configuration`](../-configuration/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Setups RAdAttribution SDK. Should be called before SDK usage. |
