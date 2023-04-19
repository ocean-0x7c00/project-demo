package org.io.nio.multiplexing;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * TODO
 *
 * @author yancy
 * @version 1.0.0
 * @since 2023/04/19 15:10
 */
public class ClientDemo {
  public static void main(String[] args) throws IOException {
    InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 1234);
    SocketChannel socketChannel = SocketChannel.open(inetSocketAddress);

    ByteBuffer buffer = ByteBuffer.allocate(1024);
    buffer.put("hello world".getBytes());

    buffer.flip();
    socketChannel.write(buffer);
    //立刻关闭输出流
    socketChannel.shutdownOutput();
    socketChannel.close();



  }

}
