package com.brins.lightmusic.ui.fragment.localmusic

import android.content.Context
import com.brins.lightmusic.R
import com.brins.lightmusic.model.LocalMusic
import com.brins.lightmusic.ui.base.adapter.AbstractSummaryAdapter

class LocalMusicAdapter(var context: Context ,  data : MutableList<LocalMusic>?) : AbstractSummaryAdapter<LocalMusic, LocalMusicView>(context, data) {
    override fun createView(context: Context?): LocalMusicView {
        return LocalMusicView(context!!)
    }

    override fun getEndSummaryText(dataCount: Int): String {
        return context.getString(R.string.music_summary ,dataCount)
    }
}