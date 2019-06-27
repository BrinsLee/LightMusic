package com.brins.lightmusic.player

enum class PlayMode {

    SINGLE,
    LOOP,
    LIST,
    SHUFFLE;

    companion object {
        @JvmStatic
        fun getDefault(): PlayMode {
            return LOOP
        }

        @JvmStatic
        fun switchNextMode(current : PlayMode?) : PlayMode{
            if (current == null){
                return getDefault()
            }
            return when(current){
                LOOP -> LIST
                LIST -> SHUFFLE
                SHUFFLE -> SINGLE
                SINGLE -> LOOP
            }
        }
    }
}