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
package io.guthix.oldscape.server.net.state.js5

import io.guthix.cache.js5.container.disk.Js5DiskStore
import io.guthix.oldscape.server.Cache
import io.guthix.oldscape.server.net.PacketInboundHandler
import io.netty.channel.ChannelHandlerContext

class Js5Handler : PacketInboundHandler<Js5FileRequest>() {
    override fun channelRead0(ctx: ChannelHandlerContext, msg: Js5FileRequest) {
        val data = if(msg.indexFileId == Js5DiskStore.MASTER_INDEX) {
            Cache.getRawSettings(msg.containerId)
        } else {
            Cache.getRawGroup(msg.indexFileId, msg.containerId).data
        }.duplicate() // TODO remove copying
        val compressionType = data.readUnsignedByte().toInt()
        val compressedSize = data.readInt()
        val response = Js5FileResponse(msg.indexFileId, msg.containerId, compressionType, compressedSize, data)
        try {
            ctx.writeAndFlush(response)
        } finally {
            if(response.refCnt() > 0) response.release() //TODO make this better, probably not the best solution
        }
    }
}