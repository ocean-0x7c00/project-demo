package org.io.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author yancy
 * @version 1.0.0
 * @since 2023/04/18 16:51
 */
public class NIODemo {
  public void bufferDemo() {
    IntBuffer intBuffer = IntBuffer.allocate(5);
    for (int i = 0; i < intBuffer.limit(); i++) {
      intBuffer.put(i + 1);

    }



    intBuffer.flip();
    while (intBuffer.position() < intBuffer.limit()) {
      System.out.println(intBuffer.get());
    }
    intBuffer.put(1212);
    intBuffer.clear();
    intBuffer.put(1212);
    while (intBuffer.position() < intBuffer.limit()) {
      System.out.println(intBuffer.get());
    }
  }


  /**
   * FileChannel;
   * SocketChannel;
   * DatagramChannel;
   * ServerSocketChannel;
   */
  public void channelDemo() throws Exception {
    RandomAccessFile file = new RandomAccessFile("abc", "rw");
    FileChannel channel = file.getChannel();


    String msg = "Hello Channel";
    byte[] bytes = msg.getBytes();
    ByteBuffer byteBuffer = ByteBuffer.allocate(2);
    for (int i = 0; i < bytes.length; i++) {
      while (byteBuffer.position() < byteBuffer.limit()) {
        byteBuffer.put(bytes[i]);
      }
      //todo 写到文件中
      channel.write(byteBuffer);
      byteBuffer.clear();
    }


    channel.close();
    file.close();
  }


  public void selectorDemo() {
  }


  public static void main(String[] args) throws Exception {
    NIODemo nioDemo = new NIODemo();

//    nioDemo.channelDemo();
    nioDemo.read();


  }

  public void printBufferInfo(IntBuffer intBuffer) {
    System.out.println();
    System.out.println("=============");
    System.out.println("intBuffer.capacity() = " + intBuffer.capacity());
    System.out.println("intBuffer.position() = " + intBuffer.position());
    System.out.println("intBuffer.limit() = " + intBuffer.limit());


    System.out.println("============");
    System.out.println();
  }



  public void read() throws Exception {
    RandomAccessFile file = new RandomAccessFile("abc", "rw");
    FileChannel fileChannel = file.getChannel();

    List<Byte> bytesList = new ArrayList<>();
    ByteBuffer buffer = ByteBuffer.allocate(2);
    String line = "";
    boolean flag = false;
    while (fileChannel.read(buffer) != -1) {
      buffer.flip();
      CharBuffer decode = StandardCharsets.UTF_8.decode(buffer);
      line += decode.toString();
      if (decode.toString().contains("\n")) {
        flag = true;
        System.out.println(line.trim());
        line = "";
      }
//      System.out.println(decode);


      buffer.rewind();
      while (buffer.position() != buffer.limit()) {
        bytesList.add(buffer.get());
      }
      buffer.clear();
    }
    fileChannel.close();
    file.close();

    if (flag) {
      System.out.println(line);
    }


    byte[] bytes = new byte[bytesList.size()];


    for (int i = 0; i < bytesList.size(); i++) {
      bytes[i] = bytesList.get(i);
    }

    String msg = new String(bytes);
//    System.out.println(msg);


//    fileChannel.transferFrom();
//    fileChannel.transferTo()
  }

}
