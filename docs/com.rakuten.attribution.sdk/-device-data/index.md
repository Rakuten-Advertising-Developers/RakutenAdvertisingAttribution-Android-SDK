[radattribution-sdk-android](../../index.md) / [com.rakuten.attribution.sdk](../index.md) / [DeviceData](./index.md)

# DeviceData

`data class DeviceData`

A class that represents info about user's device

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DeviceData(os: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, osVersion: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, model: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, screenWidth: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, screenHeight: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, isSimulator: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`)`<br>A class that represents info about user's device |

### Properties

| Name | Summary |
|---|---|
| [deviceId](device-id.md) | `val deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [isSimulator](is-simulator.md) | `val isSimulator: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [model](model.md) | `val model: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [os](os.md) | `val os: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [osVersion](os-version.md) | `val osVersion: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [screenHeight](screen-height.md) | `val screenHeight: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [screenWidth](screen-width.md) | `val screenWidth: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Companion Object Functions

| Name | Summary |
|---|---|
| [create](create.md) | `fun create(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`DeviceData`](./index.md)<br>Creates DeviceData class from Android's Context instance |
