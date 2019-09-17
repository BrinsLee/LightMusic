package com.brins.lightmusic.ui.fragment.video

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.jzvd.Jzvd
import com.brins.lightmusic.LightMusicApplication
import com.brins.lightmusic.R
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.customview.JZVideoPalyerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class VideoListAdapter(var MvData: MutableList<Mv>, var context: Context) :
    RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    private var mItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_video_list, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return MvData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (MvData.isNotEmpty()) {
            val mv = MvData[position].dataBean
            val metaData = MvData[position].metaDataBean
            holder.title.text = mv.name
            holder.nickName.text = mv.artistName
            holder.watchCount.text = if (mv.playCount > 1000) "${mv.playCount / 1000}万播放" else {
                "${mv.playCount}播放"
            }
            holder.videoPlayer.setAllControlsVisiblity(GONE, GONE, VISIBLE, GONE, VISIBLE, GONE, GONE)
            Glide.with(LightMusicApplication.getLightApplication()).load(mv.cover)
                .into(holder.videoPlayer.thumbImageView)
            holder.videoPlayer.setUp(metaData.url, mv.name, Jzvd.SCREEN_NORMAL)
            holder.videoPlayer.titleTextView.text = ""
            holder.subCount.text = mv.subCount.toString()
            holder.commentCount.text = mv.commentCount.toString()

            if (mItemClickListener != null) {
                holder.videoPlayer.setOnClickListener {
                    mItemClickListener!!.onItemClick(position)
                }
            }
        }

    }

    fun setOnItemListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.tv_title)
        var videoPlayer: JZVideoPalyerView = view.findViewById(R.id.video_player)
        var watchCount: TextView = view.findViewById(R.id.tv_watch_count)
        var subCount: TextView = view.findViewById(R.id.sub)
        var avatar: ImageView = view.findViewById(R.id.iv_avatar)
        var nickName: TextView = view.findViewById(R.id.tv_author)
        var commentCount: TextView = view.findViewById(R.id.tv_comment_count)
    }
}