package com.brins.lightmusic.utils


class TimeUtils {

    companion object{
        @JvmStatic
        fun formatDuration(duration: Int): String{
            val durationSecond = duration / 1000
            var minute = durationSecond / 60
            val hour = minute / 60
            minute %= 60
            val second = durationSecond % 60
            return if (hour != 0)
                String.format("%2d:%02d:%02d", hour, minute, second)
            else
                String.format("%02d:%02d", minute, second)


        }
    }
}
