package com.brins.lightmusic.ui.fragment.discovery

import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.customview.CommonHeaderView
import com.brins.lightmusic.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_music_detail.*
import kotlinx.android.synthetic.main.fragment_music_detail.*
import kotlinx.android.synthetic.main.fragment_music_detail.coverMusicList
import kotlinx.android.synthetic.main.fragment_music_detail.musicRecycler
import kotlinx.android.synthetic.main.fragment_music_detail.nestScrollView
import kotlinx.android.synthetic.main.fragment_music_detail.toolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicDetailActivity : BaseActivity(), DiscoveryContract.View,
    OnItemClickListener,
    CommonHeaderView.OnBackClickListener {

    @Inject
    lateinit var mPresenter: DiscoverPresenter
    var id: String = ""
    lateinit var mAdapter: MusicDetailAdapter
    private var playList: PlayList = PlayList()
    private var currentTime: Long = 0

    companion object {
        fun startThis(activity: AppCompatActivity, TAG: String, id: String) {
            val bundle = Intent(activity, MusicDetailActivity::class.java)
            bundle.putExtra(TAG, id)
            activity.startActivity(bundle)
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_music_detail
    }

    override fun initInject() {
        getActivityComponent().inject(this)
    }

    override fun handleError(error: Throwable) {
    }

    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        super.onCreateAfterBinding(savedInstanceState)
        setTranslucent(this)
        mAdapter = MusicDetailAdapter(playList.getSongs())
        mAdapter.animationEnable = true
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInLeft)
        mAdapter.setOnItemClickListener(this)
        toolbar.setOnBackClickListener(this)
        nestScrollView.fadingView = toolbar
        nestScrollView.fadingHeightView = coverMusicList
        DiscoverPresenter.instance.subscribe(this)
        if (intent != null) {
            id = intent.getStringExtra("DiscoveryFragment")
            if (id === "") {
                id = intent.getStringExtra("ArtistTabFragment")
                loadAlbumDetail()
            } else {
                loadMusicListDetail()
            }
            musicRecycler.adapter = mAdapter
            musicRecycler.layoutManager = LinearLayoutManager(this)
            musicRecycler.addItemDecoration(
                SpacesItemDecoration(this, 2, R.color.gery)
            )
        }
    }


    private fun loadMusicListDetail() {
        launch({
            showLoading()
            val musicData = getMusicListData()
            val detailBean = musicData.playlist
            if (detailBean != null) {
                ImageLoadreUtils.getInstance().loadImage(
                    this,
                    ImageLoader.Builder().url(detailBean.coverImgUrl).assignWidth(640)
                        .assignHeight(480)
                        .bulid(), object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            /*val result = rsBlur(this@MusicDetailActivity, resource, 25f)
                            coverBg.setImageBitmap(result)*/
                            coverMusicList.setImageBitmap(resource)
                            cover.setImageBitmap(resource)
                            Palette.from(resource).generate {
                                it?.let {
                                    when {
                                        it.getDarkVibrantColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                                            coverBg.setImageBitmap(
                                                createLinearGradientBitmap(
                                                    coverBg,
                                                    1,
                                                    it.getDarkVibrantColor(Color.TRANSPARENT),
                                                    it.getVibrantColor(Color.TRANSPARENT)
                                                )
                                            )
                                            nestScrollView.setDrawable(
                                                createLinearGradientBitmap(
                                                    toolbar,
                                                    0,
                                                    it.getDarkVibrantColor(Color.TRANSPARENT),
                                                    it.getVibrantColor(Color.TRANSPARENT)
                                                )
                                            )
                                        }


                                        it.getDarkMutedColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                                            coverBg.setImageBitmap(
                                                createLinearGradientBitmap(
                                                    coverBg,
                                                    1,
                                                    it.getDarkMutedColor(Color.TRANSPARENT),
                                                    it.getMutedColor(Color.TRANSPARENT)
                                                )
                                            )
                                            nestScrollView.setDrawable(
                                                createLinearGradientBitmap(
                                                    toolbar,
                                                    0,
                                                    it.getDarkMutedColor(Color.TRANSPARENT),
                                                    it.getMutedColor(Color.TRANSPARENT)
                                                )
                                            )
                                        }

                                        else -> {
                                            coverBg.setImageBitmap(
                                                createLinearGradientBitmap(
                                                    coverBg,
                                                    1,
                                                    it.getLightMutedColor(Color.TRANSPARENT),
                                                    it.getLightVibrantColor(Color.TRANSPARENT)
                                                )
                                            )

                                            nestScrollView.setDrawable(
                                                createLinearGradientBitmap(
                                                    toolbar,
                                                    0,
                                                    it.getLightMutedColor(Color.TRANSPARENT),
                                                    it.getLightVibrantColor(Color.TRANSPARENT)
                                                )
                                            )

                                        }

                                    }
//                                    coverBg.setImageBitmap(handleBimap(resource))
                                }

                            }
                        }

                    }
                )
                toolbar.title = detailBean.name
                description.text = detailBean.description
                playCount.text = handleNum(detailBean.playCount)
                shareCount.text = handleNum(detailBean.shareCount)
                subscriptCount.text = handleNum(detailBean.subscribedCount)
                playList.addSong(detailBean.tracks!!)
                mAdapter.addData(playList.getSongs())
                hideLoading()
            }

        }, {})
    }

    private fun handleNum(num: Int): String {
        return if (num > 10000) {
            "${num / 10000}万"
        } else num.toString()
    }

    private fun handleBimap(bitmap: Bitmap): Bitmap {
        //透明渐变
        val argb = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(argb, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        //循环开始的下标，设置从什么时候开始改变
        val start = argb.size / 2
        val end = argb.size

        //        int mid = argb.length;
        //        int row = ((mid - start) / bitmap.getHeight()) + 2;


        val width = bitmap.width
        for (i in 0 until bitmap.height / 2 + 1) {
            for (j in 0 until width) {
                val index = start - width + i * width + j
                if (argb[index] != 0) {
                    argb[index] =
                        ((1 - i / (bitmap.height / 2f)) * 255).toInt() shl 24 or (argb[index] and 0x00FFFFFF)
                }
            }
        }
        //        for (int i = mid; i < argb.length; i++) {
        //            argb[i] = (argb[i] & 0x00FFFFFF);
        //        }

        return Bitmap.createBitmap(argb, bitmap.width, bitmap.height, Bitmap.Config.RGB_565)

    }

    private fun createLinearGradientBitmap(
        v: View,
        orientataion: Int,
        darkColor: Int,
        color: Int
    ): Bitmap {
        val bgColors = IntArray(3)
        bgColors[0] = setAlpha(0.2f, color)
        bgColors[1] = setAlpha(0.5f, color)
        bgColors[2] = color

        val bgBitmap = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas()
        val paint = Paint()
        canvas.setBitmap(bgBitmap)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        val gradient = if (orientataion == 0) LinearGradient(
            bgBitmap.width.toFloat(),
            0f,
            0f,
            0f,
            bgColors,
            null,
            Shader.TileMode.CLAMP
        ) else LinearGradient(
            0f,
            0f,
            0f,
            bgBitmap.height.toFloat(),
            bgColors,
            null,
            Shader.TileMode.CLAMP
        )
        paint.shader = gradient
        paint.isAntiAlias = true
        val rectF = RectF(0f, 0f, bgBitmap.width.toFloat(), bgBitmap.height.toFloat())
        canvas.drawRoundRect(rectF, 20f, 20f, paint)
        canvas.drawRect(rectF, paint)
        return bgBitmap
    }

    fun setAlpha(fraction: Float, color: Int): Int {
        val r = color shr 16 and 0xff
        val g = color shr 8 and 0xff
        val b = color and 0xff

        return (256 * fraction).toInt() shl 24 or (r shl 16) or (g shl 8) or b
    }

    private fun loadAlbumDetail() {
        launch({
            showLoading()
            val musics = getAlbumData()
            Glide.with(this)
                .load(musics.album!!.picUrl)
                .into(coverMusicList)
            toolbar.title = musics.album!!.name
            playList.addSong(musics.songs!!)
            mAdapter.setNewData(playList.getSongs())
            hideLoading()
        }, {
            val i = it
            Toast.makeText(this, i.message, Toast.LENGTH_SHORT).show()
            hideLoading()

        })
    }


    private suspend fun getMusicListData() = withContext(Dispatchers.IO) {
        val musicList = mPresenter.loadMusicListDetail(id)
        musicList
    }

    private suspend fun getAlbumData() = withContext(Dispatchers.IO) {
        val albumList = mPresenter.loadAlbumDetail(id)
        albumList
    }


    override fun onItemClick(view: View?, position: Int) {

        if (System.currentTimeMillis() - currentTime < 2000) {
            return
        }
        currentTime = System.currentTimeMillis()
        playList.setPlayingIndex(position)
        RxBus.getInstance().post(PlayListEvent(playList, position, TYPE_ONLINE_MUSIC))
    }

    override fun onBackClick(view: View) {
        finish()
    }

}
