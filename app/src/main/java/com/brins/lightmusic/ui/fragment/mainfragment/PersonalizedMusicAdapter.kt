package com.brins.lightmusic.ui.fragment.mainfragment

import com.brins.lightmusic.R
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.personal.PersonalizedMusic
import com.brins.lightmusic.ui.customview.CornersTransform
import com.brins.lightmusic.utils.ImageLoader
import com.brins.lightmusic.utils.ImageLoadreUtils
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
        ImageLoadreUtils.getInstance().loadImage(
            context, ImageLoader.Builder()
                .assignHeight(500).assignWidth(500).bitmapTransformation(
                    CornersTransform(20f)
                ).url(item.picUrl).imgView(
                    helper.getView(R.id.cover)
                ).scaleModeType(
                    ImageLoadreUtils.SCALE_MODE_CENTER_CROP
                ).bulid()
        )
    }


}