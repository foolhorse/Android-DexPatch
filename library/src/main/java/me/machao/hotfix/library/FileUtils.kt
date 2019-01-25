package me.machao.hotfix.library

import java.io.*

/**
 * Date  2019/1/23
 * @author charliema
 */

fun copyFile(sourceFile: File, targetFile: File){

    sourceFile.copyTo(targetFile, true)

//    val fis = sourceFile.inputStream()
//    val bis = fis.buffered()
//
//    val fos = targetFile.outputStream()
//    val bos = fos.buffered()
//
//    val byteArr = ByteArray(1024 * 4)
//    var length = bis.read(byteArr)
//    while (length != -1) {
//        bos.write(byteArr, 0, length)
//        length = bis.read(byteArr)
//    }
//
//    bos.flush()
//
//    fis.close()
//    bis.close()
//    fos.close()
//    bos.close()
}