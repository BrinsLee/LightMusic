package com.brins.lightmusic.ui.fragment.myfragment


import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.manager.PermissionManager
import com.brins.lightmusic.model.loaclmusic.LocalMusic
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.ListAdapter
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.utils.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragment_localmusic.*

class MyFragment : BaseFragment(), OnItemClickListener, View.OnClickListener {


    lateinit var permissionManager : PermissionManager
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
        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.addItemDecoration(SpacesItemDecoration(10))
    }

    override fun onCreateViewAfterBinding(view: View) {
        super.onCreateViewAfterBinding(view)

    }

    private fun setListener() {
        mAdapter.setOnItemClickListener(this)
        avatar.setOnClickListener(this)
        nickName.setOnClickListener(this)
    }



    override fun onItemClick(position: Int) {

    }

    override fun onClick(v: View) {

    }


    class titleBean {

    }

}
