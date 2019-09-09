package com.brins.lightmusic.ui.fragment.localmusic

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.model.artist.Album
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.model.loaclmusic.LocalMusic
import com.brins.lightmusic.utils.loadingCover
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList


class LocalMusicPresenter private constructor() : LocalMusicContract.Presenter
    , LoaderManager.LoaderCallbacks<Cursor> {


    private var mView: LocalMusicContract.View? = null

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = LocalMusicPresenter()
    }

    val mSubscriptions: CompositeDisposable = CompositeDisposable()
    private val TAG = "LocalMusicPresenter"
    private var URL_LOAD_LOCAL_MUSIC = 0
    private val MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    private val WHERE = (MediaStore.Audio.Media.DURATION + "> ?" + "AND "
            + MediaStore.Audio.Media.SIZE + ">?")
    private val ORDER_BY = MediaStore.Audio.Media.DISPLAY_NAME + " ASC"
    val FILTER_SIZE = 1 * 1024 * 1024// 1MB
    val FILTER_DURATION = 1 * 60 * 1000// 1分钟
    val projectLocal = arrayOf(
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.SIZE,
        MediaStore.Audio.Media.DATA
    )

    init {
        mView?.setPresenter(this)
    }

    override fun loadLocalMusic() {
        mView?.getLoaderManager()!!.initLoader(URL_LOAD_LOCAL_MUSIC, null, this)
    }

    override fun subscribe(view: LocalMusicContract.View?) {
        mView = view
        loadLocalMusic()
    }

    override fun unsubscribe() {
        mView = null
        mSubscriptions.clear()
    }

    private fun getCursor(): Cursor? {
        return BaseApplication.getInstance().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projectLocal,
            MediaStore.Audio.Media.SIZE + "> ?"
                    + "and " + MediaStore.Audio.Media.DURATION + "> ?",
            arrayOf("$FILTER_SIZE", "$FILTER_DURATION"), null
        )

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        var loader = CursorLoader(
            BaseApplication.getInstance().applicationContext,
            MEDIA_URI,
            projectLocal,
            MediaStore.Audio.Media.IS_MUSIC + "=1 AND "
                    + MediaStore.Audio.Media.SIZE + ">0",
            null,
            ORDER_BY
        )
        return loader
    }

    @SuppressLint("CheckResult")
    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        val songs = ArrayList<LocalMusic>()
        val flow: Flowable<ArrayList<LocalMusic>> = Flowable.create(
            {
                Log.d(TAG, "${cursor?.moveToNext()}")
                if (cursor != null && cursor.count > 0) {
                    cursor.moveToFirst()
                    do {
                        var song = cursorToMusic(cursor)
                        songs.add(song)
                    } while (cursor.moveToNext())
                    it.onNext(songs)
                }
            }, BackpressureStrategy.BUFFER
        )
        val disposable = flow.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                mView!!.onLocalMusicLoaded(it)
            }, {
                Log.e(TAG, it.message)
            })
        mSubscriptions.add(disposable)

    }

    private fun createObservable(songs: MutableList<LocalMusic>): Observable<MutableList<LocalMusic>> {
        return Observable.create { emitter ->
            emitter.onNext(songs)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    private fun cursorToMusic(cursor: Cursor): LocalMusic {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
        val duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
        val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
        val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
        val size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
        val url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)) ?: ""
        val artistBean = ArtistBean()
        artistBean.name = artist
        val bitmap = if (url.isNotEmpty()) loadingCover(url) else ""
        val albums = Album()
        albums.name = album
        albums.picUrl = bitmap
        val song = LocalMusic(
            id,
            name,
            title,
            listOf(artistBean),
            size,
            url,
            albums,
            duration = duration
        )
        Log.d(TAG,bitmap)
        return song
    }


}