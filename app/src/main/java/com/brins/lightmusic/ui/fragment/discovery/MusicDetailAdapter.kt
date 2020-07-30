package com.brins.lightmusic.ui.fragment.discovery

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.brins.lightmusic.R
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.utils.GlideHelper.GlideHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.lang.StringBuilder

class MusicDetailAdapter(list: MutableList<Music>) :
    BaseQuickAdapter<Music, BaseViewHolder>(R.layout.item_online_music, list) {


    private var mItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

    override fun convert(helper: BaseViewHolder, item: Music) {
        val strBuilder = StringBuilder()
        item.artistBeans?.forEach { strBuilder.append("${it.name} ") }
        helper.setText(R.id.artist, strBuilder)
        helper.setText(R.id.name, item.name)
        helper.setText(R.id.count, "${helper.layoutPosition}")
        val image = helper.getView<ImageView>(R.id.item_cover)
        GlideHelper.setImageResource(image,item.album.picUrl)

        if (mItemClickListener != null) {
            helper.getView<ConstraintLayout>(R.id.rootLayout).setOnClickListener {
                mItemClickListener!!.onItemClick(it, helper.layoutPosition)
            }

        }
    }

}