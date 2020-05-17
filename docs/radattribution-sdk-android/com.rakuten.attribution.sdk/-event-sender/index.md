[radattribution-sdk-android](../../index.md) / [com.rakuten.attribution.sdk](../index.md) / [EventSender](./index.md)

# EventSender

`class EventSender`

A class that can send various events via SDK

### Functions

| Name | Summary |
|---|---|
| [sendEvent](send-event.md) | `fun sendEvent(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, eventData: `[`EventData`](../-event-data/index.md)`? = null, customData: `[`CustomData`](../-custom-data.md)` = emptyMap(), contentItems: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`ContentItem`](../-content-item/index.md)`> = emptyArray(), callback: (`[`Result`](../-result/index.md)`<`[`RAdSendEventData`](../-r-ad-send-event-data/index.md)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Sends event to server`fun sendEvent(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, eventData: `[`EventData`](../-event-data/index.md)`? = null, userData: `[`UserData`](../-user-data/index.md)`, deviceData: `[`DeviceData`](../-device-data/index.md)`, customData: `[`CustomData`](../-custom-data.md)` = emptyMap(), contentItems: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`ContentItem`](../-content-item/index.md)`> = emptyArray(), callback: (`[`Result`](../-result/index.md)`<`[`RAdSendEventData`](../-r-ad-send-event-data/index.md)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [tag](tag.md) | `val tag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
