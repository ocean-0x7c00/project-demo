package org.io.nio.tea;

/**
 * 异步阻塞模式（主线程阻塞等待，子线程完成后才能继续运行）
 *
 * 调用方 调用  被调用方（阻塞等待） 如何翻转这个关系？
 *
 * 被调用方 调用 调用方 （非阻塞异步）
 *
 *
 *
 * @author yancy
 * @version 1.0.0
 * @since 2023/04/18 11:25
 */
public class BrewTeaMainThread {
  public static void main(String[] args) throws InterruptedException {
    BoilWaterThread boilWaterThread = new BoilWaterThread();
    boilWaterThread.start();
    WashThread washThread = new WashThread();
    washThread.start();


    boilWaterThread.join();
    washThread.join();

    System.out.println("泡茶喝");


  }
}
