package com.brins.lightmusic.common;

import android.content.Context;

import com.brins.lightmusic.model.userlogin.UserAccountBean;
import com.brins.lightmusic.model.userlogin.UserProfileBean;

import org.jetbrains.annotations.Nullable;

public class AppConfig {
    public static boolean isLogin = false;
    public static final String BASEURL = "https://fc827649.ngrok.io";


    /**
     * 登录
     */
    public static final String LOGIN_EMAIL = "/login";
    public static final String USER = "/user/detail";
    public static final String DEFAULT_ID = "490583871";

    public static final String LOGOUT = "/logout";


    /**
     * 用户歌单
     */
    public static final String USER_PLAYLIST = "/user/playlist";

    /**
     * 私人FM
     */
    public static final String USER_FM = "/personal_fm";

    /**
     * 每日推荐
     */
    public static final String DAILY_RECOMMEND= "/recommend/songs";


    /**
     * 轮播图
     */
    public static final String BANNER = "/banner";


    /**
     * 心动模式
     */
    public static final String INTELLIGENCE = "/playmode/intelligence/list";

    /**
     * 歌单
     */
    public static final String PLAYLIST = "/top/playlist";
    public static final String PLAYLISTDETAIL = "/playlist/detail";

    /**
     * 精选歌单
     */
    public static final String MUSICLIST_HIGHQUALITY = "/top/playlist/highquality";

    /**
     * 歌手
     */
    public static final String ARTISTS = "/top/artists";

    /**
     * 歌手描述
     */
    public static final String ARTISTDESC = "/artist/desc?id=6452";

    /**
     * 歌手分类
     */
    public static final String ARTISTS_CATEGORY = "/artist/list";


    /**
     * 歌手音乐
     */
    public static final String ARTISTS_MUSIC = "artists";

    /**
     * 歌手MV
     */
    public static final String ARTISTS_MV = "artist/mv";

    /**
     * 歌手专辑
     */
    public static final String ARTIST_ALBUM = "/artist/album";

    public static final String ALBUM_DETAIL = "/album";
    /**
     * 音乐链接
     */
    public static final String SONG = "/song/url";


    /**
     * 最新MV
     */
    public static final String LASTESTMUSICVIDEO = "/mv/first";

    /**
     * Mv播放地址
     */
    public static final String MVURL = "/mv/url";

    /**
     * Mv详情
     */
    public static final String MVDETAIL = "/mv/detail";

    /**
     * Mv评论
     */
    public static final String MVCOMMENTS = "/comment/mv";

    /**
     * 所有MV
     */
    public static final String MVALL = "/mv/all";
    /**
     * 音乐封面
     */
    public static final String ALBUM = "/song/detail";

    /**
     * 搜索建议
     * 必选参数 : keywords : 关键词
     * 可选参数 : type : 如果传 'mobile' 则返回移动端数据
     */
    public static final String SEARCH_SUGGEST = "/search/suggest";

    public static final String ALIPAY = "fkx00199c1ypi0c9bzss8a8";

    /**
     * 搜索
     * 必选参数 : keywords : 关键词
     *
     * 可选参数 : limit : 返回数量 , 默认为 30 offset : 偏移数量，用于分页 , 如 : 如 :( 页数 -1)*30, 其中 30 为 limit 的值 , 默认为 0
     *
     * type: 搜索类型；默认为 1 即单曲 , 取值意义 : 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单, 1002: 用户, 1004: MV, 1006: 歌词, 1009: 电台, 1014: 视频, 1018:综合
     */
    public static final String SEARCH = "/search";


    public static final String RECOMMEND_MV = "/personalized/mv";

    public static final String VIDEO = "/video/url";
    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

    public static String USERNAME = "";
    public static String PASSWORD = "";

    public static final String ACTION_PRV = "com.brins.lightmusic.ACTION.PLAY_LAST";
    public static final String ACTION_NEXT = "com.brins.lightmusic.ACTION.PLAY_NEXT";
    public static final String ACTION_PAUSE = "com.brins.lightmusic.ACTION.PAUSE";
    public static final String ACTION_PLAY = "com.brins.lightmusic.ACTION.PLAY";

    public static final int CODE_MAIN = 0;
    public static final int CODE_CLOSE = 1;
    public static final int CODE_PRV = 2;
    public static final int CODE_PAUSE = 3;
    public static final int CODE_PLAY = 4;
    public static final int CODE_NEXT = 5;


    /**
     * 包名相关常量
     */
    public static class Package {

        /**
         * 包名
         */
        public static final String PACKAGE_NAME = "com.brins.lightmusic";

        /**
         * 主进程名
         */
        public static final String MAIN_PROCESS_NAME = PACKAGE_NAME;

    }

    public static class DisplayUtil {

        /*手柄起始角度*/
        public static final float ROTATION_INIT_NEEDLE = -30;

        /*截图屏幕宽高*/
        private static final float BASE_SCREEN_WIDTH = (float) 1080.0;
        private static final float BASE_SCREEN_HEIGHT = (float) 1920.0;

        /*唱针宽高、距离等比例*/
        public static final float SCALE_NEEDLE_WIDTH = (float) (276.0 / BASE_SCREEN_WIDTH);
        public static final float SCALE_NEEDLE_MARGIN_LEFT = (float) (500.0 / BASE_SCREEN_WIDTH);
        public static final float SCALE_NEEDLE_PIVOT_X = (float) (43.0 / BASE_SCREEN_WIDTH);
        public static final float SCALE_NEEDLE_PIVOT_Y = (float) (43.0 / BASE_SCREEN_WIDTH);
        public static final float SCALE_NEEDLE_HEIGHT = (float) (413.0 / BASE_SCREEN_HEIGHT);
        public static final float SCALE_NEEDLE_MARGIN_TOP = (float) (43.0 / BASE_SCREEN_HEIGHT);

        /*唱盘比例*/
        public static final float SCALE_DISC_SIZE = (float) (813.0 / BASE_SCREEN_WIDTH);
        public static final float SCALE_DISC_MARGIN_TOP = (float) (190 / BASE_SCREEN_HEIGHT);

        /*专辑图片比例*/
        public static final float SCALE_MUSIC_PIC_SIZE = (float) (533.0 / BASE_SCREEN_WIDTH);

        /*设备屏幕宽度*/
        public static int getScreenWidth(Context context) {
            return context.getResources().getDisplayMetrics().widthPixels;
        }

        /*设备屏幕高度*/
        public static int getScreenHeight(Context context) {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
    }


    public static final int NEXT_MUSIC = 0;
    public static final int PRE_MUSIC = 1;

    /**
     * 用户信息，缓存，方便调用
     */
    public static UserAccountBean userAccount;
    /**
     * 用户画像，缓存，方便调用
     */
    public static UserProfileBean userProfile;

    public static String UserCookie = "";
}
