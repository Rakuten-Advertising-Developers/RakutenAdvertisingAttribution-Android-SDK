[radattribution-sdk-android](../../index.md) / [com.rakuten.attribution.sdk](../index.md) / [EventSender](index.md) / [sendEvent](./send-event.md)

# sendEvent

`fun sendEvent(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, eventData: `[`EventData`](../-event-data/index.md)`? = null, customData: `[`CustomData`](../-custom-data.md)` = emptyMap(), contentItems: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`ContentItem`](../-content-item/index.md)`> = emptyArray(), callback: (`[`Result`](../-result/index.md)`<`[`RAdSendEventData`](../-r-ad-send-event-data/index.md)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Sends event to server

### Parameters

`name` - event's name. i.e. "ADD_TO_CART"

`eventData` - meta data associated with event

`customData` - custom data associated with event

`contentItems` - content items associated with event

`callback` - lambda to be called with operation result`fun sendEvent(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, eventData: `[`EventData`](../-event-data/index.md)`? = null, userData: `[`UserData`](../-user-data/index.md)`, deviceData: `[`DeviceData`](../-device-data/index.md)`, customData: `[`CustomData`](../-custom-data.md)` = emptyMap(), contentItems: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`ContentItem`](../-content-item/index.md)`> = emptyArray(), callback: (`[`Result`](../-result/index.md)`<`[`RAdSendEventData`](../-r-ad-send-event-data/index.md)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)