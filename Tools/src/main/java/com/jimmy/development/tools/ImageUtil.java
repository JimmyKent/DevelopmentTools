package com.jimmy.development.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.IOException;

/**
 * 只在该类出现图片加载的实现，其他类不出现诸如Picasso Glide之类的，
 * 其他类调用该类来进行图片加载。
 */
public class ImageUtil {
    public static final String PATH_MEIZU = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "meizu";
    public static final String ANNOUNCEMENT_CACHE_PATH = Environment.getExternalStoragePublicDirectory("meizu").getAbsolutePath() + File.separatorChar + "anns";
    public static final String GLIDE_CACHE_PATH = PATH_MEIZU + File.separatorChar + "glide";
    public static final String LOGOUT_CACHE_PATH = Environment.getExternalStoragePublicDirectory("meizu").getAbsolutePath() + File.separatorChar + "logout";

    public static int CACHE_SIZE_30_MEGABYTES = 1024 * 1024 * 30;

    public static int DEFAULT_ICON = R.drawable.image_background;
    public static int DEFAULT_ERROR_ICON = R.drawable.image_background;

    public static int DEFAULT_BIG_ICON = R.drawable.image_background;
    public static int DEFAULT_BIG_ERROR_ICON = R.drawable.image_background;

    public interface OnGetImgCallback {
        void succeed(Bitmap bitmap);

        void fail();
    }

    public interface OnDownloadImgCallback {
        void succeed();

        void fail();
    }

    public static void init(Context context) {

    }

    public static boolean isImgDownload(Context context, String url, int width, int height) {
        try {
            Bitmap myBitmap = Glide.with(context.getApplicationContext()).load(url).asBitmap().into(width, height).get();
            return (myBitmap != null);
        } catch (Exception e) {
            return false;
        }
    }

    public static void loadIcon(Context context, String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (imageView != null && imageView.getContext() != null) {
            Activity activity = null;
            if (context instanceof Activity) {
                activity = (Activity) context;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity == null || activity.isDestroyed()) {
                    return;
                }
            } else {
                if (activity == null || activity.isFinishing()) {
                    return;
                }
            }
            Glide.with(context)
                    .load(url)
                    .placeholder(DEFAULT_ICON)
                    .error(DEFAULT_ERROR_ICON)
                    .fitCenter()
                    .into(imageView);
        }

    }

    public static void loadBigImage(Context context, String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (imageView != null && imageView.getContext() != null) {
            Activity activity = null;
            if (context instanceof Activity) {
                activity = (Activity) context;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity == null || activity.isDestroyed()) {
                    return;
                }
            } else {
                if (activity == null || activity.isFinishing()) {
                    return;
                }
            }
            Glide.with(context)
                    .load(url)
                    .fitCenter()
                    .into(imageView);
        }
    }

    public static void loadIcon(Context context, String url, ImageView imageView, int defaultIcon, int errorIcon) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (imageView != null && imageView.getContext() != null) {
            Activity activity = null;
            if (context instanceof Activity) {
                activity = (Activity) context;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity == null || activity.isDestroyed()) {
                    return;
                }
            } else {
                if (activity == null || activity.isFinishing()) {
                    return;
                }
            }
            Glide.with(context)
                    .load(url)
                    .placeholder(defaultIcon)
                    .error(errorIcon)
                    .fitCenter()
                    .into(imageView);
        }
    }

    public static void loadResImg(Context context, int resDrawable, ImageView imageView) {
        if (imageView != null && imageView.getContext() != null) {
            Activity activity = null;
            if (context instanceof Activity) {
                activity = (Activity) context;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity == null || activity.isDestroyed()) {
                    return;
                }
            } else {
                if (activity == null || activity.isFinishing()) {
                    return;
                }
            }
            Glide.with(context).load(resDrawable).into(imageView);
        }
    }

    public static void loadResImg(Context context, String resDrawable, ImageView imageView) {
        if (imageView != null && imageView.getContext() != null) {
            Activity activity = null;
            if (context instanceof Activity) {
                activity = (Activity) context;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity == null || activity.isDestroyed()) {
                    return;
                }
            } else {
                if (activity == null || activity.isFinishing()) {
                    return;
                }
            }
            Glide.with(context).load(resDrawable).into(imageView);
        }
    }

    public static void loadResImg(Activity activity, String resDrawable, ImageView imageView) {
        if (imageView != null && imageView.getContext() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity == null || activity.isDestroyed()) {
                    return;
                }
            } else {
                if (activity == null || activity.isFinishing()) {
                    return;
                }
            }
            Glide.with(activity).load(resDrawable).into(imageView);
        }
    }

    public static void loadResImg(Fragment fragment, String resDrawable, ImageView imageView) {
        if (imageView != null && imageView.getContext() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (fragment == null || fragment.getActivity().isDestroyed()) {
                    return;
                }
            } else {
                if (fragment == null || fragment.getActivity().isFinishing()) {
                    return;
                }
            }
            Glide.with(fragment).load(resDrawable).into(imageView);
        }
    }

    public static void downloadImgToDesk(final Context context, final String url, final OnDownloadImgCallback callback) {
        //Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).preload();
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity.isDestroyed()) {
                    return;
                }
            } else {
                if (activity.isFinishing()) {
                    return;
                }
            }
        }
        ThreadUtils.doOnMainThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(context)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                if (callback != null) {
                                    callback.fail();
                                }
                                downloadImgToDeskAgain(context, url);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                if (callback != null) {
                                    callback.succeed();
                                }
                                return false;
                            }
                        }).preload();
            }
        });
    }

    public static void downloadImgToDeskAgain(Context context, String url) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).preload();
    }

    public static void loadImgFromDesk(Context context, String url, ImageView imageView) {
        if (imageView != null && imageView.getContext() != null) {
            Activity activity = null;
            if (context instanceof Activity) {
                activity = (Activity) context;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity == null || activity.isDestroyed()) {
                    return;
                }
            } else {
                if (activity == null || activity.isFinishing()) {
                    return;
                }
            }
            Glide.with(context.getApplicationContext()).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        }
    }

    @Deprecated
    public static void getImgFromDesk(Context context, String url, final OnGetImgCallback callback) {
        //这三个方法被删除了还是会白屏然后下载再显示
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource != null) {
                    if (callback != null)
                        callback.succeed(resource);
                } else {
                    if (callback != null)
                        callback.fail();
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                if (callback != null)
                    callback.fail();
            }

        });
        /*FutureTarget<File> future = Glide.with(context).load(url).downloadOnly(width, height);
        try {
            File cacheFile = future.get();
            if (cacheFile != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }*/
        /*Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).
                listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                        if (callback != null)
                            callback.fail();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                        // SharedPreferencesUtil.saveAdData(appContext, item.content_id, SharedPreferencesUtil.serialize(item));
                        if (callback != null)
                            callback.succeed(resource);


                        return false;
                    }
                }).preload();*/
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    /*public static int getColorFromBitmap(Bitmap bitmap) {
        int color = Color.TRANSPARENT;
        if (bitmap == null) {
            return color;
        }
        Palette palette = Palette.generate(bitmap);
        Palette.Swatch swatch = null;
        if (swatch == null) {
            swatch = palette.getMutedSwatch();
        }
        if (swatch == null) {
            swatch = palette.getDarkMutedSwatch();
        }
        if (swatch == null) {
            swatch = palette.getLightMutedSwatch();
        }
        if (swatch == null) {
            swatch = palette.getDarkVibrantSwatch();
        }
        if (swatch == null) {
            swatch = palette.getVibrantSwatch();
        }
        if (swatch == null) {
            swatch = palette.getLightVibrantSwatch();
        }
        if (swatch != null) {
            color = swatch.getRgb();
        }
        return color;
    }*/

    //glide

    public static File getLocalAnnImgList() {
        File destDir = new File(ANNOUNCEMENT_CACHE_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File nomediaFile = new File(ANNOUNCEMENT_CACHE_PATH + File.separatorChar + ".nomedia");
        if (!nomediaFile.exists()) {
            try {
                nomediaFile.createNewFile();
            } catch (IOException e) {
                Log.w("getLocalAnnImgList", e);
            }
        }
        return destDir;
    }

    public static void loadLocalAnnouncementImg(String localPath, ImageView imageView) {
        Bitmap bitmap = BitmapFactory.decodeFile(ANNOUNCEMENT_CACHE_PATH + File.separatorChar + localPath);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    public static File getLocalLogoutImg() {
        File destDir = new File(LOGOUT_CACHE_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File nomediaFile = new File(LOGOUT_CACHE_PATH + File.separatorChar + ".nomedia");
        if (!nomediaFile.exists()) {
            try {
                nomediaFile.createNewFile();
            } catch (IOException e) {
                Log.w("getLocalAnnImgList", e);
            }
        }
        return destDir;
    }

    public static void loadLocalLogoutImg(String localPath, ImageView imageView) {
        Bitmap bitmap = BitmapFactory.decodeFile(LOGOUT_CACHE_PATH + File.separatorChar + localPath);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}
