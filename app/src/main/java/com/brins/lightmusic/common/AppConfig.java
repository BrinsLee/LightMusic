package com.brins.lightmusic.common;

import android.content.Context;

import com.brins.lightmusic.model.userlogin.UserAccountBean;
import com.brins.lightmusic.model.userlogin.UserProfileBean;

public class AppConfig {
    public static boolean isLogin = false;
    public static final String BASEURL = "http://118.31.65.24/";

    public static final int PHONE_NUMBER_LENGTH = 11;
    public static final String UMAPPKEY = "5e33c33c4ca357e8100000fa";

    /**
     * 登录
     */
    public interface LOGIN {

        String LOGIN_EMAIL = "/login";
        String LOGIN_CELLPHONE = "/login/cellphone";
        String LOGOUT = "/logout";
        String VERIFY_CODE = "/captcha/verify";
        String DEFAULT_ID = "490583871";
        String CHECK_CODE = "/captcha/sent";
    }

    public interface USER {
        String USER_DETAIL = "/user/detail";//登陆后调用此接口 , 传入用户 id, 可以获取用户详情
        String USER_SUBCOUNT = "/user/subcount"; //获取用户信息 , 歌单，收藏，mv, dj 数量
        /*
        必选参数
        gender: 性别 0:保密 1:男性 2:女性

        birthday: 出生日期,时间戳 unix timestamp

        nickname: 用户昵称

        province: 省份id

        city: 城市id

        signature：用户签名*/
        String USER_UPDATE = "/user/update";//更新用户信息
        /**
         * 用户歌单
         */
        String USER_PLAYLIST = "/user/playlist";

        /***
         * 获取用户电台
         * 用户 id
         * */
        String USER_DJ = "/user/dj";

        /***
         * 获取用户关注
         * 用户 id
         *
         * 可选 limit : 返回数量 , 默认为 30
         *
         * offset : 偏移数量，用于分页 , 如 : 如 :( 页数 -1)*30, 其中 30 为 limit 的值 , 默认为 0
         * */
        String USER_FOLLOWS = "/user/follows";

        /***
         * 获取用户粉丝
         * 用户 id
         *
         * 可选 limit : 返回数量 , 默认为 30
         *
         * lasttime : 返回数据的 lasttime ,默认-1,传入上一次返回结果的 lasttime,将会返回下一页的数据
         * */
        String USER_FOLLOWEDS = "/user/followeds";

        /***
         * 获取用户动态
         * 用户 id
         *
         * 可选 limit : 返回数量 , 默认为 30
         *
         * lasttime : 返回数据的 lasttime ,默认-1,传入上一次返回结果的 lasttime,将会返回下一页的数据
         *
         * 返回type
         * 18 分享单曲
         * 19 分享专辑
         * 17、28 分享电台节目
         * 22 转发
         * 39 发布视频
         * 35、13 分享歌单
         * 24 分享专栏文章
         * 41、21 分享视频
         *
         * */
        String USER_EVENT = "/user/event";
    }



    public interface EVENT {
        /***
         * 转发用户动态
         *
         * 必选参数 : uid : 用户 id
         *
         * evId : 动态 id
         *
         * forwards : 转发的评论
         * */
        String EVENT_FORWARD = "/event/forward";

        /***
         * 删除用户动态
         *
         * 必选参数 : evId : 动态 id
         *
         * */
        String EVENT_DELETE = "/event/del";

    }

    public interface SHARE {

        /***
         *
         * 必选参数 : id : 资源 id （歌曲，歌单，mv，电台，电台节目对应 id）
         *
         * 可选参数 : type: 资源类型，默认歌曲 song，可传 song,playlist,mv,djradio,djprogram
         *
         * msg: 内容，140 字限制，支持 emoji，@用户名（/user/follows接口获取的用户名，用户名后和内容应该有空格），图片暂不支持
         * */
        String SHARE_RESOURCE = "/share/resource";

    }

    public interface PLAYLIST {
        /**
         * 更新歌单
         * id:歌单id
         *
         * name:歌单名字
         *
         * desc:歌单描述
         *
         * tags:歌单tag ,多个用 `;` 隔开,只能用官方规定标签
         */
        String UPDATE_PLAYLIST = "/playlist/update";

        /***
         * 更新歌单描述
         * id:歌单id
         *
         * desc:歌单描述
         * */
        String UPDATE_PLAYLIST_DESC = "/playlist/desc/update";

        /***
         * 更新歌单名
         *
         * id: 歌单id
         *
         * name: 歌单名
         * */
        String UPDATE_PLAYLIST_NAME = "/playlist/name/update";

        /***
         * 更新歌单标签
         * id: 歌单id
         *
         * tags: 歌单标签
         * */
        String UPDATE_PLAYLIST_TAGS = "/playlist/tags/update";

        /***
         * 调整歌单顺序
         * ids: 歌单id列表
         * /playlist/order/update?ids=[111,222]
         * */
        String UPDATE_PLAYLIST_ORDER = "/playlist/order/update";

    }




    /**
     * 私人FM
     */
    public static final String USER_FM = "/personal_fm";

    /**
     * 每日推荐
     */
    public static final String DAILY_RECOMMEND = "/recommend/songs";


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
     * 歌曲评论
     */
    public static final String MUSIC_COMMENT = "/comment/music";


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
     * <p>
     * 可选参数 : limit : 返回数量 , 默认为 30 offset : 偏移数量，用于分页 , 如 : 如 :( 页数 -1)*30, 其中 30 为 limit 的值 , 默认为 0
     * <p>
     * type: 搜索类型；默认为 1 即单曲 , 取值意义 : 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单, 1002: 用户, 1004: MV, 1006: 歌词, 1009: 电台, 1014: 视频, 1018:综合
     */
    public static final String SEARCH = "/search";

    public static final String SHARE_ANIMATION_IMAGE_NAME = "image";

    public interface RECOMMEND {

        String RECOMMEND_MV = "/personalized/mv";

        /***
         * 推荐歌单
         * */
        String RECOMMEND_MUSIC_LIST = "/personalized";

    }


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
