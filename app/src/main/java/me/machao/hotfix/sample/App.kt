package me.machao.hotfix.sample

import android.app.Application
import android.content.Context
import android.os.Environment
import android.support.multidex.MultiDex
import me.machao.hotfix.library.Hotfix
import java.io.File

/**
 * Date  2019/1/23
 * @author charliema
 */
class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)

        checkVersion()
    }

    private fun checkVersion() {
        // 假设当前版本，有一个补丁包，现在下载到了 SD 卡上了。
        val sdPatchDexFile = File(Environment.getExternalStorageDirectory(), "thisisdownloadedpatch.dex")
        // 打补丁dex
        Hotfix.loadDex(this, sdPatchDexFile)
    }
}