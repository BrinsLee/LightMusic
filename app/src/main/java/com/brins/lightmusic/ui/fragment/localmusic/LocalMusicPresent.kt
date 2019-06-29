package com.brins.lightmusic.ui.fragment.localmusic

import android.annotation.SuppressLint
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.R
import com.brins.lightmusic.model.LocalMusic
import com.brins.lightmusic.utils.AlbumUtils.Companion.loadingCover
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList


class LocalMusicPresent(var mView : LocalMusicContract.View? ) : LocalMusicContract.Presenter
    , LoaderManager.LoaderCallbacks<Cursor>{

    val mSubscriptions : CompositeDisposable = CompositeDisposable()
    private val TAG = "LocalMusicPresenter"
    private val URL_LOAD_LOCAL_MUSIC = 0
    private val MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    private val WHERE = (MediaStore.Audio.Media.DURATION + "> ?"+ "AND "
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
        MediaStore.Audio.Media.DATA)

    init {
        mView?.setPresenter(this)
    }

    override fun loadLocalMusic() {
        mView?.getLoaderManager()!!.initLoader(URL_LOAD_LOCAL_MUSIC, null, this)
    }

    override fun subscribe() {
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
            arrayOf("$FILTER_SIZE", "$FILTER_DURATION"),null
        )

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        var loader = CursorLoader(
            mView!!.getcontext(),
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
        val flow : Flowable<ArrayList<LocalMusic>> = Flowable.create(
            {
                Log.d(TAG,"${cursor?.moveToNext()}")
                if (!cursor!!.moveToNext()){
                    it.onNext(songs)
                }else{
                    do {
                        var song = cursorToMusic(cursor)
                        songs.add(song)
                    }while (cursor.moveToNext())
                }
                it.onNext(songs)
            }, BackpressureStrategy.BUFFER
        )
        val disposable =  flow.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                mView!!.onLocalMusicLoaded(it)
            }, {
                Log.e(TAG ,it.message)
            })
        mSubscriptions.add(disposable)

    }

    private fun createObservable(songs: MutableList<LocalMusic>) : Observable<MutableList<LocalMusic>> {
        return Observable.create {
                emitter -> emitter.onNext(songs)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    private fun cursorToMusic(cursor: Cursor): LocalMusic{
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
        val duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
        val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
        val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
        val size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
        val url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))?:""
        val song = LocalMusic(id,name,title,artist,duration,size,url,album)
        if (url.isNotEmpty()){
            val bitmap = loadingCover(url)
            song.cover = bitmap
        }
        return song
    }


}