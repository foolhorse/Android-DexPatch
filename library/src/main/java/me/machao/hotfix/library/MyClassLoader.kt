package me.machao.hotfix.library

import android.content.Context
import dalvik.system.DexClassLoader

/**
 * Date  2019/1/23
 * @author charliema
 */
class MyClassLoader(
    dexPath: String?,
    optimizedDirectory: String?,
    librarySearchPath: String? = null,
    parent: ClassLoader?
) : DexClassLoader(dexPath, optimizedDirectory, librarySearchPath, parent) {


    constructor(
        dexPath: String?,
        optimizedDirectory: String?,
        context: Context
    ) : this(dexPath, optimizedDirectory, null, context.classLoader)
}