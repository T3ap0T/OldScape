/*
GNU LGPL V3
Copyright (C) 2019 Bart van Helvert
B.A.J.v.Helvert@gmail.com

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software Foundation,
Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package io.guthix.cache.osrs.config

import io.github.bartvhelvert.jagex.fs.io.params
import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.writeParams
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

data class StructConfig(override val id: Int) : Config(id) {
    var params: HashMap<Int, Any>? = null

    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            params?.let {
                os.writeOpcode(249)
                os.writeParams(params!!)
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<StructConfig>() {
        override val id = 34

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): StructConfig {
            val structConfig = StructConfig(id)
            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    249 -> structConfig.params = buffer.params
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return structConfig
        }
    }
}