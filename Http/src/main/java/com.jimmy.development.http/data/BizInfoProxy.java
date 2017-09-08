package com.jimmy.development.http.data;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinguochong on 17-8-1.
 * 业务信息代理。
 */
public class BizInfoProxy {

    private static String DEFAULT_CHANNEL = "102002";

    //GameInfo是在module中用的，GameConfig才是需要
    private Map<String, GameInfoProxy> mGameInfos = new HashMap<>();
    private Map<String, String> channels = new HashMap<>();
    private Map<String, UserProxy> userProxys = new HashMap<>();


    public void setGameInfo(GameInfoProxy gameInfoProxy) {
        mGameInfos.put(gameInfoProxy.mPkgName, gameInfoProxy);
        if (TextUtils.isEmpty(gameInfoProxy.mGameId) || TextUtils.isEmpty(gameInfoProxy.mGameKey)) {
            throw new IllegalArgumentException("game appid/appkey can not be empty!");
        }
    }

    /**
     * 得到游戏信息：gameId，gameKey，gamePkName
     */
    public GameInfoProxy getGameInfo(String pkgName) {
        GameInfoProxy gameInfoProxy = mGameInfos.get(pkgName);
        if (gameInfoProxy == null)
            return new GameInfoProxy();
        return gameInfoProxy;
    }

    public void removeGameInfo(String pkgName) {
        mGameInfos.remove(pkgName);
    }

    public void clearAllGameInfo() {
        mGameInfos.clear();
    }

    public String getChannel(String pkgName) {
        String channel = channels.get(pkgName);
        if (TextUtils.isEmpty(channel)) {
            channel = DEFAULT_CHANNEL;
        }
        return channel;
    }

    public void setChannel(String pkgName, String channel) {
        channels.put(pkgName, channel);
    }

    public UserProxy getUserProxy(String pkgName) {
        UserProxy user = userProxys.get(pkgName);
        if (user == null) {
            user = new UserProxy();
        }
        return user;
    }

    public void setUserProxy(String pkgName, UserProxy userProxy) {
        userProxys.put(pkgName, userProxy);
    }

    public void removeUserInfo(String pkgName) {
        userProxys.remove(pkgName);
    }

    public void clearAllUserInfo() {
        userProxys.clear();
    }

    //---------------------------------------

    public static class GameInfoProxy {
        public String mGameId;
        public String mGameKey;
        public String mPkgName;
        public String versionCode;
        public String versionName;

        public GameInfoProxy() {
        }

        public GameInfoProxy(String gameId, String gameKey, String gamePkName, String gameVc, String gameVn) {
            mGameId = gameId;
            mGameKey = gameKey;
            mPkgName = gamePkName;
            versionCode = gameVc;
            versionName = gameVn;
        }
    }

    public static class UserProxy {
        public String uid;
        public String token;
        public String refreshToken;

        public UserProxy() {
        }

        public UserProxy(String uid, String token, String refreshToken) {
            this.uid = uid;
            this.token = token;
            this.refreshToken = refreshToken;
        }
    }


    //--------------------------------------------------------------
    private static BizInfoProxy sInstance = null;

    public static BizInfoProxy instance() {
        if (sInstance == null) {
            sInstance = new BizInfoProxy();
        }
        return sInstance;
    }
}
