[radattribution-sdk-android](./index.md)

## RADAttribution Android SDK

Rakuten advertising attribution SDK allows advertisers to track app installs and in-app conversion events using any affiliate link promoted within a publisher’s mobile app or on a mobile web page.

### Requires Android API level 21+

## Add the RADAttribution SDK into your project

## Create public/private key pairs

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

#### Setup RADAttribution SDK initialization

``` kotlin
// create Configuration class instance with the private key and unique application id 
val configuration = Configuration(
                appId = context.packageName,
                privateKey = your_private_key,
                isManualAppLaunch = false
        )
RAdAttribution.setup(context, configuration)
```

#### Handling INSTALL and OPEN events along with deeplink data

RAdAttribution SDK provides a function to track app install and open events by itself. It also provides an ability to handle  deep link data if any associated with the affiliate link promoted within a publisher’s mobile app or on a mobile web page.

Simply call the `RADAttribution.linkResolver.resolve()` method whenever Android app is brought to foreground.

To initialize  your Application call use:

``` kotlin
override fun onCreate() {
        super.onCreate()

        val secretKey = assets
                .open("private_key")
                .bufferedReader()
                .use { it.readText() }

        val configuration = Configuration(
                appId = BuildConfig.APPLICATION_ID,
                privateKey = secretKey,
                isManualAppLaunch = false
        )
        RAdAttribution.setup(this, configuration)
    }
```

### Packages

| Name | Summary |
|---|---|
| [com.rakuten.attribution.sdk](com.rakuten.attribution.sdk/index.md) |  |

### Index

[All Types](alltypes/index.md)