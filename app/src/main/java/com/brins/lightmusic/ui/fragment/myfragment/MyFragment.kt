package com.brins.lightmusic.ui.fragment.myfragment


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.manager.PermissionManager
import com.brins.lightmusic.model.loaclmusic.LocalMusic
import com.brins.lightmusic.ui.activity.login.LoginActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.ListAdapter
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.utils.SpacesItemDecoration
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_localmusic.*

class MyFragment : BaseFragment(), OnItemClickListener, View.OnClickListener {


    lateinit var permissionManager: PermissionManager
    lateinit var mAdapter: ListAdapter<LocalMusic>
    private var isLoad = false

    override fun getLayoutResID(): Int {
        return R.layout.fragment_my
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = ListAdapter(context!!, null)
        setListener()
        recyclerView.setItemViewCacheSize(5)
        recyclerView.adapter = mAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.addItemDecoration(SpacesItemDecoration(10))
    }

    override fun onCreateViewAfterBinding(view: View) {
        super.onCreateViewAfterBinding(view)

    }

    override fun onResume() {
        super.onResume()
        if (AppConfig.isLogin) {
            if (AppConfig.userAccount != null && AppConfig.userProfile != null) {
                Glide.with(this).load(AppConfig.userProfile.avatarUrl).into(avatar)
                nickName.text = AppConfig.userProfile.nickname
            }
        }
    }

    private fun setListener() {
        mAdapter.setOnItemClickListener(this)
        avatar.setOnClickListener(this)
        nickName.setOnClickListener(this)
    }


    override fun onItemClick(position: Int) {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.avatar, R.id.nickName -> LoginActivity.startThisActivity(activity as AppCompatActivity)
        }
    }


    class titleBean {

    }

}
