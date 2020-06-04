[radattribution-sdk-android](./index.md)

## RADAttribution Android SDK

Rakuten advertising attribution SDK allows advertisers to track app installs and in-app conversion events using any affiliate link promoted within a publisher’s mobile app or on a mobile web page.

### Requires Android API level 21+

### Create public/private key pairs

Our SDK internally uses a private key to sign a JSON Web Token(JWT).
This token is passed to our Attribution backend system to verify the SDK's identity.

Generate public/private key pairs with the following commands

``` sh
openssl genpkey -algorithm RSA -out rad_rsa_private.pem -pkeyopt rsa_keygen_bits:256
openssl rsa -in rad_rsa_private.pem -pubout -out rad_rsa_public.pem
```

This command will create the following two files.

1. rad_rsa_private.pem: Store this private key securely.
2. rad_rsa_public.pem: This file is required by Rakuten Attribution backend platform to verify the signature of the authentication JWT. 
(Public key handover process will be communicated separately)

#### Add RakutenAdvertisingAttribution SDK to project

To use RakutenAdvertisingAttribution SDK you need to add this string to 'dependencies' section in build.gradle file of your application module.

``` groovy
dependencies {
    //...
    implementation 'io.github.rakuten-advertising-developers:attribution-sdk:0.0.3'
}
```

and also you need to add mavenCentral to repositories list on you root build.gradle file

``` groovy
allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}
```

Now you can  sync your project and start working with RakutenAdvertisingAttribution SDK
}

#### Setup RakutenAdvertisingAttribution SDK initialization

To start working with RakutenAdvertisingAttribution SDK you need to create an instance of [Configuration](com.rakuten.attribution.sdk/-configuration/index.md) class. 
It's constructor takes three parameters:

* appId (unique android application id. You can get it from any Context class instance of your application)
* appVersion (your application version name, '1.0' for example)
* privateKey (content of your rad_rsa_private.pem file, with both header and footer removed)
* isManualAppLaunch (flag that indicates if application was opened from link with the associated domain)
* endpointUrl (url which sdk will send analytics to)

``` kotlin
 val configuration = Configuration(
                appId = BuildConfig.APPLICATION_ID,
                appVersion = BuildConfig.VERSION_NAME,
                privateKey = secretKey,
                isManualAppLaunch = false,
                endpointUrl = ENDPOINT_URL

        )
```

Then you need to initiate [RakutenAdvertisingAttribution](com.rakuten.attribution.sdk/-rakuten-advertising-attribution/index.md) object with you application's context class and created Configuration instance

``` kotlin
RakutenAdvertisingAttribution.setup(context, configuration)
```

#### Send events with metadata

Radattribution SDK provides a way to send events.

To send event to server you have to call sendEvent() method of EventSender sdk class. 
The only required parameter of this method is 'name'

``` kotlin
RakutenAdvertisingAttribution.eventSender.sendEvent(name = "PURCHASE")
```

Optionally you can pass an instance of [EventData](com.rakuten.attribution.sdk/-event-data/index.md) class, with event's metadata.

``` kotlin
val eventData = EventData(
             transactionId = "112233",
             searchQuery = "shoe products",
             currency = "USD",
             revenue = selectedProduct?.price,
             shipping = 0.0,
             tax = 0.8,
             coupon = "coupon_test_code",
             affiliation = "affiliation code",
             description = "description"
     ) 
```

and/or array of ContentItem instances. [ContentItem](com.rakuten.attribution.sdk/-content-item/index.md) class contains info related to purchase items, like price, tax, etc.

``` kotlin
val contentItems = arrayOf(
                ContentItem(
                        sku = "77889900",
                        productName = selectedProduct!!.name,
                        quantity = 12,
                        price = selectedProduct.price
                )
        )
```

Also you can pass data with [CustomData](com.rakuten.attribution.sdk/-custom-data.md) class, which is just alias for Map&lt;String, String&gt;

``` kotlin
val customData = mapOf(
                "custom_key1" to "value1",
                "custom_key2" to "value2"
        )
```

Optionally you can pass lambda to be called on operation result.

So your sendEvent() call might look like this

``` kotlin
  RakutenAdvertisingAttribution.eventSender.sendEvent(
                name = action,
                customData = customData,
                eventData = eventData,
                contentItems = contentItems
        ) { result ->
            when (result) {
                is Result.Success -> {
                }
                is Result.Error -> {
                }
            }
        }
```

#### Resolve application's deep links

Another feature of Radattribution SDK is to handle application's deep links and handle it's metadata.
Firstly you need to add an &lt;intent-filter&gt; section to you AndroidManifest.xml file. 
To let operation system know that your application is able open http ans https links in rakutenadvertising.app.link domain

``` xml
 <intent-filter android:label="@string/app_name">
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:host="rakutenadvertising.app.link"
                    android:scheme="http" />
                <data android:host="rakutenadvertising.app.link"
                    android:scheme="https" />
 </intent-filter> 
```

Now links like  "https://rakutenadvertising.app.link/..." and "http://rakutenadvertising.app.link/..." will be prompted to open with your application.

Also you can specify custom app scheme instead of "http" and "https". For example it's "demo_app" for our demo application. 
You can put your application scheme to your gradle.build in buildTypes section like this

``` groovy
 buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
        buildTypes.each {
            it.buildConfigField 'String', 'APP_SCHEME', "\"demo_app\""
            it.resValue 'string', 'APP_SCHEME', "\"demo_app\""
        }
    }
```

Then you can add another &lt;intent-filter&gt; to your AndroidManifest.xml file

``` xml
 <intent-filter android:label="@string/app_name">
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:host="open"
                    android:scheme="@string/APP_SCHEME" />
 </intent-filter>
```

Now links like "demo_app://open?link_click_id=..." will be opened with your application automatically.

Then you have to add handleIntent() method to your MainActivity class. 
As you can see it checks if applications is started with one of known schemes: "http", "https" or your custom scheme.

``` kotlin
private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            when (intent.scheme) {
                "http", "https", BuildConfig.APP_SCHEME -> resolveLink(intent.data.toString())
                null -> showError(intent.scheme)
            }
        }
    }
```

If it so we can resolve deep link with resolveLink() method. There is only one required parameter - link to resolve.
And optional lambda callback.

``` kotlin
private fun resolveLink(link: String) {
        RakutenAdvertisingAttribution.linkResolver.resolve(link) {
            when (it) {
                is Result.Success -> {
                }
                is Result.Error -> {
                }
            }
        }
    }
```

#### Demo app

We provide a sample app that demonstrate the use of the Rakuten Advertising attribution SDK. You can find the open source application at this Git Repository

* [RAd Advertiser Demo](https://github.com/Rakuten-Advertising-Developers/radadvertiser-demo-android)

#### Documentation

* [API References](https://rakuten-advertising-developers.github.io/RakutenAdvertisingAttribution-Android-SDK/)

### Packages

| Name | Summary |
|---|---|
| [com.rakuten.attribution.sdk](com.rakuten.attribution.sdk/index.md) | Contains all classes visible for users. |

### Index

[All Types](alltypes/index.md)