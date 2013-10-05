/**
 *
 */
object Main {
  def main(args: Array[String]): Unit = {
    if(args.length != 1) println("Error:")
    val port = args(0).toInt

    val serverThread = new Thread(new Runnable(){
      def run() {
        new EchoServer(port).start()
        println("Server exited")
      }
    })
    val clientThread = new Thread(new Runnable {
      def run() {
        new EchoClient("localhost", port).start()
        println("Client exited")
      }
    })
    serverThread.start()
    Thread.sleep(1000L)
    clientThread.start()
    Thread.sleep(1000L)
    serverThread.interrupt()
  }
}
