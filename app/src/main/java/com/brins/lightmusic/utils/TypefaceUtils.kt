package com.brins.lightmusic.utils

import android.content.Context
import android.graphics.Typeface
import androidx.collection.SimpleArrayMap
import java.lang.ref.WeakReference

class TypefaceUtils {
    companion object{
        private val TYPEFACE_CACHE = SimpleArrayMap<String, WeakReference<Typeface>>()
            private fun getTypefacePath(fontType: Int): String? {
                var typefacePath: String? = null
                when (fontType) {
                    1 -> typefacePath = "fonts/DIN-Bold.otf"
                    2 -> typefacePath = "fonts/DIN-Medium.otf"
                    3 -> typefacePath = "fonts/DIN-Regular.otf"
                    4 -> typefacePath = "fonts/LilyScriptOne-Regular.ttf"
                    else ->{}
                }
                return typefacePath
            }

        fun getTypeface(context: Context, fontType: Int): Typeface? {
            val typefacePath = getTypefacePath(fontType)
            var typeface: Typeface? = null
            if (TYPEFACE_CACHE.containsKey(typefacePath)) {
                val typefaceWr = TYPEFACE_CACHE.get(typefacePath)
                if (typefaceWr != null) {
                    typeface = typefaceWr.get()
                }
            }
            if (typeface == null) {
                typeface = Typeface.createFromAsset(context.assets, typefacePath)
                TYPEFACE_CACHE.put(typefacePath, WeakReference(typeface))
            }
            return typeface
        }
        }

}