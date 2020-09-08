package org.fouryouandme.researchkit.utils

import java.io.*

object ObjectUtils {

    /**
     * @param copyObject the Object to copy, which must implement interface Serializable
     * and all classes, subclasses, and member field classes must
     * implement a default package-level constructor, or exception will be thrown
     * @return deep object copy
     */

    @Suppress("UNCHECKED_CAST")
    fun <T : Serializable> clone(copyObject: T): T? {

        try {

            val baos = ByteArrayOutputStream(4096)
            val oos = ObjectOutputStream(baos)
            oos.writeObject(copyObject)

            val bais = ByteArrayInputStream(baos.toByteArray())
            val ois = ObjectInputStream(bais)
            return ois.readObject() as? T

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        return null
    }
}