package com.jimmy.development.tools;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.jimmy.development.constants.PackageConstants;

/**
 * Created by jinguochong on 17-8-29.
 * 召回游戏中心
 */

public class RecallHelper {
    private static final String RECALL_GAME_CENTER = "setForceMode";
    private static final String INVALID_ACTIVITY = "com.meizu.flyme.gamecenter.activity.GameMainActivity2";

    /*public static void recallGameCenter(Context context) throws Exception {
        Intent intent = new Intent();
        intent.setPackage(PackageConstants.PACKAGE_NAME_GAME_CENTER);
        ComponentName componentName = new ComponentName(
                PackageConstants.PACKAGE_NAME_GAME_CENTER, INVALID_ACTIVITY);
        intent.setComponent(componentName);
        Reflect.on(intent).call(RECALL_GAME_CENTER, true).get();
        context.startActivity(intent);
    }*/
}
