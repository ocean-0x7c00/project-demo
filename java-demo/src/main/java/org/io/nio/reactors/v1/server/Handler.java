package org.io.nio.reactors.v1.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 负责处理读写操作
 *
 * @author yancy
 * @version 1.0.0
 * @since 2023/04/19 16:00
 */
public class Handler implements Runnable {
  private SelectionKey selectionKey;
  private SocketChannel socketChannel;

  private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
  private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

  private final static int READ = 0;
  private final static int SEND = 1;
  int status = READ;

  public Handler(Selector selector, SocketChannel socketChannel) throws IOException {
    //接收客户端连接
    this.socketChannel = socketChannel;
    this.socketChannel.configureBlocking(false);
    //将该客户端注册到selector，得到一个SelectionKey，以后的select到的就绪动作全都是由该对象进行封装
//    selectionKey = this.socketChannel.register(selector, SelectionKey.OP_READ);
    //与之前的注册方式不同，先仅仅取得选择键
    // 之后在设置感兴趣的IO事件（selectionKey.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ)）
    selectionKey = this.socketChannel.register(selector, 0);


    //附加处理对象，当前是Handler对象，run是对象处理业务的方法
    selectionKey.attach(this);


    //注册读写事件
    //走到这里，说明之前Acceptor里的建连已完成，那么接下来就是读取动作，因此这里首先将读事件标记为“感兴趣”事件
    selectionKey.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);


    //唤起select阻塞
    selector.wakeup();


  }

  @Override
  public void run() {
    //Reactors和Handle在同一个线程中，如果Handle发生了阻塞，会影响到Reactors
    //TODO 改进方式：a.Handle处理使用多线程 b.Reactors中引入多Selector


    try {
      switch (status) {
        case READ:
          read();
          break;
        case SEND:
          send();
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + status);
      }

    } catch (IOException e) {
      //这里的异常处理是做了汇总，
      //常出的异常就是server端还有未读/写完的客户端消息，客户端就主动断开连接，这种情况下是不会触发返回-1的，
      //这样下面read和write方法里的cancel和close就都无法触发，这样会导致死循环异常
      //（read/write处理失败，事件又未被cancel，因此会不断的被select到，不断的报异常）
      e.printStackTrace();
    }

  }

  private void read() throws IOException {
    if (selectionKey.isValid()) {
      readBuffer.clear();
      int count = socketChannel.read(readBuffer); //read方法结束，意味着本次"读就绪"变为"读完毕"，标记着一次就绪事件的结束
      if (count > 0) {
        System.out.println(String.format("收到来自 %s 的消息: %s",
            socketChannel.getRemoteAddress(),
            new String(readBuffer.array())));
        status = SEND;
        selectionKey.interestOps(SelectionKey.OP_WRITE); //注册写方法
      } else {
        //读模式下拿到的值是-1，说明客户端已经断开连接，那么将对应的selectKey从selector里清除，否则下次还会select到，因为断开连接意味着读就绪不会变成读完毕，也不cancel，下次select会不停收到该事件
        //所以在这种场景下，（服务器程序）你需要关闭socketChannel并且取消key，最好是退出当前函数。注意，这个时候服务端要是继续使用该socketChannel进行读操作的话，就会抛出“远程主机强迫关闭一个现有的连接”的IO异常。
        selectionKey.cancel();
        socketChannel.close();
        System.out.println("read时-------连接关闭");
      }
    }
  }

  void send() throws IOException {
    if (selectionKey.isValid()) {
      writeBuffer.clear();
      writeBuffer.put(String.format("我收到来自%s的信息辣：%s,  200ok;",
          socketChannel.getRemoteAddress(),
          new String(readBuffer.array())).getBytes());
      writeBuffer.flip();
      int count = socketChannel.write(writeBuffer); //write方法结束，意味着本次写就绪变为写完毕，标记着一次事件的结束

      if (count < 0) {
        //同上，write场景下，取到-1，也意味着客户端断开连接
        selectionKey.cancel();
        socketChannel.close();
        System.out.println("send时-------连接关闭");
      }

      //没断开连接，则再次切换到读
      status = READ;
      selectionKey.interestOps(SelectionKey.OP_READ);
    }
  }

}
