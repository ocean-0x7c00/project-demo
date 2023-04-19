package org.io.nio.tea;

/**
 * TODO
 *
 * @author yancy
 * @version 1.0.0
 * @since 2023/04/18 11:26
 */
public class WashThread extends Thread {
  @Override
  public void run() {

    try {
      System.out.println("Start Wash...");
      System.out.println("洗茶壶");
      System.out.println("洗茶杯");
      System.out.println("拿茶叶");
      Thread.sleep(9000);
      System.out.println("洗完了");
      System.out.println();
    } catch (InterruptedException e) {
    }

  }
}
