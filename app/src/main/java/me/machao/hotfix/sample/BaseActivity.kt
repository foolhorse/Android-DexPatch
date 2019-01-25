package me.machao.hotfix.sample

import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity

/**
 * Date  2019/1/23
 * @author charliema
 */
open class BaseActivity : AppCompatActivity() {

    internal fun requestPermissions(permissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(permissions, 200)
            }
        }
    }
}