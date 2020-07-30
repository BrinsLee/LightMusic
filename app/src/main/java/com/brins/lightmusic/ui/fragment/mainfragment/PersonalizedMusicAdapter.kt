package com.brins.lightmusic.ui.fragment.mainfragment

import android.app.Activity
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.brins.lightmusic.R
import com.brins.lightmusic.model.personal.PersonalizedMusic
import com.brins.lightmusic.ui.fragment.discovery.MusicDetailActivity
import com.brins.lightmusic.utils.GlideHelper.GlideHelper
import com.brins.lightmusic.utils.handleNum
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author lipeilin
 * @date 2020/7/22
 */
class PersonalizedMusicAdapter(data: ArrayList<PersonalizedMusic>) :
    BaseQuickAdapter<PersonalizedMusic, BaseViewHolder>(
        R.layout.item_personalized_music, data
    ) {


    override fun convert(helper: BaseViewHolder, item: PersonalizedMusic) {
        helper.setText(R.id.name, item.name)
        helper.setText(R.id.playCount, "${handleNum(item.playCount)}")
        val image: ImageView = helper.getView(R.id.cover)
        ViewCompat.setTransitionName(image, item.picUrl)
        GlideHelper.setRoundImageResource(image, item.picUrl, 10)

        helper.getView<ConstraintLayout>(R.id.rootLayout).setOnClickListener {

            val options: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    image,
                    ViewCompat.getTransitionName(image)!!
                )
            MusicDetailActivity.startThis(
                context as AppCompatActivity,
                options,
                image.transitionName,
                item.id
            )
        }
    }


}