package com.brins.lightmusic.ui.fragment.localmusic


import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.utils.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.manager.PermissionManager
import com.brins.lightmusic.model.loaclmusic.LocalMusic
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.ListAdapter
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.widget.CommonHeaderView
import com.brins.lightmusic.utils.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_localmusic.*
@AndroidEntryPoint
class LocalMusicFragment : BaseFragment(),
    CommonHeaderView.OnBackClickListener, LocalMusicContract.View, OnItemClickListener,
    View.OnClickListener {


    lateinit var permissionManager: PermissionManager
    lateinit var mAdapter: ListAdapter<LocalMusic>
    private var playList: PlayList = PlayList()
    private var isLoad = false

    override fun getLayoutResID(): Int {
        return R.layout.fragment_localmusic
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = ListAdapter(context!!, null)
        setListener()
        recyclerView.setItemViewCacheSize(5)
        recyclerView.adapter = mAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(SpacesItemDecoration(context!!,10,R.color.gery))
        requestpermission()
    }

    private fun setListener() {
        mAdapter.setOnItemClickListener(this)
        toolbar.setOnBackClickListener(this)
        toolbar.title = getString(R.string.local_music)
    }

    override fun onResume() {
        super.onResume()
        if (!isLoad) {
            val PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionManager.checkPermissions(0, PERMISSIONS[0])
        }
    }

    private fun requestpermission() {
        permissionManager = object : PermissionManager((activity as AppCompatActivity?)!!) {
            override fun authorized(requestCode: Int) {
                LocalMusicPresenter.instance.subscribe(this@LocalMusicFragment)
            }

            override fun noAuthorization(requestCode: Int, lacksPermissions: Array<String>) {
                val builder = AlertDialog.Builder(activity!!)
                builder.setTitle("提示")
                builder.setMessage("缺少读取权限！")
                builder.setPositiveButton("设置权限") { _, _ ->
                    PermissionManager.startAppSettings(
                        context!!
                    )
                }
                builder.create().show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.recheckPermissions(requestCode, permissions, grantResults)
    }

/*    override fun getcontext(): Context {
        return context!!
    }*/



    override fun handleError(error: Throwable) {
        Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
    }

    override fun onLocalMusicLoaded(songs: MutableList<LocalMusic>) {
        if (songs.size == 0) {
            return
        }
        playList.addSong(songs)
        mAdapter.setData(songs)
        mAdapter.notifyDataSetChanged()
        isLoad = true
    }


    override fun onItemClick(view: View?,position: Int) {
        playList.setPlayingIndex(position)
        RxBus.getInstance().post(PlayListEvent(playList, position))
    }

    override fun onClick(v: View) {

    }

    override fun onBackClick(view: View) {
        (activity as MainActivity).onBackPressed()
    }

}
