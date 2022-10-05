package com.watayouxiang.appupdate;

import androidx.fragment.app.FragmentActivity;

import com.watayouxiang.appupdate.entity.AppUpdate;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/09/15
 *     desc   :
 * </pre>
 */
public class TestAppUpdate {
    public static void normalUpdate(FragmentActivity context) {
        AppUpdate appUpdate = new AppUpdate.Builder(UpdateManager.TEST_APK_URL_24M)
                .updateResourceId(R.layout.tio_dialog_update)
                .forceUpdate(false)
                .updateTitle(context.getString(R.string.test_update_title))
                .updateInfo(context.getString(R.string.test_update_content))
                .manualUpdateUrl("https://www.tiocloud.com/2/h5down.html")
                .build();
        new UpdateManager(context, appUpdate).startUpdate();
    }

    public static void normalUpdate2(FragmentActivity context) {
        AppUpdate appUpdate = new AppUpdate.Builder(UpdateManager.TEST_APK_URL_24M)
                .updateResourceId(R.layout.tio_dialog_update2)
                .forceUpdate(false)
                .updateTitle(context.getString(R.string.test_update_title))
                .updateInfo(context.getString(R.string.test_update_content))
                .manualUpdateUrl("https://www.tiocloud.com/2/h5down.html")
                .build();
        new UpdateManager(context, appUpdate).startUpdate();
    }

    public static void forceUpdate(FragmentActivity context) {
        AppUpdate appUpdate = new AppUpdate.Builder(UpdateManager.TEST_APK_URL_24M)
                .updateResourceId(R.layout.tio_dialog_update)
                .forceUpdate(true)
                .updateTitle(context.getString(R.string.test_update_title))
                .updateInfo(context.getString(R.string.test_update_content))
                .manualUpdateUrl("https://www.tiocloud.com/2/h5down.html")
                .build();
        new UpdateManager(context, appUpdate).startUpdate();
    }

    public static void forceUpdate2(FragmentActivity context) {
        AppUpdate appUpdate = new AppUpdate.Builder(UpdateManager.TEST_APK_URL_24M)
                .updateResourceId(R.layout.tio_dialog_update2)
                .forceUpdate(true)
                .updateTitle(context.getString(R.string.test_update_title))
                .updateInfo(context.getString(R.string.test_update_content))
                .manualUpdateUrl("https://www.tiocloud.com/2/h5down.html")
                .build();
        new UpdateManager(context, appUpdate).startUpdate();
    }
}
