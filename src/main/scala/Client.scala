import io.netty.bootstrap.Bootstrap
import io.netty.buffer.{ByteBufUtil, Unpooled, ByteBuf}
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler, ChannelInitializer}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.socket.SocketChannel
import io.netty.util.CharsetUtil
import java.net.InetSocketAddress

/**
 *
 */
class EchoClient(host: String, port: Int) {
  def start() {
    val group = new NioEventLoopGroup
    try {
      val b = new Bootstrap
      b.group(group)
       .channel(classOf[NioSocketChannel])
       .remoteAddress(new InetSocketAddress(host,port))
       .handler(new ChannelInitializer[SocketChannel] {
        def initChannel(ch: SocketChannel) {
          ch.pipeline().addLast(new EchoClientHandler)
        }
      })
      val f = b.connect.sync
      println(s"${classOf[EchoClient].getName} started and sending to ${f.channel.remoteAddress}")

      f.channel.closeFuture.sync
    } finally group.shutdownGracefully().sync
  }
}

class EchoClientHandler extends SimpleChannelInboundHandler[ByteBuf] {

  override def channelActive(ctx: ChannelHandlerContext) {
    ctx.writeAndFlush(Unpooled.copiedBuffer("Netty Rocks!", CharsetUtil.UTF_8))
  }

  override def channelRead0(ctx: ChannelHandlerContext, in: ByteBuf) {
    println(s"Client received: ${ByteBufUtil.hexDump(in.readBytes(in.readableBytes))}")
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
    cause.printStackTrace
  }

}