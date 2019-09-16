package com.brins.lightmusic.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.model.userplaylist.UserPlayListBean
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.utils.SpacesItemDecoration
import kotlinx.android.synthetic.main.item_recycler_head.view.*
import kotlinx.android.synthetic.main.view_my_recycler.view.*

class UserPlayListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    View.OnClickListener {

    override fun onClick(v: View?) {
        isExpend = !isExpend
        if (isExpend) {
            status.setImageResource(R.drawable.ic_chevron_down)
            recyclerView.visibility = View.VISIBLE

        } else {
            status.setImageResource(R.drawable.ic_chevron_right)
            recyclerView.visibility = View.GONE
        }
    }

    private var isExpend = false

    init {
        LayoutInflater.from(context).inflate(R.layout.view_my_recycler, this)
        initView()
    }

    private fun initView() {

        recyclerView.setItemViewCacheSize(5)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(SpacesItemDecoration(10))
        setListener()
    }

    fun setAdapter(adapter: CommonViewAdapter<UserPlayListBean>) {
        val animator = DefaultAnimator()
        animator.addDuration = 1000
        animator.removeDuration = 1000
        recyclerView.itemAnimator = animator
        recyclerView.adapter = adapter
    }

    private fun setListener() {
        rootli.setOnClickListener(this)
    }

    fun setListener(onItenClick : (item : UserPlayListBean) -> Unit){
        recyclerView.setOnClickListener{

        }
    }
}