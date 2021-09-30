package com.ba.ex.mvvmsample.ui.activity


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ba.ex.mvvmsample.R
import com.ba.ex.mvvmsample.databinding.ActivityMainBinding
import com.ba.ex.mvvmsample.log.workers.ArtificialUploadLogWorker
import com.ba.ex.mvvmsample.ui.fragment.viewmodels.LoggerInfoViewModel
import com.ba.ex.mvvmsample.ui.fragment.viewmodels.MsgViewModel
import com.orhanobut.logger.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : BaseActivity<ActivityMainBinding>() {
    companion object {
        const val PERMISSIONS_CODE = 1002
    }

    private var receiveMsgStart = false
    private var confirmDialog: Dialog? = null //删除dialog
    private val loggerInfoViewModel: LoggerInfoViewModel by viewModels()
    private val msgViewModel: MsgViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
    }

    override fun onBinding(savedInstanceState: Bundle?): ActivityMainBinding =
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

    override fun setupUI(binding: ActivityMainBinding) {
        title = getString(R.string.main_activity_title)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_like -> {
                getDataBinding().navHost.findNavController()
                    .navigate(R.id.home_fragment_to_like_fruit_fragment)

                true
            }
            R.id.menu_upload_logger -> {
                showUpLoadLogDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        receiveMsg(menu)
        return super.onPrepareOptionsMenu(menu)
    }

    private fun receiveMsg(menu: Menu) {
        //防止重复创建job
        if (receiveMsgStart) {
            return
        }

        lifecycleScope.launch {
            val msg = getString(R.string.menu_msg)
            receiveMsgStart = true
            //只有在数据有变化时，且生命周期处与STARTED后才能收到，适合用来做实时状态监听的视图
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                msgViewModel.msgNum.collectLatest {
                    Logger.d(">>> onPrepareOptionsMenu $it")
                    //item加载可能会慢一点，所以加个判空检测
                    while (menu.findItem(R.id.menu_msg) == null) {
                        delay(500)
                    }
                    menu.findItem(R.id.menu_msg).title = java.lang.String.format(msg, it)
                }
            }
            receiveMsgStart = false
        }
    }

    private fun showUpLoadLogDialog() {
        var num = 0
        loggerInfoViewModel.logInfo.value?.let {
            num = it.size
        }
        //日志数量
        confirmDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.upload_log_dialog_title))
            .setMessage("${getString(R.string.upload_log_dialog_message)} : $num")
            .setPositiveButton(getString(R.string.dialog_ok)) { dialog, which ->
                val request = OneTimeWorkRequestBuilder<ArtificialUploadLogWorker>().build()
                WorkManager.getInstance(this).enqueue(request)
                Toast.makeText(
                    this,
                    getString(R.string.toast_upload_loginfo),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { dialog, which ->
            }.create().apply { show() }
    }

    override fun onDestroy() {
        super.onDestroy()
        confirmDialog?.cancel()
    }
}
