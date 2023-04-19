package org.io.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * TODO
 *
 * @author yancy
 * @version 1.0.0
 * @since 2023/04/18 20:08
 */
public class CopyFileNIO {
  public void copy(String srcFileName, String targetFilename) throws Exception {
    RandomAccessFile srcFile = new RandomAccessFile("abc", "rw");
    FileChannel srcChannel = srcFile.getChannel();

    RandomAccessFile targetFile = new RandomAccessFile("copy_file", "rw");
    FileChannel targetChannel = targetFile.getChannel();

    ByteBuffer buffer = ByteBuffer.allocate(2);

    while (srcChannel.read(buffer) != -1) {
      buffer.flip();
      targetChannel.write(buffer);
      while (targetChannel.write(buffer) != 0) {

      }
      buffer.clear();
    }

    targetChannel.force(true);
    targetChannel.close();
    srcChannel.close();

    srcFile.close();
    targetFile.close();


  }


  public void copy2() throws Exception {

    RandomAccessFile srcFile = new RandomAccessFile("abc", "rw");
    FileChannel srcChannel = srcFile.getChannel();

    RandomAccessFile targetFile = new RandomAccessFile("copy_file", "rw");
    FileChannel targetChannel = targetFile.getChannel();

    ByteBuffer buffer = ByteBuffer.allocate(2);

//    while (srcChannel.read(buffer) != -1) {
//      buffer.flip();
//      targetChannel.write(buffer);
//      while (targetChannel.write(buffer) != 0) {
//
//      }
//      buffer.clear();
//    }


//    srcChannel.transferTo(buffer.position(), buffer.limit(),targetChannel);
    targetChannel.transferFrom(srcChannel, buffer.position(), buffer.limit());



//    targetChannel.force(true);
    targetChannel.close();
    srcChannel.close();

    srcFile.close();
    targetFile.close();

  }



  public static void main(String[] args) throws Exception {
    new CopyFileNIO().copy(null, null);
  }

}
