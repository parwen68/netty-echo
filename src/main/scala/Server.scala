import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.Unpooled
import io.netty.channel.{ChannelFutureListener, ChannelHandlerContext, ChannelInboundHandlerAdapter, ChannelInitializer}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.SocketChannel

class EchoServer(port: Int) {
  def start() {
    val group = new NioEventLoopGroup
    try {
      val b = new ServerBootstrap
      b.group(group)
       .channel(classOf[NioServerSocketChannel])
       .localAddress("localhost", 9090)
       .childHandler(new ChannelInitializer[SocketChannel] {
        def initChannel(ch: SocketChannel){
          ch.pipeline().addLast(new EchoServerHandler)
        }
      })
      val f = b.bind().sync
      println(s"${classOf[EchoServer].getName} started and listening on ${f.channel().localAddress}")
      f.channel().closeFuture().sync
    } catch {
      case e: InterruptedException => println("Shutting down server...")
    } finally {
      group.shutdownGracefully().sync
    }
  }
}

class EchoServerHandler extends ChannelInboundHandlerAdapter {

  override def channelReadComplete(ctx: ChannelHandlerContext) {
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE)
  }

  override def channelRead(ctx: ChannelHandlerContext, msg: scala.Any) {
    println(s"Server received: $msg")
    ctx.write(msg)
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
    cause.printStackTrace()
    ctx.close
  }
}