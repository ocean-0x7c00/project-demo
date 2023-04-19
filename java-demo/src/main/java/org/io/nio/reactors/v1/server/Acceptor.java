package org.io.nio.reactors.v1.server;

import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 处理连接就绪事件
 *
 * @author yancy
 * @version 1.0.0
 * @since 2023/04/19 16:00
 */
public class Acceptor implements Runnable {
  private Selector selector;
  private ServerSocketChannel serverSocketChannel;

  public Acceptor(Selector selector, ServerSocketChannel serverSocketChannel) {
    this.selector = selector;
    this.serverSocketChannel = serverSocketChannel;
  }

  @Override
  public void run() {
    SocketChannel socketChannel;
    try {
      socketChannel = serverSocketChannel.accept();
      if (socketChannel != null) {
        new Handler(selector, socketChannel);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
