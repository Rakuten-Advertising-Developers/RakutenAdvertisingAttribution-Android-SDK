[radattribution-sdk-android](../../index.md) / [com.rakuten.attribution.sdk](../index.md) / [RakutenAdvertisingAttribution](./index.md)

# RakutenAdvertisingAttribution

`object RakutenAdvertisingAttribution`

An object that encapsulates various features of RAdAttribution SDK,
like sending events and links resolving

### Functions

| Name | Summary |
|---|---|
| [resolve](resolve.md) | `fun resolve(link: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, callback: ((`[`Result`](../-result/index.md)`<`[`RAdDeepLinkData`](../-r-ad-deep-link-data/index.md)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [sendEvent](send-event.md) | `fun sendEvent(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, eventData: `[`EventData`](../-event-data/index.md)`? = null, customData: `[`CustomData`](../-custom-data.md)` = emptyMap(), contentItems: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`ContentItem`](../-content-item/index.md)`> = emptyArray(), callback: ((`[`Result`](../-result/index.md)`<`[`RAdSendEventData`](../-r-ad-send-event-data/index.md)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Sends event to server |
| [setup](setup.md) | `fun setup(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, configuration: `[`Configuration`](../-configuration/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Setups RAdAttribution SDK. Should be called before SDK usage. |
