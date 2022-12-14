package com.tiocloud.newpay.feature.redpaper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletRedpaperActivityBinding;
import com.tiocloud.newpay.feature.paypwd_setup.SetupPayPwdActivity;
import com.tiocloud.newpay.feature.redpacket.RedPacketActivity;
import com.tiocloud.newpay.feature.redpaper.adapter.RedPacketFragmentAdapter;
import com.tiocloud.newpay.feature.redpaper.adapter.RedPacketTabAdapter;
import com.tiocloud.newpay.feature.wallet.WalletActivity;
import com.watayouxiang.androidutils.page.BindingDarkActivity;
import com.watayouxiang.androidutils.widget.dialog.progress.EasyProgressDialog;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.PayOpenFlagReq;
import com.watayouxiang.httpclient.model.request.PayOpenReq;
import com.watayouxiang.httpclient.model.response.PayOpenFlagResp;
import com.watayouxiang.httpclient.model.response.PayOpenResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/09
 *     desc   : 发红包
 * </pre>
 */
public class RedPaperActivity extends BindingDarkActivity<WalletRedpaperActivityBinding> {

    private static final String RED_PAPER_VO = "red_paper_vo";

    public static void startP2P(Context context, String chatlinkid) {
        PayOpenFlagReq openFlagReq = new PayOpenFlagReq();
        openFlagReq.setCancelTag(context);
        openFlagReq.get(new TioCallback<PayOpenFlagResp>() {
            @Override
            public void onTioSuccess(PayOpenFlagResp payOpenFlagResp) {
                int openflag = payOpenFlagResp.getOpenflag();
                int paypwdflag = payOpenFlagResp.paypwdflag;
                if (openflag == 1) {
                    // 已经开户
                    // 是否设置钱包支付密码
                    if (paypwdflag == 1) {
                        RedPaperVo redPaperVo = new RedPaperVo(RedPaperType.P2P, chatlinkid);
                        start(context, redPaperVo);
                    } else {
                        SetupPayPwdActivity.start(context);
                    }
                } else {
                    // 未开户
//                    OpenWalletActivity.start(activity);
                    PayOpenReq payOpenReq = new PayOpenReq("0", "0", "0");
                    payOpenReq.setCancelTag(context);
                    payOpenReq.post(new TioCallback<PayOpenResp>() {
                        @Override
                        public void onTioSuccess(PayOpenResp payOpenResp) {
//                            Intent starter = new Intent(activity, WalletActivity.class);
//                            activity.startActivity(starter);
                            SetupPayPwdActivity.start(context);

                        }

                        @Override
                        public void onTioError(String msg) {
                            ToastUtils.showShort(msg);
                        }
                    });
                }
            }

            @Override
            public void onTioError(String msg) {
                ToastUtils.showShort(msg);
            }
        });
    }

    public static void startGroup(Context context, String chatlinkid) {
        PayOpenFlagReq openFlagReq = new PayOpenFlagReq();
        openFlagReq.setCancelTag(context);
        openFlagReq.get(new TioCallback<PayOpenFlagResp>() {
            @Override
            public void onTioSuccess(PayOpenFlagResp payOpenFlagResp) {
                int openflag = payOpenFlagResp.getOpenflag();
                int paypwdflag = payOpenFlagResp.paypwdflag;
                if (openflag == 1) {
                    // 已经开户
                    // 是否设置钱包支付密码
                    if (paypwdflag == 1) {
                        RedPaperVo redPaperVo = new RedPaperVo(RedPaperType.Group, chatlinkid);
                        start(context, redPaperVo);
                    } else {
                        SetupPayPwdActivity.start(context);
                    }
                } else {
                    // 未开户
//                    OpenWalletActivity.start(activity);
                    PayOpenReq payOpenReq = new PayOpenReq("0", "0", "0");
                    payOpenReq.setCancelTag(context);
                    payOpenReq.post(new TioCallback<PayOpenResp>() {
                        @Override
                        public void onTioSuccess(PayOpenResp payOpenResp) {
//                            Intent starter = new Intent(activity, WalletActivity.class);
//                            activity.startActivity(starter);
                            SetupPayPwdActivity.start(context);

                        }

                        @Override
                        public void onTioError(String msg) {
                            ToastUtils.showShort(msg);
                        }
                    });
                }
            }

            @Override
            public void onTioError(String msg) {
                ToastUtils.showShort(msg);
            }
        });

    }

    private static void start(Context context, RedPaperVo vo) {
        Intent starter = new Intent(context, RedPaperActivity.class);
        starter.putExtra(RED_PAPER_VO, vo);
        context.startActivity(starter);
    }

    public RedPaperVo getRedPaperVo() {
        return (RedPaperVo) getIntent().getSerializableExtra(RED_PAPER_VO);
    }

    @NonNull
    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    public Integer statusBar_color() {
        return getResources().getColor(R.color.red_ff5e5e);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_redpaper_activity;
    }

    @Override
    protected Integer background_color() {
        return Color.parseColor("#F8F8F8");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RedPaperVo redPaperVo = getRedPaperVo();
        if (redPaperVo.type == RedPaperType.Group) {
            binding.rvIndicatorContainer.setVisibility(View.VISIBLE);
        } else if (redPaperVo.type == RedPaperType.P2P) {
            binding.rvIndicatorContainer.setVisibility(View.GONE);
        } else {
            return;
        }
        // viewPager
        binding.vpPager.setAdapter(new RedPacketFragmentAdapter(getSupportFragmentManager(), redPaperVo.type));
        binding.vpPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                KeyboardUtils.hideSoftInput(RedPaperActivity.this);
            }
        });
        // indicator
        RedPacketTabAdapter tabAdapter = new RedPacketTabAdapter(binding.rvIndicator);
        tabAdapter.setViewPager(binding.vpPager);
        // 标题栏 - 右侧按钮
        binding.titleBar.getTvRight().setOnClickListener(view -> RedPacketActivity.start(RedPaperActivity.this));

    }

    private EasyProgressDialog progressDialog;

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new EasyProgressDialog.Builder(this)
                    .setMessage("处理中")
                    .build();
        }
        progressDialog.show();
    }
}
