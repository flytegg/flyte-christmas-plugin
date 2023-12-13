package gg.flyte.event.util

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.nio.charset.Charset


class StoredString : PersistentDataType<ByteArray, String> {
    private val charset = Charset.defaultCharset()
    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<String> {
        return String::class.java
    }

    override fun toPrimitive(var1: String, var2: PersistentDataAdapterContext): ByteArray {
        return var1.toByteArray(charset)
    }

    override fun fromPrimitive(var1: ByteArray, var2: PersistentDataAdapterContext): String {
        return String(var1, charset)
    }
}