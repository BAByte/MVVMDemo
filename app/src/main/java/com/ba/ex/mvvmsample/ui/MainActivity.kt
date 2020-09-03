package com.ba.ex.mvvmsample.ui


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ba.ex.mvvmsample.R
import com.ba.ex.mvvmsample.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>() {
    companion object {
        const val PERMISSIONS_CODE = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
    }

    override fun onBinding(savedInstanceState: Bundle?): ActivityMainBinding =
        DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

    override fun setupUI(binding: ActivityMainBinding) {
    }

    // 请求多个权限
    private fun requestPermissions() {
        // 创建一个权限列表，把需要使用而没用授权的的权限存放在这里
        val permissionList = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(
                this,
                WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(WRITE_EXTERNAL_STORAGE)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(READ_EXTERNAL_STORAGE)
        }

        if (permissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionList.toTypedArray(),
                PERMISSIONS_CODE
            )
        }
    }

    // 请求权限回调方法
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_CODE ->
                // 1002请求码对应的是申请多个权限
                if (grantResults.isNotEmpty()) {
                    // 因为是多个权限，所以需要一个循环获取每个权限的获取情况
                    for (i in grantResults.indices) {
                        // PERMISSION_DENIED 这个值代表是没有授权，我们可以把被拒绝授权的权限显示出来
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.toast_request_permission),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }
                }
        }
    }
}
