package com.brins.lightmusic.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.R
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.player.IPlayback
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.fragment.quickcontrol.MusicPlayerContract
import com.brins.lightmusic.utils.AlbumUtils.Companion.String2Bitmap
import com.brins.lightmusic.utils.FastBlurUtil
import com.brins.lightmusic.utils.HandlerUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_music_play.*
import kotlinx.android.synthetic.main.fragment_quick_control.*
import kotlinx.android.synthetic.main.include_play_control.*

class MusicPlayActivity : BaseActivity(),View.OnClickListener{

    private var mPlayer : IPlayback? = null
    private var isPlaying  = false
    private lateinit var mPresenter: MusicPlayerContract.Presenter
    private val mHamdler : HandlerUtil by lazy {HandlerUtil.getInstance(this)}
    private val mUpAlbumRunnable = Runnable { SetBlurredAlbumArt().execute() }
    private var current : Music? = null
    private var cover : Bitmap? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_music_play
    }

    companion object{
        @JvmStatic private val PLAYLIST = "playlist"
        @JvmStatic private var PLAYINDEX = "playindex"
        fun startThisActivity(activity: AppCompatActivity , isPlaying : Boolean) {
            val intent = Intent(activity, MusicPlayActivity::class.java)
            intent.putExtra(PLAYINDEX,isPlaying)
            activity.startActivity(intent)
        }
    }

    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        super.onCreateAfterBinding(savedInstanceState)
        showBottomBar(false)
        isPlaying = intent.getBooleanExtra(PLAYINDEX , false)
        setSupportActionBar(toolBar)
        setPlayView()
    }

    private fun setPlayView() {
        current = getCurrentMusic()
        if (current != null){
            cover = String2Bitmap(current!!.cover!!)
            initView()
            setListener()
        }
    }

    private fun initView() {
        if (isPlaying){
            playOrPause.setImageResource(R.drawable.ic_pausemusic)

        } else {
            playOrPause.setImageResource(R.drawable.ic_playmusic)
            playAnimate.pauseAnimation()
        }
        supportActionBar?.title = current!!.title
        ivCover.setImageBitmap(cover)
        mHamdler.postDelayed(mUpAlbumRunnable, 200)

    }

    //click event

    fun setListener(){
        playOrPause.setOnClickListener(this)
        preSong.setOnClickListener(this)
        nextSong.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            playOrPause -> {
                onPlayPauseToggle()
            }
            nextSong -> {
                PlayBackControll(COMMAND_NEXT)
                setPlayView()
            }
            preSong -> {
                PlayBackControll(COMMAND_LAST)
                setPlayView()
            }
        }
    }

    fun onPlayPauseToggle() {
        PlayBackControll(COMMAND_PLAY_PAUSE)
        if (isPlaying) {
            playAnimate.pauseAnimation()
            playOrPause.setImageResource(R.drawable.ic_playmusic)
            isPlaying = false
        } else {
            playAnimate.playAnimation()
            playOrPause.setImageResource(R.drawable.ic_pausemusic)
            isPlaying = true
        }
    }



    inner class SetBlurredAlbumArt : AsyncTask<Void, Void, Drawable>(){
        override fun doInBackground(vararg params: Void?): Drawable {
            return getForegroundDrawable(cover!!)
        }

        override fun onPostExecute(result: Drawable?) {
            setDrawable(result)
        }
    }

    private fun setDrawable(result: Drawable?) {
        if (result != null){
            if (rootLayout.background != null){
                val td = TransitionDrawable(arrayOf(rootLayout.background, result))
                rootLayout.background = td
                td.isCrossFadeEnabled = true
                td.startTransition(200)
            } else{ rootLayout.background = result }
        }
    }

    fun getForegroundDrawable(bitmap : Bitmap): Drawable {
        val widthHeightSize = (AppConfig.DisplayUtil.getScreenWidth(this@MusicPlayActivity) * 1.0 / AppConfig.DisplayUtil.getScreenHeight(this) * 1.0).toFloat()
        val cropBitmapWidth = (widthHeightSize * bitmap.height).toInt()
        val cropBitmapWidthX = ((bitmap.width - cropBitmapWidth) / 2.0).toInt()

        val cropBitmap = Bitmap.createBitmap(bitmap, cropBitmapWidthX, 0, cropBitmapWidth,
            bitmap.height)
        val scaleBitmap = Bitmap.createScaledBitmap(cropBitmap, bitmap.width / 50, bitmap
            .height / 50, false)
        val blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 8, true)
        val foregroundDrawable = BitmapDrawable(resources,blurBitmap)
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
        return foregroundDrawable

    }
}
