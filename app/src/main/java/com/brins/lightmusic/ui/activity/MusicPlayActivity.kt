package com.brins.lightmusic.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.R
import com.brins.lightmusic.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MusicPlayActivity : BaseActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.activity_music_play
    }

    companion object{
        fun startThisActivity(activity: AppCompatActivity) {
            val intent = Intent(activity, MusicPlayActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        super.onCreateAfterBinding(savedInstanceState)
        initView()
    }

    private fun initView() {
        setSupportActionBar(toolbar)
    }
}
