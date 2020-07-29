package com.brins.lightmusic.ui.customview

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.model.onlinemusic.MusicCommentResult
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.utils.GlideHelper.GlideHelper
import com.brins.lightmusic.utils.TimeUtils
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.comment_poup_layout.view.*

class CommentPopup(context: Context, var lists: List<MusicCommentResult.Companion.HotComments>) :
    BottomPopupView(context) {

    private lateinit var mCommentAdater: CommentAdater


    override fun getImplLayoutId(): Int {
        return R.layout.comment_poup_layout
    }

    override fun onCreate() {
        super.onCreate()
        recomment_total_tip.text = "${lists.size}条评论"

        mCommentAdater = CommentAdater(
            context, R.layout.adapter_comment_item,
            lists as ArrayList<MusicCommentResult.Companion.HotComments>
        )
        comment_recyclerview.adapter = mCommentAdater
        comment_recyclerview.setHasFixedSize(true)
        comment_recyclerview.layoutManager = LinearLayoutManager(context)
    }


    class CommentAdater(
        context: Context,
        layout: Int,
        list: ArrayList<MusicCommentResult.Companion.HotComments>
    ) : CommonViewAdapter<MusicCommentResult.Companion.HotComments>(context, layout, list) {
        override fun converted(
            holder: ViewHolder,
            t: MusicCommentResult.Companion.HotComments,
            position: Int
        ) {
            val avatarIV = holder.getView(R.id.user_avatar_iv) as ImageView
            GlideHelper.setCircleImageResource(avatarIV, t.user?.avatarUrl)

            holder.setText(R.id.user_name_tv, t.user!!.nickname)
            holder.setText(R.id.recommen_content_tv, t.content)
            if (TimeUtils.getDaysAgoByMills(t.time) > 1) {
                holder.setText(
                    R.id.reply_time_tv,
                    "${TimeUtils.getDaysAgoByMills(t.time)}天前"
                )
            } else {
                if (TimeUtils.isToday(t.time)) {
                    holder.setText(
                        R.id.reply_time_tv,
                        TimeUtils.getFormatHHMM(t.time)
                    )
                } else {
                    holder.setText(
                        R.id.reply_time_tv,
                        context.getString(R.string.comment_yestoday_tip)
                    )
                }
            }

        }

    }
}