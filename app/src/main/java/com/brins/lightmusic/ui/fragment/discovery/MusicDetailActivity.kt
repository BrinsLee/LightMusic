package com.brins.lightmusic.ui.fragment.discovery

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.utils.RxBus
import com.brins.lightmusic.common.AppConfig.KEY_ID
import com.brins.lightmusic.common.AppConfig.TRANSITION_NAME
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.widget.CommonHeaderView
import com.brins.lightmusic.utils.*
import com.brins.lightmusic.utils.GlideHelper.GlideHelper
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_music_detail.*
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
        fun startThis(activity: AppCompatActivity, id: String) {
            val bundle = Intent(activity, MusicDetailActivity::class.java)
            bundle.putExtra(KEY_ID, id)
            activity.startActivity(bundle)
        }

        fun startThis(
            activity: AppCompatActivity,
            options: ActivityOptionsCompat,
            transitionName: String, id: String
        ) {
            val intent = Intent(activity, MusicDetailActivity::class.java)
            intent.putExtra(KEY_ID, id)
            intent.putExtra(TRANSITION_NAME, transitionName)
            activity.startActivity(intent, options.toBundle())

        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_music_detail
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
            id = intent.getStringExtra(KEY_ID)
            loadMusicListDetail()
            val name = intent.getStringExtra(TRANSITION_NAME)
            name?.let {
                postponeEnterTransition()
                ViewCompat.setTransitionName(coverMusicList, it)
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
                GlideHelper.setImageResource(
                    coverMusicList,
                    detailBean.coverImgUrl,
                    object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            startPostponedEnterTransition()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            coverMusicList.setImageDrawable(resource)
                            startPostponedEnterTransition()
                            cover.setImageDrawable(resource)
                            Palette.from((resource as BitmapDrawable).bitmap).generate {
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
                            return true
                        }
                    })
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


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe()
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


    private suspend fun getMusicListData() = withContext(Dispatchers.IO) {
        val musicList = mPresenter.loadMusicListDetail(id)
        musicList
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
