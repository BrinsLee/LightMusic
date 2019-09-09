package com.brins.lightmusic.event

import com.brins.lightmusic.model.loaclmusic.PlayList

class PlayListEvent (var playlist : PlayList, var playIndex : Int, var type : Int = 0)