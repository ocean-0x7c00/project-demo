package org.io.bio.chat;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
  public static void main(String[] args) {
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    BufferedReader bufferedReader = null;
    BufferedWriter bufferedWriter = null;
    try {

      //1.创建一个服务器端的套接字，并监听9999端口，等待客户端的连接
      serverSocket = new ServerSocket();
      serverSocket.bind(new InetSocketAddress("127.0.0.1", 9999));

      //2.等待客户端连接，一旦有客户端连接到该接口，serverSocket将返回一个新的socket对象。
      //该对象表示与客户端的连接，使用socket对象完成客户端与服务端的通信
      clientSocket = serverSocket.accept();

      //3.读客户端发来的信息
      bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      String line = null;


      //这段代码会阻塞程序，直到读取到一行数据或者输入流被关闭为止，因此会一直等待客户端发送消息，导致服务端无法往客户端写入数据。
      //为了解决这个问题，可以在读取客户端发送的消息后，立即关闭输入流，然后再向客户端发送响应
      //客户端读取服务端响应时，使用的是 readLine() 方法，但是服务端发送的响应中并没有换行符，因此客户端一直在等待换行符，导致程序一直阻塞。
//      while ((line = bufferedReader.readLine()) != null) {
//        System.out.println(line);
//      }
//
//      bufferedReader.close();
//      //关闭输入流，立即向客户端发送响应
//      clientSocket.shutdownInput();


      //4.服务端返回给客户端的信息
      bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

      String msg = "HTTP/1.1 200 OK\n" +
          "Date: Fri, 22 May 2009 06:07:21 GMT\n" +
          "Content-Type: text/html; charset=UTF-8\n" +
          "\n" +
          "<html>\n" +
          "      <head></head>\n" +
          "      <body>\n" +
          "            hello\n" +
          "      </body>\n" +
          "</html>";

      bufferedWriter.write(msg);

      //bufferedWriter.flush() 的作用是将缓冲区中的数据强制输出到目标设备中
      //确保数据被及时发送。如果不执行 flush()，则可能会导致数据在缓冲区中滞留，无法及时发送到目标设备，从而影响程序的正常运行。

      //如果不执行 bufferedWriter.flush()
      //则服务端发送的响应可能会滞留在缓冲区中，无法及时发送到客户端，从而导致客户端无法接收到完整的响应。
      //因此，建议在发送完数据后，一定要执行 flush() 方法，以确保数据被及时发送。
      bufferedWriter.flush();

      bufferedWriter.close();
      clientSocket.close();
      serverSocket.close();


    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
