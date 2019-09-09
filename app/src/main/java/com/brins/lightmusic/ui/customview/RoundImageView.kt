package com.brins.lightmusic.ui.customview


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.Bitmap.Config
import android.graphics.PorterDuff.Mode
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.NinePatchDrawable
import android.util.AttributeSet
import android.widget.ImageView

@SuppressLint("AppCompatCustomView")
class RoundImageView : ImageView {
    private var a = 0
    private var b = 0

    constructor(var1: Context) : super(var1) {}

    constructor(var1: Context, var2: AttributeSet) : super(var1, var2) {}

    constructor(var1: Context, var2: AttributeSet, var3: Int) : super(var1, var2, var3) {}

    override fun onDraw(var1: Canvas) {
        val var2 = this.drawable
        if (var2 != null) {
            if (this.width != 0 && this.height != 0) {
                this.measure(0, 0)
                if (var2.javaClass != NinePatchDrawable::class.java) {
                    val var3 = (var2 as BitmapDrawable).bitmap
                    if (var3 != null) {
                        var var4: Bitmap? = null

                        try {
                            var4 = var3.copy(Config.ARGB_8888, true)
                        } catch (var9: Throwable) {
                        }

                        if (var4 == null) {
                            super.onDraw(var1)
                        } else {
                            if (this.a == 0) {
                                this.a = this.width
                            }

                            if (this.b == 0) {
                                this.b = this.height
                            }

                            val var5 = (if (this.a < this.b) this.a else this.b) / 2
                            var var6: Bitmap? = null

                            try {
                                var6 = this.a(var4, var5)
                            } catch (var8: Throwable) {
                            }

                            if (var6 == null) {
                                super.onDraw(var1)
                            } else {
                                var1.drawBitmap(
                                    var6,
                                    (this.a / 2 - var5).toFloat(),
                                    (this.b / 2 - var5).toFloat(),
                                    null as Paint?
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun a(var1: Bitmap, var2: Int): Bitmap {
        var var1: Bitmap? = var1
        val var4 = var2 * 2
        val var5 = var1!!.width
        val var6 = var1.height
        var var11: Bitmap? = null
        if (var6 > var5) {
            val var9: Byte = 0
            val var10 = (var6 - var5) / 2
            var11 = Bitmap.createBitmap(var1, var9.toInt(), var10, var5, var5)
        } else if (var6 < var5) {
            val var16 = (var5 - var6) / 2
            val var17: Byte = 0
            var11 = Bitmap.createBitmap(var1, var16, var17.toInt(), var6, var6)
        }

        if (var11 == null) {
            var11 = var1
        }

        var var3: Bitmap?
        if (var11.width == var4 && var11.height == var4) {
            var3 = var11
        } else {
            var3 = Bitmap.createScaledBitmap(var11, var4, var4, true)
        }

        val var12 = Bitmap.createBitmap(var3!!.width, var3.height, Config.ARGB_8888)
        val var13 = Canvas(var12)
        val var14 = Paint()
        val var15 = Rect(0, 0, var3.width, var3.height)
        var14.isAntiAlias = true
        var14.isFilterBitmap = true
        var14.isDither = true
        var13.drawARGB(0, 0, 0, 0)
        var13.drawCircle(
            (var3.width / 2).toFloat(),
            (var3.height / 2).toFloat(),
            (var3.width / 2).toFloat(),
            var14
        )
        var14.xfermode = PorterDuffXfermode(Mode.SRC_IN)
        var13.drawBitmap(var3, var15, var15, var14)
        var1 = null
        var11 = null
        var3 = null
        return var12
    }
}
