package com.brins.lightmusic.ui.fragment.localmusic

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.brins.lightmusic.R
import com.brins.lightmusic.model.LocalMusic
import com.brins.lightmusic.ui.base.adapter.IAdapterView
import kotlinx.android.synthetic.main.item_local_music.view.*

class LocalMusicView @JvmOverloads constructor(context: Context)
    : LinearLayout(context),IAdapterView<LocalMusic> {


    init {
        View.inflate(context, R.layout.item_local_music, this)
    }
    override fun bind(item: LocalMusic, position: Int) {
        textViewName.text = item.title
        textViewArtist.text = item.singer
        imgCover.setImageBitmap(item.cover)
    }
}