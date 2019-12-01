/**
 * This file is part of Guthix OldScape.
 *
 * Guthix OldScape is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Guthix OldScape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
package io.guthix.oldscape.wiki

import io.guthix.cache.js5.Js5Cache
import io.guthix.cache.js5.container.disk.Js5DiskStore
import io.guthix.oldscape.cache.ConfigArchive
import io.guthix.oldscape.cache.config.NpcConfig
import io.guthix.oldscape.wiki.wikitext.NpcWikiDefinition
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.*
import java.nio.file.Path
import kotlin.Exception

@KtorExperimentalAPI
fun main() {
    JsonDownloader().load()
}

class JsonDownloader {
    @KtorExperimentalAPI
    fun load() {
        val ds = Js5DiskStore.open(Path.of(JsonDownloader::class.java.getResource("/cache").toURI()))
        val cache = Js5Cache(ds)
        val npcConfigs = NpcConfig.load(cache.readArchive(ConfigArchive.id).readGroup(NpcConfig.id)).values.groupBy {
            it.id / stepSize
        }.values
        val scope = CoroutineScope(Dispatchers.Default)
        val definitions = mutableListOf<NpcWikiDefinition>()
        for(subConfigList in npcConfigs) {
            val deffered = subConfigList.map { config ->
                scope.async {
                    val string = scrapeWikiText(NpcWikiDefinition.queryString, config.id, config.name)
                    NpcWikiDefinition().parse(string, null)
                }
            }
            runBlocking {
                val curDefinitions = deffered.awaitAll()
                definitions.addAll(curDefinitions)
            }
        }
    }

    companion object {
        const val stepSize = 50
    }
}