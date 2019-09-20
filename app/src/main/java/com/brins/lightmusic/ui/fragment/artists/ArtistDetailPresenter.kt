package com.brins.lightmusic.ui.fragment.artists

import androidx.lifecycle.Lifecycle
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider

class ArtistDetailPresenter : ArtistDetailConstract.Presenter {

    val provider: AndroidLifecycleScopeProvider by lazy {
        AndroidLifecycleScopeProvider.from(mView?.getLifeActivity(), Lifecycle.Event.ON_DESTROY)
    }
    private var mView: ArtistConstract.View? = null

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = ArtistDetailPresenter()
    }

    override fun loadArtistInfo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadArtistSong(id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadArtistAlbum(id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadArtistMv(id: String, limit: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun subscribe(view: ArtistDetailConstract.View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unsubscribe() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}