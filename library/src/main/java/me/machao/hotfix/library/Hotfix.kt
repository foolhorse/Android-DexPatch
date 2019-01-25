package me.machao.hotfix.library

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import dalvik.system.BaseDexClassLoader
import java.io.File

/**
 * Date  2019/1/23
 * @author charliema
 */
object Hotfix {

    const val TAG = "HotFix"

    const val DEX_DIR_NAME = "dex"
    const val DEX_OPT_DIR_NAME = "dexopt"
    const val DEX_FILE_NAME = "patch.dex"
    const val DEX_SUFFIX = ".dex"

//        var myDexFileSet = setOf<File>()

    init {
//            myDexFileSet.clear()
    }

    fun loadDex(context: Context, patchDexFile: File) {
        if (!patchDexFile.exists()) {
            Log.d(TAG, "patch dex file does not exist")
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                Log.e(TAG, "HotFix WRITE_EXTERNAL_STORAGE PERMISSION_DENIED")
                return
            }
        }
        // copy to app dir
        val appDirFile =
            File(context.getDir(Hotfix.DEX_DIR_NAME, Context.MODE_PRIVATE).absolutePath, Hotfix.DEX_FILE_NAME)

        copyFile(patchDexFile, appDirFile)
        Log.e(TAG, "appDirFile length:" + appDirFile.length())

//        myDexFileSet = getAppDexDirFiles(context)

        getAppDexDir(context)
            .listFiles()
            .filter {
                it.name.endsWith(DEX_SUFFIX) && it.name != "classes.dex"
            }
            .map {
                val myClassLoader = MyClassLoader(it.absolutePath, getAppDexOptDir(context).absolutePath, context)

                // dexElements
                getDexElements(getPathList(myClassLoader)!!)
            }
            .reduce { total, next ->
                joinArray(total!!, next!!)
            }
            .let {
                val sysClassLoader = context.classLoader as BaseDexClassLoader

                // PathList
                val sysPathList = getPathList(sysClassLoader)!!
                val sysDexElements = getDexElements(sysPathList)

                val newDexElements =
                    if (it != null && sysDexElements != null) {
                        joinArray(it, sysDexElements)
                    } else {
                        null
                    }
                // pathList
                setField(sysPathList, "dexElements", newDexElements)
            }
    }

    private fun getAppDexDir(context: Context): File {
        val dir = context.getDir(DEX_DIR_NAME, Context.MODE_PRIVATE)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    private fun getAppDexOptDir(context: Context): File {
        val dir = context.getDir(DEX_OPT_DIR_NAME, Context.MODE_PRIVATE)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    private fun getPathList(baseDexClassLoaderObj: BaseDexClassLoader) =
        getField(
            baseDexClassLoaderObj,
            "pathList",
            Class.forName("dalvik.system.BaseDexClassLoader")
        )

    private fun getDexElements(pathListObj: Any) =
        getField(
            pathListObj,
            "dexElements",
            Class.forName("dalvik.system.DexPathList")
        )

    private fun getField(obj: Any, fieldStr: String, clz: Class<*>): Any? {
        val field = clz.getDeclaredField(fieldStr)
        field.isAccessible = true
        return field.get(obj)
    }

    private fun getField(obj: Any, fieldStr: String): Any? {
        val field = obj.javaClass.getDeclaredField(fieldStr)
        field.isAccessible = true
        return field.get(obj)
    }

    private fun setField(obj: Any, fieldStr: String, value: Any?) {
        val field = obj.javaClass.getDeclaredField(fieldStr)
        field.isAccessible = true
        field.set(obj, value)
    }

    private fun joinArray(array1: Any, array2: Any): Any {
        val length1 = java.lang.reflect.Array.getLength(array1)
        val length2 = java.lang.reflect.Array.getLength(array2)
        val length = length1 + length2

        val clz = array1.javaClass.componentType
        val result = java.lang.reflect.Array.newInstance(clz, length)
//            for (i in 0 until length) {
        repeat(length) {
            if (it < length1) {
                java.lang.reflect.Array.set(
                    result,
                    it,
                    java.lang.reflect.Array.get(array1, it)
                )
            } else {
                java.lang.reflect.Array.set(
                    result,
                    it,
                    java.lang.reflect.Array.get(array2, it - length1)
                )
            }
        }
        return result
    }
}

