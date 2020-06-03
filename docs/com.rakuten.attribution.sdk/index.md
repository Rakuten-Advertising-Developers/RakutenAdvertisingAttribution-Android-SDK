[radattribution-sdk-android](../index.md) / [com.rakuten.attribution.sdk](./index.md)

## Package com.rakuten.attribution.sdk

Contains all classes visible for users.

### Types

| Name | Summary |
|---|---|
| [Configuration](-configuration/index.md) | `data class Configuration`<br>A type that provides an ability to configure SDK |
| [ContentItem](-content-item/index.md) | `data class ContentItem`<br>A class that represents purchased item info |
| [DeviceData](-device-data/index.md) | `data class DeviceData`<br>A class that represents info about user's device |
| [EventData](-event-data/index.md) | `data class EventData`<br>A class that represents details of event data |
| [EventSender](-event-sender/index.md) | `class EventSender`<br>A class that can send various events via SDK |
| [LinkResolver](-link-resolver/index.md) | `class LinkResolver`<br>A class that can resolve links via SDK |
| [RAdDeepLinkData](-r-ad-deep-link-data/index.md) | `data class RAdDeepLinkData`<br>A class that represents response on link resolving operation |
| [RAdSendEventData](-r-ad-send-event-data/index.md) | `data class RAdSendEventData`<br>A class that represents response on link event sending operation |
| [RakutenAdvertisingAttribution](-rakuten-advertising-attribution/index.md) | `object RakutenAdvertisingAttribution`<br>An object that encapsulates various features of RAdAttribution SDK, like sending events and links resolving |
| [Result](-result/index.md) | `sealed class Result<out T>`<br>Container to return result of asynchronous methods |
| [UserData](-user-data/index.md) | `data class UserData`<br>A class that represents user specific data |

### Type Aliases

| Name | Summary |
|---|---|
| [CustomData](-custom-data.md) | `typealias CustomData = `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>A dictionary to put custom data associated with event |
