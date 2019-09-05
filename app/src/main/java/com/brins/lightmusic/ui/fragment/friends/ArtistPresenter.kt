package com.brins.lightmusic.ui.fragment.friends

import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.model.artist.Category
import com.brins.lightmusic.model.artist.CategoryResult
import com.brins.lightmusic.utils.JsonToObject
import com.brins.lightmusic.utils.getJson
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider

class ArtistPresenter private constructor(): ArtistConstract.Presenter{

    val provider: AndroidLifecycleScopeProvider by lazy {
        AndroidLifecycleScopeProvider.from(mView?.getLifeActivity(), Lifecycle.Event.ON_DESTROY)
    }
    private var mView: ArtistConstract.View? = null

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = ArtistPresenter()
    }

    override fun loadArtistCategory() {
        val json = getJson(BaseApplication.getInstance().baseContext,"category.json")
        mView?.onArtistCategoryLoad(JsonToObject(json,CategoryResult::class.java))
    }

    override fun loadArtist() {
    }

    override fun subscribe(view: ArtistConstract.View?) {
        mView = view
        mView!!.setPresenter(this)
    }

    override fun unsubscribe() {
    }

}