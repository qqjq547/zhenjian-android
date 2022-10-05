package com.tiocloud.chat.feature.browser;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;

import com.tiocloud.social.TioShareHelper;
import com.watayouxiang.androidutils.feature.browser.TioBrowserActivity;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.tools.TioLogger;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/02/23
 *     desc   :
 * </pre>
 */
public class TioShareBrowserActivity extends TioBrowserActivity {

    private static final String KEY_SHARE_BEAN = "KEY_SHARE_BEAN";

    public static void start(Context context, ShareBean shareBean) {
        TioLogger.d(String.valueOf(shareBean));
        Intent starter = new Intent(context, TioShareBrowserActivity.class);
        starter.putExtra(KEY_SHARE_BEAN, shareBean);
        starter.putExtra(KEY_URL, shareBean.getUrl());
        context.startActivity(starter);
    }

    public ShareBean getShareBean() {
        return (ShareBean) getIntent().getSerializableExtra(KEY_SHARE_BEAN);
    }

    @Override
    public View.OnClickListener getIvRightOnClickListener() {
        return new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                showShareDialog();
            }
        };
    }

    private TioShareHelper shareHelper;

    private void showShareDialog() {
        if (shareHelper == null) {
            ShareBean shareBean = getShareBean();
            shareHelper = new TioShareHelper(this, new TioShareHelper.H5UrlProxy() {
                @Override
                public String getH5Url() {
                    return shareBean.getUrl();
                }

                @Override
                public String getTitle() {
                    return shareBean.getTitle();
                }

                @Override
                public String getSubtitle() {
                    return shareBean.getSubtitle();
                }

                @Override
                public String getImgUrl() {
                    return shareBean.getImg();
                }
            });
        }
        shareHelper.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (shareHelper != null) {
            shareHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
}
