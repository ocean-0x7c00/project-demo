package org.io.nio.multiplexing;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * 通道
 * 选择器
 *
 * @author yancy
 * @version 1.0.0
 * @since 2023/04/19 14:20
 */
public class ServerDemo {
  public static void main(String[] args) throws IOException {
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 1234));

    Selector selector = Selector.open();
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    while (selector.select() > 0) {
      Set<SelectionKey> selectionKeys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = selectionKeys.iterator();
      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        if (key.isAcceptable()) {

          ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
//          key.channel()和serverSocketChannel是一样的
          System.out.println(ssc == serverSocketChannel);
          SocketChannel socketChannel = ssc.accept();
          socketChannel.configureBlocking(false);
          socketChannel.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
          SocketChannel channel = (SocketChannel) key.channel();
          ByteBuffer buffer = ByteBuffer.allocate(1024);


          while (channel.read(buffer) > 0) {
            buffer.flip();
            CharBuffer decode = StandardCharsets.UTF_8.decode(buffer);
            System.out.println(decode.toString());
            buffer.clear();
          }

          channel.close();
        }

        iterator.remove();
      }
    }

    serverSocketChannel.close();

  }
}
