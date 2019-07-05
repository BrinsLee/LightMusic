package com.brins.lightmusic.manager

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.ArrayList

abstract class PermissionManager (var targetActivity : AppCompatActivity){

    companion object {
        val PACKAGE_URL_SCHEME = "package:"
        /**
         * 进入应用设置
         *
         * @param context context
         */
        fun startAppSettings(context: Context) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.data = Uri.parse(PACKAGE_URL_SCHEME + context.packageName)
            context.startActivity(intent)
        }
    }

    abstract fun authorized(requestCode: Int)

    /**
     * 有权限没有通过
     *
     * @param requestCode      请求码
     * @param lacksPermissions 被拒绝的权限
     */
    abstract fun noAuthorization(requestCode: Int, lacksPermissions: Array<String>)

    /**
     * 检查权限
     *
     * @param requestCode 请求码
     * @param permissions 准备校验的权限
     */
    fun checkPermissions(requestCode: Int, vararg permissions: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6.0 动态检查权限
            val lacks = ArrayList<String>()
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(targetActivity, permission) == PackageManager.PERMISSION_DENIED) {
                    lacks.add(permission)
                }
            }

            if (!lacks.isEmpty()) {
                // 有权限没有授权
                var lacksPermissions: Array<String> = lacks.toTypedArray()
                //申请权限
                ActivityCompat.requestPermissions(targetActivity, lacksPermissions, requestCode)
            } else {
                // 授权
                authorized(requestCode)
            }
        }
    }



    fun recheckPermissions(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                // 未授权
                noAuthorization(requestCode, permissions)
                return
            }
        }
        // 授权
        authorized(requestCode)
    }

}