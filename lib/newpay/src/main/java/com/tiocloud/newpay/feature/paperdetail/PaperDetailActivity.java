package com.tiocloud.newpay.feature.paperdetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletPaperdetailActivityBinding;
import com.tiocloud.newpay.feature.paperdetail.adapter.ListAdapter;
import com.tiocloud.newpay.feature.paperdetail.adapter.ListModel;
import com.tiocloud.newpay.feature.redpacket.RedPacketActivity;
import com.watayouxiang.androidutils.page.BindingDarkActivity;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.httpclient.model.response.PayRedInfoResp;
import com.watayouxiang.httpclient.preferences.HttpCache;

import java.util.List;
import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/13
 *     desc   : 红包详情（未领取、领取完、过期）
 * </pre>
 */
public class PaperDetailActivity extends BindingDarkActivity<WalletPaperdetailActivityBinding> {

    public final ObservableField<String> fromInfo = new ObservableField<>("小生的红包");
    public final ObservableField<String> giftInfo = new ObservableField<>("恭喜发财，大吉大利");

    private ListAdapter listAdapter;
    private static final String KEY_R_ID = "key_r_id";
    private PaperDetailViewModel viewModel;

    public static void start(Context context, String rid) {
        Intent starter = new Intent(context, PaperDetailActivity.class);
        starter.putExtra(KEY_R_ID, rid);
        context.startActivity(starter);
    }

    public String getRid() {
        return getIntent().getStringExtra(KEY_R_ID);
    }

    @Override
    protected Integer background_color() {
        return Color.WHITE;
    }

    @NonNull
    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_paperdetail_activity;
    }

    @Override
    public Integer statusBar_color() {
        return getResources().getColor(R.color.red_ff5e5e);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setVm(this);
        viewModel = newViewModel(PaperDetailViewModel.class);

        // 重置ui
        resetUI();
        // 刷新数据
        viewModel.refresh(this);
    }

    private void resetUI() {
        // 标题栏 - 右侧按钮
        binding.titleBar.getTvRight().setOnClickListener(view -> {
            if (ClickUtils.isViewSingleClick(view)) {
                RedPacketActivity.start(PaperDetailActivity.this);
            }
        });
        // 头像
        binding.ivAvatar.loadUrlStatic_radius(null, 2);
        // 拼手气红包标志 显隐
        setPinDrawableVisibility(false);
        // 红包来源
        fromInfo.set("");
        // 祝福语
        giftInfo.set("");
        // 列表初始化
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ListAdapter(null);
        binding.recyclerView.setAdapter(listAdapter);
    }

    private void setPinDrawableVisibility(boolean visibility) {
        if (visibility) {
            Drawable drawable = ResourceUtils.getDrawable(R.drawable.wallet_ic_pin_19);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            binding.tvFromInfo.setCompoundDrawables(null, null, drawable, null);
        } else {
            binding.tvFromInfo.setCompoundDrawables(null, null, null, null);
        }
    }

    public void setRefreshData(List<ListModel> models) {
        listAdapter.setNewData(models);
    }

    /**
     * 红包信息通知
     */
    public void onRedInfoResp(PayRedInfoResp.InfoBean info) {
        // 发红包人昵称
        String nick = info.getNick();
        // 发红包人祝福语
        String remark = info.bless;
        String avatar = info.getAvatar();
        avatar = HttpCache.getResUrl(avatar);
        // 是否为拼手气红包
        boolean isPinRed = info.getMode() == 2;

        // 头像
        binding.ivAvatar.loadUrlStatic_radius(avatar, 2);
        // 红包来源
        fromInfo.set(String.format(Locale.getDefault(), "%s的红包", nick));
        // 祝福语
        giftInfo.set(StringUtils.null2Length0(remark));
        // 拼手气红包标志 显隐
        setPinDrawableVisibility(isPinRed);
    }
}
