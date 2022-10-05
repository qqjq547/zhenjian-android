package com.tiocloud.chat.baseNewVersion;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.base.BaseConstants;
import com.tiocloud.chat.baseNewVersion.base.BaseNewVersionActivity;
import com.tiocloud.chat.baseNewVersion.base.BasePresenter;
import com.tiocloud.chat.baseNewVersion.utils2.DensityUtils;
import com.tiocloud.chat.baseNewVersion.utils2.IntentUtilsNew;
import com.tiocloud.chat.baseNewVersion.utils2.SPUtilsNew;
import com.tiocloud.chat.baseNewVersion.view.FontSizeView;
import com.tiocloud.chat.feature.main.MainActivity;
import com.watayouxiang.androidutils.page.AppManager;

import butterknife.BindView;
import butterknife.OnClick;

public class FontSizeActivity extends BaseNewVersionActivity {




    @BindView(R.id.fsv_font_size)
    FontSizeView fsv_font_size;
    private float fontSizeScale;
    private boolean isChange;//用于监听字体大小是否有改动
    private int defaultPos;
    @BindView(R.id.tv_font_size1)
    TextView tv_font_size1;
    @BindView(R.id.tv_font_size2)
    TextView tv_font_size2;
    @BindView(R.id.tv_font_size3)
    TextView tv_font_size3;
    @BindView(R.id.fl_right)
    FrameLayout fl_right;
    @Override
    protected BasePresenter bindPresenter() {
        return null;
    }

    @Override
    public void initView() {
        //滑动返回监听
        fsv_font_size.setChangeCallbackListener(new FontSizeView.OnChangeCallbackListener() {
            @Override
            public void onChangeListener(int position) {
                int dimension = getResources().getDimensionPixelSize(R.dimen.sp_stander);
                //根据position 获取字体倍数
                fontSizeScale = (float) (0.875 + 0.125 * position);
                //放大后的sp单位
                double v = fontSizeScale * (int) DensityUtils.px2sp(FontSizeActivity.this, dimension);
                //改变当前页面大小
                changeTextSize((int) v);
                isChange = !(position==defaultPos);
            }
        });
        float  scale = (float) SPUtilsNew.get(this, BaseConstants.SP_FontScale, 0.0f);
        if (scale > 0.5) {
            defaultPos = (int) ((scale - 0.875) / 0.125);
        } else {
            defaultPos=1;
        }
        //注意： 写在改变监听下面 —— 否则初始字体不会改变
        fsv_font_size.setDefaultPosition(defaultPos);

    }
    @OnClick({R.id.iv_back,R.id.fl_right})
    public void OnEvenClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.fl_right:
                onsavePressedNext();
                break;
        }
    }
    /**
     * 改变textsize 大小
     */
    private void changeTextSize(int dimension) {
        tv_font_size1.setTextSize(dimension);
        tv_font_size2.setTextSize(dimension);
        tv_font_size3.setTextSize(dimension);
    }
    /**
     * 重新配置缩放系数
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res =super.getResources();
        Configuration config = res.getConfiguration();
        config.fontScale= 1;//1 设置正常字体大小的倍数
        res.updateConfiguration(config,res.getDisplayMetrics());
        return res;
    }

    //保存字体
    public void onsavePressedNext() {
        SPUtilsNew.put(FontSizeActivity.this,BaseConstants.SP_FontScale,fontSizeScale);
        //重启应用
        AppManager.getAppManager().finishAllActivity();
        IntentUtilsNew.toActivity(FontSizeActivity.this, MainActivity.class,true);
    }
    @Override
    public int bindLayout() {
        return R.layout.activity_font_size;
    }
}