package com.brins.lightmusic.ui.fragment.localmusic


import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.R
import com.brins.lightmusic.manager.PermissionManager
import com.brins.lightmusic.model.LocalMusic
import com.brins.lightmusic.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_localmusic.*

class LocalMusicFragment : BaseFragment(), LocalMusicContract.View {

    lateinit var permissionManager : PermissionManager
    lateinit var mAdapter: LocalMusicAdapter
    lateinit var mPresenter: LocalMusicContract.Presenter

    override fun getLayoutResID(): Int {
        return R.layout.fragment_localmusic
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = LocalMusicAdapter(context!!, null)
        recyclerView.adapter = mAdapter
        requestpermission()
        val PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissionManager.checkPermissions(0,PERMISSIONS[0])
    }


    private fun requestpermission() {
        permissionManager = object : PermissionManager((activity as AppCompatActivity?)!!) {
            override fun authorized(requestCode: Int) {
                LocalMusicPresent(this@LocalMusicFragment).subscribe()
            }

            override fun noAuthorization(requestCode: Int, lacksPermissions: Array<String>) {
                val builder = AlertDialog.Builder(activity!!)
                builder.setTitle("提示")
                builder.setMessage("缺少读取权限！")
                builder.setPositiveButton("设置权限") { _, _ -> PermissionManager.startAppSettings(context!!) }
                builder.create().show()            }
        }
    }


    override fun getcontext(): Context {
        return context!!
    }

    override fun handleError(error: Throwable) {
        Toast.makeText(activity, error.message , Toast.LENGTH_SHORT).show()
    }

    override fun onLocalMusicLoaded(songs: MutableList<LocalMusic>) {
        mAdapter.data = songs
        mAdapter.notifyDataSetChanged()
    }

    override fun setPresenter(presenter: LocalMusicContract.Presenter?) {
        mPresenter = presenter!!
    }
}
