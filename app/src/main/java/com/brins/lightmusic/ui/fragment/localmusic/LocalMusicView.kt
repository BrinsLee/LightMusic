package com.brins.lightmusic.ui.fragment.localmusic

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.brins.lightmusic.R
import com.brins.lightmusic.model.LocalMusic
import com.brins.lightmusic.ui.base.adapter.IAdapterView
import com.brins.lightmusic.utils.AlbumUtils.Companion.String2Bitmap
import kotlinx.android.synthetic.main.item_local_music.view.*

class LocalMusicView @JvmOverloads constructor(context: Context)
    : ConstraintLayout(context),IAdapterView<LocalMusic> {


    init {
        View.inflate(context, R.layout.item_local_music, this)
    }
    override fun bind(item: LocalMusic, position: Int) {
        textViewName.text = item.title
        textViewArtist.text = item.singer
        imgCover.setImageBitmap(String2Bitmap(item.cover!!))
    }
}