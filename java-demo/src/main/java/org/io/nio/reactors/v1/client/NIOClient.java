package org.io.nio.reactors.v1.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * TODO
 *
 * @author yancy
 * @version 1.0.0
 * @since 2023/04/19 17:05
 */

public class NIOClient implements Runnable {

  private Selector selector;
  private SocketChannel socketChannel;

  public NIOClient() throws IOException {
    this.selector = Selector.open();

    this.socketChannel = SocketChannel.open();
    socketChannel.bind(new InetSocketAddress("127.0.0.1", 1234));
    socketChannel.configureBlocking(false);


    SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_CONNECT);

    selectionKey.attach(new Connector(selector, socketChannel));
  }

  @Override
  public void run() {
    try {
      while (!Thread.interrupted()) {
        selector.select();
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
          dispatch(iterator.next());
        }
        selectionKeys.clear();
      }
    } catch (IOException e) {
    }

  }

  void dispatch(SelectionKey k) {
    Runnable r =
        (Runnable) (k.attachment()); //这里很关键，拿到每次selectKey里面附带的处理对象，然后调用其run，这个对象在具体的Handler里会进行创建，初始化的附带对象为Connector（看上面构造器）
    //调用之前注册的callback对象
    if (r != null) {
      r.run();
    }
  }


  public static void main(String[] args) throws IOException {
    new Thread(new NIOClient()).start();
//    new Thread(new NIOClient()).start();
  }
}
