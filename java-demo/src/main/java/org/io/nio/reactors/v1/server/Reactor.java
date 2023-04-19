package org.io.nio.reactors.v1.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 事件监听和事件分发
 * https://www.cnblogs.com/hama1993/p/10611229.html
 *
 * @author yancy
 * @version 1.0.0
 * @since 2023/04/19 15:59
 */
public class Reactor implements Runnable {

  private Selector selector;
  private ServerSocketChannel serverSocketChannel;

  public Reactor() throws IOException {
    //开启一个selector
    selector = Selector.open();

    //开启监听端口
    serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.configureBlocking(false);
//    serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 1234));
    serverSocketChannel.bind(new InetSocketAddress(1234));

    //channel上注册selector
    SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    //在selectionKey上绑定回调对象
    selectionKey.attach(new Acceptor(selector, serverSocketChannel));

  }

  public static void main(String[] args) throws IOException {
    new Thread(new Reactor()).start();
  }


  @Override
  public void run() {
    while (!Thread.interrupted()) {
      try {
        selector.select();
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          dispatch(key);
        }
        selectionKeys.clear();
      } catch (Exception e) {

      }
    }
  }


  public void dispatch(SelectionKey key) {

    Runnable r = (Runnable) key.attachment();
    r.run();
  }

}
