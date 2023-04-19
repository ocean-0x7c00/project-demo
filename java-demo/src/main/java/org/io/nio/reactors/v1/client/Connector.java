package org.io.nio.reactors.v1.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * TODO
 *
 * @author yancy
 * @version 1.0.0
 * @since 2023/04/19 19:30
 */
public class Connector implements Runnable {
  private Selector selector;
  private SocketChannel socketChannel;

  public Connector(Selector selector, SocketChannel socketChannel) {
    this.selector = selector;
    this.socketChannel = socketChannel;
  }

  @Override
  public void run() {
    try {
      if (socketChannel.finishConnect()) {
        System.out.println(String.format("已完成 %s 的连接", socketChannel.getRemoteAddress()));
        new Handler(selector, socketChannel);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
