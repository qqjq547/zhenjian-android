package com.tiocloud.newpay.feature.bankcard_add;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletAddBankcardFragmentBinding;
import com.tiocloud.newpay.feature.newadapter.BaseRVAdapter;
import com.tiocloud.newpay.feature.newadapter.BaseViewHolder;
import com.watayouxiang.androidutils.page.MvpFragment;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.androidutils.widget.dialog.confirm.EasyConfirmDialog;
import com.watayouxiang.androidutils.widget.dialog.confirm.TioConfirmDialog;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.BankCardListReq;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.BankCardListResp;
import com.watayouxiang.httpclient.model.response.PayBindCardConfirmResp;
import com.watayouxiang.httpclient.model.response.PayBindCardResp;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/09
 *     desc   : 添加银行卡 fragment
 * </pre>
 */
public class AddBankCardFragment extends MvpFragment<AddBankCardFPresenter, WalletAddBankcardFragmentBinding> implements AddBankCardFContract.View {

    public final ObservableField<String> txt_phone = new ObservableField<>("");
    public final ObservableField<Boolean> isStartTimer = new ObservableField<>(false);
    private boolean isCreditCard = true;
    private  List<BankCardListResp.ListBean> bankCardList=new ArrayList<>();
    private String bankcode;
    private int PageNum=1;
    public static AddBankCardFragment getInstance() {
        AddBankCardFragment fragment = new AddBankCardFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public AddBankCardFPresenter newPresenter() {
        return new AddBankCardFPresenter(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_add_bankcard_fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setData(this);
        presenter.init();
    }

    // 获取验证码
    public void onClick_reqPhoneCode(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;

//        if (isCreditCard) {
//            String cardNo = binding.cardNoEtInput.getText().toString().trim();
//            String creditCardExpiry = binding.expiryEtInput.getText().toString().trim();
//            String creditCardTail = binding.tailEtInput.getText().toString().trim();
//            String name = binding.nameEtInput.getText().toString().trim();
//            String idNo = binding.idNoEtInput.getText().toString().trim();
//            String phone = binding.phoneEtInput.getText().toString().trim();
//            presenter.reqSmsCode(cardNo, creditCardExpiry, creditCardTail, name, idNo, phone);
//        } else {
//            String cardNo = binding.cardNoEtInput.getText().toString().trim();
//            String name = binding.nameEtInput.getText().toString().trim();
//            String idNo = binding.idNoEtInput.getText().toString().trim();
//            String phone = binding.phoneEtInput.getText().toString().trim();
//            presenter.reqSmsCode(cardNo, name, idNo, phone);
//        }
    }

    // 下一步
    public void onClick_next(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        if (isCreditCard) {

            String bankname=binding.etCardName.getText().toString().trim();

            String cardNo = binding.cardNoEtInput.getText().toString().trim();
//            String creditCardExpiry = binding.expiryEtInput.getText().toString().trim();
//            String creditCardTail = binding.tailEtInput.getText().toString().trim();
            String name = binding.nameEtInput.getText().toString().trim();
//            String idNo = binding.idNoEtInput.getText().toString().trim();
//            String phone = binding.phoneEtInput.getText().toString().trim();
            String creditCardExpiry = "2022";
            String creditCardTail = "128";
            String idNo ="202201281100";
            String phone = "1234567890";

            presenter.reqSmsCode(cardNo, creditCardExpiry, creditCardTail, name, idNo, phone,"2",bankname);
        } else {
            String cardNo = binding.cardNoEtInput.getText().toString().trim();
            String name = binding.nameEtInput.getText().toString().trim();
            String idNo = binding.idNoEtInput.getText().toString().trim();
            String phone = binding.phoneEtInput.getText().toString().trim();
            presenter.reqSmsCode(cardNo, name, idNo, phone,"1",bankcode);
        }
//        String smsCode = binding.codeEtInput.getText().toString().trim();
//        presenter.reqBindCard("");
    }

    @Override
    public void resetUI() {
        if (isCreditCard) {
            binding.tvChangeCardType.setText("绑定储蓄卡");
//            binding.expiryRlContainer.setVisibility(View.VISIBLE);
//            binding.tailRlContainer.setVisibility(View.VISIBLE);
        } else {
            binding.tvChangeCardType.setText("绑定信用卡");
//            binding.expiryRlContainer.setVisibility(View.GONE);
//            binding.tailRlContainer.setVisibility(View.GONE);
        }

        binding.tvChangeCardType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                isCreditCard = !isCreditCard;
//                resetUI();
            }
        });
        binding.yinhanXuanzhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageNum=1;
                ShowYinhangDialog();

            }
        });
        getBankcardListData(PageNum);
    }
    private void getBankcardListData(int Page){
        new BankCardListReq(Page+"").setCancelTag(getActivity()).get(new TioCallback<BankCardListResp>() {
            @Override
            public void onTioSuccess(BankCardListResp lists) {
                List<BankCardListResp.ListBean> listBeans=lists.getList();

                Log.d("===银行卡列表=="+Page,lists.getList().size()+"==");

                if(PageNum==1){
                    bankCardList.clear();
                }
                if(listBeans.size()>0){
                    bankCardList.addAll(listBeans);
                }
                if(baseRVAdapter!=null&&Page>1){
                    baseRVAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTioError(String msg) {
                Log.e("===银行卡列表==","=="+msg);

            }
        });
    }
    private  View inflateCard;
    private PopupWindow popupWindowCard;
    private BaseRVAdapter baseRVAdapter;
    private void ShowYinhangDialog(){
        if(popupWindowCard!=null){
            if(popupWindowCard.isShowing()){
                return;
            }
        }
        inflateCard = View.inflate(getActivity(), R.layout.show_yinhang_card_layout, null);
        popupWindowCard = new PopupWindow(inflateCard, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindowCard.setFocusable(true);
        popupWindowCard.setOutsideTouchable(true);
        popupWindowCard.setAnimationStyle(R.style.anim_menu_bottombar);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.6f;
        getActivity().getWindow().setAttributes(lp);
        popupWindowCard.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        RecyclerView recy_cardView=inflateCard.findViewById(R.id.recy_viewYH);
        inflateCard.findViewById(R.id.tv_closeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowCard.dismiss();
            }
        });
        inflateCard.findViewById(R.id.tv_okBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowCard.dismiss();
            }
        });
        recy_cardView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        baseRVAdapter=new BaseRVAdapter(getActivity(),bankCardList) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.card_yinhang_item_layout;
            }

            @Override
            public void onBind(BaseViewHolder holder, int position) {
                TextView tv_yinhangText=holder.getTextView(R.id.tv_yinhangText);
                tv_yinhangText.setText(bankCardList.get(position).getBankname()+"");
                tv_yinhangText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.yinhangEtInput.setText(bankCardList.get(position).getBankname());
                        bankcode=bankCardList.get(position).getBankcode();
                        popupWindowCard.dismiss();
                    }
                });
            }


        };
//        recy_cardView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//                //得到当前显示的最后一个item的view
//                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount()-1);
//                //得到lastChildView的bottom坐标值
//                int lastChildBottom = lastChildView.getBottom();
//                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
//                int recyclerBottom =  recyclerView.getBottom()-recyclerView.getPaddingBottom();
//                //通过这个lastChildView得到这个view当前的position值
//                int lastPosition  = recyclerView.getLayoutManager().getPosition(lastChildView);
//
//                //判断lastChildView的bottom值跟recyclerBottom是否相等
//                //判断lastPosition是不是最后一个position
//                if(lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount()-1 ){
//                    Toast.makeText(getContext(), "到底了", Toast.LENGTH_SHORT).show();
////                    getBankcardListData(2);
//
//                }
//            }
//        });

        recy_cardView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView)) {
                    PageNum++;
                    getBankcardListData(PageNum);
                }
            }
        });
        recy_cardView.setAdapter(baseRVAdapter);

        popupWindowCard.showAtLocation(getActivity().findViewById(R.id.all_linyout),
                Gravity.BOTTOM, 0, 0);
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

        @Override
    public void onCodeTimerRunning(int second) {
        isStartTimer.set(true);
        binding.phoneTvReqCode.setText(String.format(Locale.getDefault(), "已发送(%ds)", second));
    }

    @Override
    public void onCodeTimerStop() {
        isStartTimer.set(false);
        binding.phoneTvReqCode.setText("获取验证码");
    }

    @Override
    public void showErrorDialog(String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "银行卡信息有误，或卡已失效。请核对确认后再试！";
        }
        new EasyConfirmDialog.Builder(getTioActivity())
                .setMessage(msg)
                .setOnConfirmListener(new TioConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(View view, TioConfirmDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show();
    }

    @Override
    public void reqBindCardSuccess(  PayBindCardResp o) {//PayBindCardConfirmResp
        new EasyConfirmDialog.Builder(getTioActivity())
                .setMessage("绑卡成功")
                .setOnConfirmListener(new TioConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(View view, TioConfirmDialog dialog) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .build()
                .show();
    }
}
