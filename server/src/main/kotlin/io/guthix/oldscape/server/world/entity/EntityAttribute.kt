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
package io.guthix.oldscape.server.world.entity

import kotlin.reflect.KProperty

class EntityAttribute<T : Any?> {
    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Entity, property: KProperty<*>): T {
        return thisRef.attributes[property] as T
    }

    operator fun setValue(thisRef: Entity, property: KProperty<*>, value: T) {
        thisRef.attributes[property] = value
    }
}