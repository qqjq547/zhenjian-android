package com.tiocloud.newpay.feature.redpacket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ArrayUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.watayouxiang.androidutils.page.BindingDarkActivity;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletRedpacketActivityBinding;
import com.tiocloud.newpay.feature.redpacket.adapter.RedPacketFragmentAdapter;
import com.tiocloud.newpay.feature.redpacket.adapter.RedPacketTabAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/03
 *     desc   : 红包记录
 * </pre>
 */
public class RedPacketActivity extends BindingDarkActivity<WalletRedpacketActivityBinding> {

    private String spinnerValue;
    private final List<OnSpinnerSelectedListener> listeners = new ArrayList<>(2);

    public static void start(Context context) {
        Intent starter = new Intent(context, RedPacketActivity.class);
        context.startActivity(starter);
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
    public Integer statusBar_color() {
        return getResources().getColor(R.color.red_ff5e5e);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_redpacket_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setData(this);
        resetUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listeners.clear();
    }

    private void resetUI() {
        String[] spinnerItems = getSpinnerItems();
        if (ArrayUtils.isEmpty(spinnerItems)) {
            return;
        }
        // viewPager
        binding.vpPager.setAdapter(new RedPacketFragmentAdapter(getSupportFragmentManager()));
        // indicator
        RedPacketTabAdapter tabAdapter = new RedPacketTabAdapter(binding.rvIndicator);
        tabAdapter.setViewPager(binding.vpPager);
        // spinner
        spinnerValue = spinnerItems[0];
        binding.titleBar.setSpinnerRight_data(spinnerItems);
        binding.titleBar.setSpinnerRight_onItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spinnerValue = adapterView.getItemAtPosition(position).toString();
                for (OnSpinnerSelectedListener l : listeners) {
                    l.onItemSelected();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private String[] getSpinnerItems() {
        // 红包记录的年份时间下拉调整从2020开始，历史的不要
        int currYear = TimeUtils.getValueByCalendarField(Calendar.YEAR);
        List<String> yearArr = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int _year = currYear - i;
            if (_year >= 2020) {
                yearArr.add(_year + "年");
            }
        }
        return yearArr.toArray(new String[0]);
    }

    public String getPeriod() {
        if (spinnerValue != null) {
            return spinnerValue.replace("年", "");
        }
        return null;
    }

    public void setOnSpinnerSelectedListener(OnSpinnerSelectedListener l) {
        listeners.add(l);
    }

    public interface OnSpinnerSelectedListener {
        void onItemSelected();
    }
}
