package org.io.nio.tea;

/**
 * TODO
 *
 * @author yancy
 * @version 1.0.0
 * @since 2023/04/18 11:26
 */
public class BoilWaterThread extends Thread {
  @Override
  public void run() {
    try {
      System.out.println("Start Boil Water...");
      System.out.println("洗好茶壶");
      System.out.println("灌上凉水");
      System.out.println("放在火上");
      Thread.sleep(9000);
      System.out.println("水开了");

      System.out.println();
    } catch (InterruptedException e) {
    }
  }
}
