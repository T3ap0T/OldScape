/**
 * This file is part of Guthix OldScape.
 *
 * Guthix OldScape is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Guthix OldScape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
package io.guthix.oldscape.server.api

import io.guthix.oldscape.cache.ConfigArchive
import io.guthix.oldscape.cache.config.EnumConfig
import io.guthix.oldscape.server.Cache
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }

data class Component(val interfaceId: Int, val componentId: Int)

fun readComponent(value: Int) = Component(value shr Short.SIZE_BITS, value and 0xFFFF)

object Enums {
    private lateinit var configs: Map<Int, EnumConfig>

    operator fun get(index: Int) = configs[index]

    fun load() {
        configs = EnumConfig.load(Cache.getGroup(ConfigArchive.id, EnumConfig.id))
        logger.info { "Loaded ${configs.size} enums" }
    }
}