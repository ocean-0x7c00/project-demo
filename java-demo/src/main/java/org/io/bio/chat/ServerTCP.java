package org.io.bio.chat;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
    public static void main(String[] args) throws IOException {
        //1.创建一个套接字
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("127.0.0.1", 9999));

        //2.等待客户端连接
        Socket socket = serverSocket.accept();

        //3.获取socket通道流
        InputStream inputStream = socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//        String line = null;
//
//        while ((line = bufferedReader.readLine()) != null) {
//            System.out.println(line);
//        }
//
//        System.out.println();


        OutputStream outputStream = socket.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


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
        bufferedWriter.flush();
        serverSocket.close();

    }
}
