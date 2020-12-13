package com.tiny.asmdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.gyf.barlibrary.ImmersionBar;

import java.lang.reflect.Method;

/**
 * 自定义状态栏工具类
 */
public class StatusBarUtil {

    // 深色字体设置失败时默认兼容状态栏透明度
    private static final float STATUS_ALPHA_DARK = 0.2f;
    // 浅色字体设置失败时默认兼容状态栏透明度
    private static final float STATUS_ALPHA_LIGHT = 0.2f;

    /**
     * 状态栏透明
     *
     * @param activity 需要处理透明的页面
     * @param darkFont 页面字体颜色（支持android 6.0）
     */
    public static void initStatusBar(Activity activity, boolean darkFont) {
        if (activity == null) {
            return;
        }
        initStatusBar(activity, darkFont, false);
    }

    /**
     * 状态栏透明
     *
     * @param activity 需要处理透明的页面
     * @param darkFont 页面字体颜色（支持android 6.0）
     */
    public static void initStatusBar(Activity activity, boolean darkFont, boolean navigationBarEnable) {
        if (activity == null) {
            return;
        }
        initStatusBar(activity, darkFont, darkFont ? STATUS_ALPHA_DARK : STATUS_ALPHA_LIGHT, navigationBarEnable);
    }

    /**
     * 状态栏透明
     *
     * @param fragment 需要处理透明的页面
     * @param darkFont 页面字体颜色（支持android 6.0）
     */
    public static void initStatusBar(Fragment fragment, boolean darkFont) {
        if (fragment == null) {
            return;
        }
        ImmersionBar.with(fragment)
            .statusBarDarkFont(darkFont, darkFont ? STATUS_ALPHA_DARK : STATUS_ALPHA_LIGHT)
            .init();
    }

    /**
     * 状态栏透明
     *
     * @param activity 需要处理透明的页面
     * @param darkFont 页面字体颜色（支持android 6.0）
     * @param statusAlpha 当字体颜色设置失效时，改变状态栏透明度，兼容式处理
     */
    public static void initStatusBar(Activity activity, boolean darkFont, float statusAlpha,
                                     boolean navigationBarEnable) {
        if (activity == null) {
            return;
        }
        ImmersionBar.with(activity).statusBarDarkFont(darkFont, statusAlpha).navigationBarEnable(navigationBarEnable)
            .init();
    }

    public static void updateBarBackground(View immersionBarStub, String color) {
        if (immersionBarStub != null && !TextUtils.isEmpty(color)) {
            immersionBarStub.setBackgroundColor(Color.parseColor(color));
        }
    }

    /**
     * @description :初始化虚拟导航栏颜色
     * @date :2020/2/27 9:29
     * @author :dingqq
     * @version :1.0
     * @param activity:
     * @param isDarkIcon: 是否深色图标
     * @param navigationColor:
     */
//    public static void initNavigationBarColor(Activity activity, boolean isDarkIcon, @ColorRes int navigationColor) {
//        if (activity == null) {
//            return;
//        }
//        ImmersionBar.with(activity).navigationBarDarkIcon(isDarkIcon).navigationBarColor(navigationColor).init();
//    }

    /**
     * 状态栏透明
     *
     * @param activity 需要处理透明的页面
     * @param dialog 需要处理透明dialog
     */
//    public static void initStatusBar(Activity activity, Dialog dialog) {
//        if (activity == null || dialog == null) {
//            return;
//        }
//        ImmersionBar.with(activity, dialog).init();
//    }

    /**
     * 清除状态栏透明
     *
     * @param activity 需要处理透明的页面
     * @param dialog 需要处理透明dialog
     */
//    public static void destroy(Activity activity, Dialog dialog) {
//        if (activity == null || dialog == null) {
//            return;
//        }
//        ImmersionBar.with(activity, dialog).destroy();
//    }

    /**
     * 状态栏透明
     *
     * @param activity 需要处理透明的页面
     * @param fullScreen 设置是否为全屏
     */
    public static void initFullScreen(Activity activity, boolean fullScreen) {
        if (activity == null) {
            return;
        }
        ImmersionBar.with(activity).fullScreen(fullScreen).init();
    }

    /**
     * 隐藏底部虚拟导航栏
     */
    public static void transparentNavigationBar(Activity activity) {
        if (activity == null) {
            return;
        }
        ImmersionBar.with(activity)
            .fullScreen(true)
            .transparentBar()
            .init();
    }

    /**
     * 释放资源
     */
    public static void destroy(Activity activity) {
        if (activity == null) {
            return;
        }
        ImmersionBar.with(activity).destroy();
    }

    /**
     * 释放资源
     */
    public static void destroy(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        ImmersionBar.with(fragment).destroy();
    }

    @TargetApi(19)
    public static void setTranslucentStatus(Window win, boolean on) {
        if (win == null) {
            return;
        }
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     *  处理全屏后界面的顶部 view，使页面主体内容不被状态栏覆盖
     */
    public static void setStatusTranslucent(View view) {
        setStatusTranslucent(view.getContext(), view);
    }

    public static void setStatusTranslucent(Context context, View view) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 增加 view 的 paddingTop
            if (context instanceof Activity) {
                int height = getStatusHeight((Activity) context);
                view.setPadding(view.getPaddingLeft(), height, view.getPaddingBottom(), view.getPaddingBottom());
                // 增加 paddingTop 后改变相应的 height
                if (view.getLayoutParams().height != ViewGroup.LayoutParams.WRAP_CONTENT
                    && view.getLayoutParams().height != ViewGroup.LayoutParams.MATCH_PARENT) {
                    view.getLayoutParams().height = view.getLayoutParams().height + height;
                }
            }
        } else {
            view.setPadding(view.getPaddingLeft(), 0, view.getPaddingBottom(), view.getPaddingBottom());
        }
    }

    /**
     * @return > 0 success; <= 0 fail
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        if (activity == null) {
            return statusHeight;
        }
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (statusHeight == 0) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 是否存在导航栏
     */
    public static boolean hasNavigationBar(Context context) {
        if (context == null) {
            return false;
        }
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }


    /**
     * 获取导航栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        if (context == null) {
            return 0;
        }
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0/* && hasNavigationBar(context)*/) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }


}
