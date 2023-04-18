package org.io.bio.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientTCP {
    public static void main(String[] args) {
        try {
            //1.创建一个客户端的套接字(尝试连接）
            Socket socket = new Socket("127.0.0.1", 9999);

            //2.获取socket通道的输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //3.获取socket 通道的输出流
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


            //循环读取数据，并拼接到文本域
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            //4.关闭socket 通道
            socket.close();
        } catch (Exception e) {
            System.out.println();

        }
    }
}
