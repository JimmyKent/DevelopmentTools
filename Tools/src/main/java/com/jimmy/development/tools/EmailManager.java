package com.jimmy.development.tools;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class EmailManager {

    private static final String ACCOUNT_ID_PARAM = "ACCOUNT_ID";
    private static final String ACTIVITY_INTENT_SCHEME = "content";
    private static final String ACTIVITY_INTENT_HOST = "ui.email.android.com";
    private static final String VIEW_MAILBOX_INTENT_URL_PATH = "/view/mailbox";

    public static Intent createOpenAccountInboxIntent(Context context, long accountId) {
        final Uri.Builder b = createActivityIntentUrlBuilder(VIEW_MAILBOX_INTENT_URL_PATH);
        setAccountId(b, accountId);
        Intent i = new Intent(Intent.ACTION_MAIN, b.build());
        prepareRestartAppIntent(i);
        return i;
    }

    public static Uri.Builder createActivityIntentUrlBuilder(String path) {
        final Uri.Builder b = new Uri.Builder();
        b.scheme(ACTIVITY_INTENT_SCHEME);
        b.authority(ACTIVITY_INTENT_HOST);
        b.path(path);
        return b;
    }

    public static void setAccountId(Uri.Builder b, long accountId) {
        if (accountId != -1) {
            b.appendQueryParameter(ACCOUNT_ID_PARAM, Long.toString(accountId));
        }
    }

    private static void prepareRestartAppIntent(Intent i) {
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
    }

    public static int findLocalEmailId(Context context, String email) {
        try {
            final String FIELD_ADDRESS = "emailAddress";
            final String FIELD_ID = "_id";
            final Uri EMAIL_URI = Uri.parse("content://com.android.email.provider/account");
            Cursor c = context.getContentResolver().query(
                    EMAIL_URI,
                    new String[]{FIELD_ID}, FIELD_ADDRESS + "=?", new String[]{email}, null);
            if (c != null) {
                try {
                    if (c.moveToFirst()) {
                        return c.getInt(0);
                    }
                } finally {
                    c.close();
                }
            }
        } catch (Exception ignore) {
            // 某些情况下没有读取权限，忽略
        }
        return -1;
    }

    public static void useBrowserMail(Context context, String url) {
        if (!TextUtils.isEmpty(url)) {
            final Uri uri = Uri.parse(url);
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            } catch (Exception e) {
                Log.w("useBrowserMail", e);
            }
        }
    }
}
