package com.amsavarthan.tally.data.utils

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.amsavarthan.tally.AppPreference
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class AppPreferenceSerializer @Inject constructor() : Serializer<AppPreference> {
    override val defaultValue: AppPreference
        get() = AppPreference.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppPreference {
        try {
            return AppPreference.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AppPreference, output: OutputStream) {
        t.writeTo(output)
    }
}