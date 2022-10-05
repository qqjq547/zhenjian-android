package com.tiocloud.session.feature.join_group_apply_info;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tiocloud.session.R;
import com.tiocloud.session.TioSession;
import com.tiocloud.session.databinding.SessionJoinGroupApplyInfoActivityBinding;
import com.tiocloud.session.databinding.SessionJoinGroupApplyInfoHeaderBinding;
import com.tiocloud.session.event.AgreeJoinGroupEvent;
import com.tiocloud.session.feature.join_group_apply_info.mvp.Contract;
import com.tiocloud.session.feature.join_group_apply_info.mvp.Presenter;
import com.watayouxiang.androidutils.constant.Extra;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.page.MvpLightActivity;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.model.response.GroupApplyInfoResp;
import com.watayouxiang.imclient.TioIMClient;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/02/08
 *     desc   : 入群申请信息页
 * </pre>
 */
public class JoinGroupApplyInfoActivity extends MvpLightActivity<Presenter, SessionJoinGroupApplyInfoActivityBinding> implements Contract.View {

    public static final int REQUEST_CODE_AGREE = 666;
    public static final int RESULT_CODE_AGREE = 667;

    private ListAdapter listAdapter;
    private SessionJoinGroupApplyInfoHeaderBinding headerBinding;

    public static void start(Activity context, long aid, String mid) {
        Intent starter = new Intent(context, JoinGroupApplyInfoActivity.class);
        starter.putExtra(Extra.APPLY_ID, aid);
        starter.putExtra(Extra.MESSAGE_ID, mid);
        context.startActivityForResult(starter, JoinGroupApplyInfoActivity.REQUEST_CODE_AGREE);
    }

    @Override
    protected Integer background_color() {
        return Color.WHITE;
    }

    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.session_join_group_apply_info_activity;
    }

    @Override
    public Presenter newPresenter() {
        return new Presenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (headerBinding != null) {
            headerBinding.unbind();
        }
    }

    @Override
    public long getAid() {
        return getIntent().getLongExtra(Extra.APPLY_ID, 0);
    }

    @Override
    public String getMid() {
        return getIntent().getStringExtra(Extra.MESSAGE_ID);
    }

    @Override
    public void onApplyInfoResp(GroupApplyInfoResp resp) {
        List<GroupApplyInfoResp.ItemsBean> items = resp.getItems();
        GroupApplyInfoResp.ApplyBean apply = resp.getApply();
        if (apply == null || items == null) {
            TioToast.showShort("申请信息为空");
            return;
        }
        // btn container
        int status = apply.getStatus();
        if (status == 1) {// 1已处理
            binding.llContainer.setVisibility(View.VISIBLE);
            binding.slPositive.setVisibility(View.VISIBLE);
            binding.slNegative.setVisibility(View.GONE);
            binding.btnPositive.setEnabled(false);
            binding.btnPositive.setText("已同意");
        } else if (status == 2) {// 2未处理
            binding.llContainer.setVisibility(View.VISIBLE);
            binding.slPositive.setVisibility(View.VISIBLE);
            binding.slNegative.setVisibility(View.VISIBLE);
            binding.btnPositive.setEnabled(true);
            binding.btnPositive.setText("同意邀请");
        } else {
            binding.llContainer.setVisibility(View.GONE);
        }
        // list
        listAdapter.setNewData(items);
        // header view
        headerBinding.ivAvatar.load_tioAvatar(apply.getGroupavator());
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(getResUrl(apply.getGroupavator()))
//                .listener(new RequestListener<File>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
//                        Log.d("===下载失败===","===");
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
////                                Log.d("===下载成功===",resource.getName()+"==="+resource.getAbsolutePath());
//                        byte[] bytes= FileUtils.File2byte(resource);
//                        try {
//                            byte[] imagebyte2=  FileUtils.encryptByte(bytes);
////                                    Bitmap  bitmap= Bytes2Bimap(imagebyte2);
////                                    msgImageView.setImageBitmap(bitmap);
//                            Glide.with(getContext())
//                                    .load(imagebyte2)
//                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                                    .into(headerBinding.ivAvatar);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
        headerBinding.ivAvatar.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                TioSession.getBridge().startUserDetailActivity(getActivity(), apply.getOperuid() + "");
            }
        });
        headerBinding.tvNick.setText(StringUtils.null2Length0(apply.getGroupnick()));
        headerBinding.tvExplainTitle.setText(StringUtils.format("邀请%d位朋友进群", items.size()));
        headerBinding.tvExplainContent.setText(StringUtils.null2Length0(apply.getApplymsg()));
        // positive btn
        binding.btnPositive.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                presenter.agreeApply(apply);
            }
        });
    }

    @Override
    public void onAgreeSuccess(String mid, int aid) {
        // 发送事件
        TioIMClient.getInstance().getEventEngine().post(new AgreeJoinGroupEvent(mid));
        // 返回结果
        Intent intent = new Intent();
        intent.putExtra(Extra.MESSAGE_ID, mid);
        setResult(RESULT_CODE_AGREE, intent);
        finish();
    }

    @Override
    public void resetUI() {
        // list header
        View headerView = LayoutInflater.from(binding.recyclerView.getContext()).inflate(R.layout.session_join_group_apply_info_header, null);
        headerBinding = DataBindingUtil.bind(headerView);
        // 定制间距
        binding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            private final int space = SizeUtils.dp2px(10);

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int position = parent.getChildLayoutPosition(view);
                if (position == 0) {
                    // 表头
                    super.getItemOffsets(outRect, view, parent, state);
                } else {
                    // 列表元素
                    position = position - 1;
                    if (position % 5 == 0) {
                        outRect.left = space;
                    } else if (position % 5 == 4) {
                        outRect.right = space;
                    } else {
                        super.getItemOffsets(outRect, view, parent, state);
                    }
                }
            }
        });
        // list
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5, RecyclerView.VERTICAL, false));
        listAdapter = new ListAdapter();
        listAdapter.setHeaderView(headerView);
        binding.recyclerView.setAdapter(listAdapter);
        // positive btn
        binding.btnPositive.setOnClickListener(null);
        // negative btn
        binding.btnNegative.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });
        // header view
        headerBinding.tvNick.setText("");
        headerBinding.tvExplainTitle.setText("");
        headerBinding.tvExplainContent.setText("");
        // btn container
        binding.llContainer.setVisibility(View.GONE);
    }
}
