package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.*
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

data class NpcConfig @ExperimentalUnsignedTypes constructor(
    override val id: Int,
    val name: String,
    val size: UByte,
    val combatLevel: UShort?,
    val varpId: UShort?,
    val varp32Id: UShort?,
    val isInteractable: Boolean,
    val drawMapDot: Boolean,
    val isClickable: Boolean,
    val rotation: UShort,
    val headIcon: UShort?,
    val options: Array<String?>,
    val stanceAnimation: UShort?,
    val walkAnimation: UShort?,
    val rotate90RightAnimation: UShort?,
    val rotate90LeftAnimation: UShort?,
    val rotate180Animation: UShort?,
    val anInt2165: UShort?,
    val anInt2189: UShort?,
    val colorReplace: UShortArray?,
    val colorFind: UShortArray?,
    val textureReplace: UShortArray?,
    val textureFind: UShortArray?,
    val models: UShortArray?,
    val models2: UShortArray?,
    val resizeX: UShort,
    val resizeY: UShort,
    val contrast: Int,
    val ambient: Byte,
    val hasRenderPriority: Boolean,
    val configs: Array<UShort?>?,
    val aBool2190: Boolean,
    val params: HashMap<Int, Any>?
) : Config(id) {
    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            models?.let {
                os.writeOpcode(1)
                os.writeByte(models.size)
                models.forEach {
                    os.writeShort(it.toInt())
                }
            }
            if(name != "null") {
                os.writeOpcode(2)
                os.writeString(name)
            }
            if(size.toInt() != 1) {
                os.writeOpcode(12)
                os.writeByte(size.toInt())
            }
            stanceAnimation?.let {
                os.writeOpcode(13)
                os.writeShort(stanceAnimation.toInt())
            }
            if(walkAnimation != null && rotate180Animation == null && rotate90LeftAnimation == null
                && rotate90RightAnimation == null
            ) {
                os.writeOpcode(14)
                os.writeShort(walkAnimation.toInt())
            }
            anInt2165?.let {
                os.writeOpcode(15)
                os.writeShort(anInt2165.toInt())
            }
            anInt2189?.let {
                os.writeOpcode(16)
                os.writeShort(anInt2189.toInt())
            }
            if(walkAnimation != null && rotate180Animation != null && rotate90LeftAnimation != null
                && rotate90RightAnimation != null
            ) {
                os.writeOpcode(17)
                os.writeShort(walkAnimation.toInt())
                os.writeShort(rotate180Animation.toInt())
                os.writeShort(rotate90RightAnimation.toInt())
                os.writeShort(rotate90LeftAnimation.toInt())
            }
            options.forEachIndexed { i, str ->
                if(str != null && str != "Hidden") {
                    os.writeOpcode(30 + i)
                    os.writeString(str)
                }
            }
            if (colorFind != null && colorReplace != null) {
                os.writeOpcode(40)
                os.writeByte(colorFind.size)
                for (i in 0 until colorFind.size) {
                    os.writeShort(colorFind[i].toInt())
                    os.writeShort(colorReplace[i].toInt())
                }
            }
            if (textureFind != null && textureReplace != null) {
                os.writeOpcode(41)
                os.writeByte(textureFind.size)
                for (i in 0 until textureReplace.size) {
                    os.writeShort(textureFind[i].toInt())
                    os.writeShort(textureReplace[i].toInt())
                }
            }
            models2?.let {
                os.writeOpcode(60)
                os.writeByte(models2.size)
                models2.forEach { id ->
                    os.writeShort(id.toInt())
                }
            }
            if(!drawMapDot) os.writeOpcode(93)
            combatLevel?.let {
                os.writeOpcode(95)
                os.writeShort(combatLevel.toInt())
            }
            if(resizeX.toInt() != 128) {
                os.writeOpcode(97)
                os.writeShort(resizeX.toInt())
            }
            if(resizeY.toInt() != 128) {
                os.writeOpcode(98)
                os.writeShort(resizeY.toInt())
            }
            if(hasRenderPriority) os.writeOpcode(99)
            if(ambient.toInt() != 0) {
                os.writeOpcode(100)
                os.writeByte(ambient.toInt())
            }
            if(contrast != 0) {
                os.writeOpcode(101)
                os.writeByte(contrast / 5)
            }
            headIcon?.let {
                os.writeOpcode(102)
                os.writeShort(headIcon.toInt())
            }
            if(rotation.toInt() != 32) {
                os.writeOpcode(103)
                os.writeShort(103)
            }
            if(configs != null) {
                if(configs.last() != null) os.writeOpcode(118) else os.writeOpcode(106)
                if(varpId == null) os.writeShort(UShort.MAX_VALUE.toInt()) else os.writeShort(varpId.toInt())
                if(varp32Id == null) os.writeShort(UShort.MAX_VALUE.toInt()) else os.writeShort(varp32Id.toInt())
                if(configs.last() != null) os.writeShort(configs.last()!!.toInt())
                os.writeByte(configs.size - 2)
                for(i in 0 until configs.size - 1) {
                    if(configs[i] != null) {
                        os.writeShort(UShort.MAX_VALUE.toInt())
                    } else {
                        os.writeShort(configs[i]!!.toInt())
                    }
                }
            }
            if(!isInteractable) os.writeOpcode(107)
            if(!isClickable) os.writeOpcode(109)
            if(aBool2190) os.writeOpcode(111)
            params?.let {
                os.writeOpcode(249)
                os.writeParams(params)
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    @ExperimentalUnsignedTypes
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NpcConfig) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (size != other.size) return false
        if (combatLevel != other.combatLevel) return false
        if (varpId != other.varpId) return false
        if (varp32Id != other.varp32Id) return false
        if (isInteractable != other.isInteractable) return false
        if (drawMapDot != other.drawMapDot) return false
        if (isClickable != other.isClickable) return false
        if (rotation != other.rotation) return false
        if (headIcon != other.headIcon) return false
        if (!options.contentEquals(other.options)) return false
        if (stanceAnimation != other.stanceAnimation) return false
        if (walkAnimation != other.walkAnimation) return false
        if (rotate90RightAnimation != other.rotate90RightAnimation) return false
        if (rotate90LeftAnimation != other.rotate90LeftAnimation) return false
        if (rotate180Animation != other.rotate180Animation) return false
        if (anInt2165 != other.anInt2165) return false
        if (anInt2189 != other.anInt2189) return false
        if (colorReplace != other.colorReplace) return false
        if (colorFind != other.colorFind) return false
        if (textureReplace != other.textureReplace) return false
        if (textureFind != other.textureFind) return false
        if (models != other.models) return false
        if (models2 != other.models2) return false
        if (resizeX != other.resizeX) return false
        if (resizeY != other.resizeY) return false
        if (contrast != other.contrast) return false
        if (ambient != other.ambient) return false
        if (hasRenderPriority != other.hasRenderPriority) return false
        if (configs != null) {
            if (other.configs == null) return false
            if (!configs.contentEquals(other.configs)) return false
        } else if (other.configs != null) return false
        if (aBool2190 != other.aBool2190) return false
        if (params != other.params) return false
        return true
    }

    @ExperimentalUnsignedTypes
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + (combatLevel?.hashCode() ?: 0)
        result = 31 * result + (varpId?.hashCode() ?: 0)
        result = 31 * result + (varp32Id?.hashCode() ?: 0)
        result = 31 * result + isInteractable.hashCode()
        result = 31 * result + drawMapDot.hashCode()
        result = 31 * result + isClickable.hashCode()
        result = 31 * result + rotation.hashCode()
        result = 31 * result + (headIcon?.hashCode() ?: 0)
        result = 31 * result + options.contentHashCode()
        result = 31 * result + (stanceAnimation?.hashCode() ?: 0)
        result = 31 * result + (walkAnimation?.hashCode() ?: 0)
        result = 31 * result + (rotate90RightAnimation?.hashCode() ?: 0)
        result = 31 * result + (rotate90LeftAnimation?.hashCode() ?: 0)
        result = 31 * result + (rotate180Animation?.hashCode() ?: 0)
        result = 31 * result + (anInt2165?.hashCode() ?: 0)
        result = 31 * result + (anInt2189?.hashCode() ?: 0)
        result = 31 * result + (colorReplace?.hashCode() ?: 0)
        result = 31 * result + (colorFind?.hashCode() ?: 0)
        result = 31 * result + (textureReplace?.hashCode() ?: 0)
        result = 31 * result + (textureFind?.hashCode() ?: 0)
        result = 31 * result + (models?.hashCode() ?: 0)
        result = 31 * result + (models2?.hashCode() ?: 0)
        result = 31 * result + resizeX.hashCode()
        result = 31 * result + resizeY.hashCode()
        result = 31 * result + contrast
        result = 31 * result + ambient
        result = 31 * result + hasRenderPriority.hashCode()
        result = 31 * result + (configs?.contentHashCode() ?: 0)
        result = 31 * result + aBool2190.hashCode()
        result = 31 * result + (params?.hashCode() ?: 0)
        return result
    }

    companion object : ConfigCompanion<NpcConfig>() {
        override val id = 9

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): NpcConfig {
            var name = "null"
            var size: UByte = 1u
            var combatLevel: UShort? = null
            var varpId: UShort? = null
            var varp32Id: UShort? = null
            var isInteractable = true
            var drawMapDot = true
            var isClickable = true
            var rotation: UShort = 32u
            var headIcon: UShort? = null
            val options = arrayOfNulls<String>(5)
            var stanceAnimation: UShort? = null
            var walkAnimation: UShort? = null
            var rotate90RightAnimation: UShort? = null
            var rotate90LeftAnimation: UShort? = null
            var rotate180Animation: UShort? = null
            var anInt2165: UShort? = null
            var anInt2189: UShort? = null
            var colorReplace: UShortArray? = null
            var colorFind: UShortArray? = null
            var textureReplace: UShortArray? = null
            var textureFind: UShortArray? = null
            var models: UShortArray? = null
            var models2: UShortArray? = null
            var resizeX: UShort = 128u
            var resizeY: UShort = 128u
            var contrast = 0
            var ambient: Byte = 0
            var hasRenderPriority = false
            var configs: Array<UShort?>? = null
            var aBool2190 = false
            var params: HashMap<Int, Any>? = null

            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    1 -> {
                        val length = buffer.uByte.toInt()
                        models = UShortArray(length) { buffer.uShort }
                    }
                    2 -> name = buffer.string
                    12 -> size = buffer.uByte
                    13 -> stanceAnimation = buffer.uShort
                    14 -> walkAnimation = buffer.uShort
                    15 -> anInt2165 = buffer.uShort
                    16 -> anInt2189 = buffer.uShort
                    17 -> {
                        walkAnimation = buffer.uShort
                        rotate180Animation = buffer.uShort
                        rotate90RightAnimation = buffer.uShort
                        rotate90LeftAnimation = buffer.uShort
                    }
                    in 30..34 -> options[opcode - 30] = buffer.string.takeIf { it != "Hidden" }
                    40 -> {
                        val length = buffer.uByte.toInt()
                        colorFind = UShortArray(length)
                        colorReplace = UShortArray(length)
                        for (i in 0 until length) {
                            colorFind[i] = buffer.uShort
                            colorReplace[i] = buffer.uShort
                        }
                    }
                    41 -> {
                        val length = buffer.uByte.toInt()
                        textureFind = UShortArray(length)
                        textureReplace = UShortArray(length)
                        for (i in 0 until length) {
                            textureFind[i] = buffer.uShort
                            textureReplace[i] = buffer.uShort
                        }
                    }
                    60 -> {
                        val length = buffer.uByte.toInt()
                        models2 = UShortArray(length) { buffer.uShort }
                    }
                    93 -> drawMapDot = false
                    95 -> combatLevel = buffer.uShort
                    97 -> resizeX = buffer.uShort
                    98 -> resizeY = buffer.uShort
                    99 -> hasRenderPriority = true
                    100 -> ambient = buffer.get()
                    101 -> contrast = buffer.get().toInt() * 5
                    102 -> headIcon = buffer.uShort
                    103 -> rotation = buffer.uShort
                    106, 118 -> {
                        varpId = buffer.uShort
                        if(varpId.toInt() == UShort.MAX_VALUE.toInt()) varpId = null

                        varp32Id = buffer.uShort
                        if(varp32Id.toInt() == UShort.MAX_VALUE.toInt()) varp32Id = null

                        val lastEntry = if(opcode == 118) {
                            val entry = buffer.uShort
                            if(entry == UShort.MAX_VALUE) null else entry
                        } else null

                        val length = buffer.uByte.toInt()
                        configs = arrayOfNulls(length + 2)
                        for(i in 0 until configs.size - 1) {
                            var config: UShort? = buffer.uShort
                            if(config!!.toInt() == UShort.MAX_VALUE.toInt()) config = null
                            configs[i] = config
                        }
                        if(opcode == 118) {
                            configs[length + 1] = lastEntry
                        }
                    }
                    107 -> isInteractable = false
                    109 -> isClickable = false
                    111 -> aBool2190 = true
                    249 -> params = buffer.params
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return NpcConfig(id, name, size, combatLevel, varpId, varp32Id, isInteractable, drawMapDot, isClickable,
                rotation, headIcon, options, stanceAnimation, walkAnimation, rotate90RightAnimation,
                rotate90LeftAnimation, rotate180Animation, anInt2165, anInt2189, colorReplace, colorFind,
                textureReplace, textureFind, models, models2, resizeX, resizeY, contrast, ambient, hasRenderPriority,
                configs, aBool2190, params
            )
        }
    }
}