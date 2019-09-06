package com.brins.lightmusic.event

import com.brins.lightmusic.model.onlinemusic.OnlineMusic

class PlayOnLineMusicEvent(var playlists: MutableList<OnlineMusic>, var position: Int)