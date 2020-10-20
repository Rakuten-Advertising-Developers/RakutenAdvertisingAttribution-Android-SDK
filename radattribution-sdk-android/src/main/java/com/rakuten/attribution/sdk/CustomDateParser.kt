package com.rakuten.attribution.sdk

import com.squareup.moshi.*
import java.util.*

class CustomDateAdapter : JsonAdapter<Date>() {

    @FromJson
    override fun fromJson(reader: JsonReader): Date? {
        return try {
            val unixTime = reader.nextLong()
            Date(unixTime)
        } catch (e: Exception) {
            null
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Date?) {
        if (value != null) {
            writer.value(value.time)
        }
    }
}