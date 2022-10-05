package com.tiocloud.chat.widget.emotion;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.LinkMovementClickMethod;
import com.tiocloud.chat.baseNewVersion.utils2.ScreenUtils2;
import com.tiocloud.chat.baseNewVersion.view.BaseRVAdapter;
import com.tiocloud.chat.baseNewVersion.view.BaseViewHolder;
import com.tiocloud.chat.widget.emotion.emoji.EmojiView;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.EmojiAllReq;
import com.watayouxiang.httpclient.model.request.RemoveCollectEmojiReq;
import com.watayouxiang.httpclient.model.request.SendCollectEmojiReq;
import com.watayouxiang.httpclient.model.response.EmojiAllResp;

import java.util.ArrayList;
import java.util.List;

/**
 * author : TaoWang
 * date : 2019-12-27
 * desc : 表情选择控件（emoji/贴图）
 */
public class EmoticonPickerView extends LinearLayout {

    private View rootView;
    private EmoticonSelectedListener listener;
    private EmojiView emojiView;
    private RelativeLayout rl_emojiBtn1,rl_emojiBtn2;
    private RecyclerView recycler_viewSC;
    private  List<EmojiAllResp.Data> emojiRespData=new ArrayList<>();
    private BaseRVAdapter baseRVAdapterEmoji;
    public EmoticonPickerView(Context context) {
        super(context);
        init(context);
    }

    public EmoticonPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmoticonPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.emoji_layout, this);
        recycler_viewSC=rootView.findViewById(R.id.recycler_viewSC);
        rl_emojiBtn1=rootView.findViewById(R.id.rl_emojiBtn1);
        rl_emojiBtn2=rootView.findViewById(R.id.rl_emojiBtn2);
        rl_emojiBtn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_emojiBtn1.setBackgroundColor(Color.parseColor("#ffffff"));
                rl_emojiBtn2.setBackgroundColor(Color.parseColor("#F0F1F5"));
                recycler_viewSC.setVisibility(GONE);
            }
        });
        rl_emojiBtn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_emojiBtn1.setBackgroundColor(Color.parseColor("#F0F1F5"));
                rl_emojiBtn2.setBackgroundColor(Color.parseColor("#ffffff"));
                recycler_viewSC.setVisibility(VISIBLE);

            }
        });
        recycler_viewSC.setLayoutManager(new GridLayoutManager(context,5));

        baseRVAdapterEmoji=new BaseRVAdapter(context,emojiRespData) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.rec_collecemojit_layout_item;
            }

            @Override
            public void onBind(BaseViewHolder holder, int position) {
                TioImageView iv_emojiBtnCollect=holder.getView(R.id.iv_emojiBtnCollect);
                iv_emojiBtnCollect.load_tioFoundPic(emojiRespData.get(position).coverurl);
                TextView tv_EmojiBtnSend=holder.getTextView(R.id.tv_EmojiBtnSend);
                tv_EmojiBtnSend.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onEmojicollectSelected(emojiRespData.get(position).imgid+"");
                    }
                });
                tv_EmojiBtnSend.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.d("=====长按删除==","=="+emojiRespData.get(position).coverurl);

                        View windowContentViewRoot = LayoutInflater.from(context).inflate(R.layout.emoji_remove_item, null);
                        final PopupWindow popupWindow = new PopupWindow(windowContentViewRoot,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
                        popupWindow.setTouchable(true);
                        popupWindow.setBackgroundDrawable(new ColorDrawable());
                        int windowPos[] = calculatePopWindowPos(tv_EmojiBtnSend, windowContentViewRoot);
                        TextView tv_removeEmoji=windowContentViewRoot.findViewById(R.id.tv_removeEmoji);
                        tv_removeEmoji.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("=====删除==","=="+emojiRespData.get(position).coverurl);
                                RemoveCollectEmojiReq removeCollectEmojiReq = new RemoveCollectEmojiReq(emojiRespData.get(position).imgid+"");
                                removeCollectEmojiReq.setCancelTag(this);
                                removeCollectEmojiReq.post(new TioCallback<String>() {
                                    @Override
                                    public void onTioSuccess(String s) {
//                                     Log.d("====删除成功==","==="+s);
                                        emojiRespData.remove(position);
                                        baseRVAdapterEmoji.notifyDataSetChanged();

                                    }
                                    @Override
                                    public void onTioError(String msg) {
                                    Log.d("====onTioError==","==="+msg);
                                    }
                                });
                            }
                        });
                        if(position>4){
                            int xOff = (position+1)%5;// 可以自己调整偏移
                            windowPos[0]= windowPos[0]- windowPos[0]/xOff;
                        }else {
                            windowPos[0]= windowPos[0]- windowPos[0]/(position+1);
                        }
                        popupWindow.showAtLocation(windowContentViewRoot, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
                        return true;
                    }
                });
                tv_EmojiBtnSend.setMovementMethod(new LinkMovementClickMethod());

            }
        };
        recycler_viewSC.setAdapter(baseRVAdapterEmoji);
        EmojiAllReq allReq = new EmojiAllReq("");
        allReq.setCancelTag(context);
        allReq.get(new TioCallback<EmojiAllResp>() {

            @Override
            public void onTioSuccess(EmojiAllResp collectData) {
                emojiRespData.clear();
                emojiRespData.addAll(collectData);
//                Log.d("====收藏列表==","==="+emojiRespData.size()+"=="+emojiRespData.get(0).url);
                baseRVAdapterEmoji.notifyDataSetChanged();
            }

            @Override
            public void onTioError(String msg) {
//                Log.e("====收藏列表==","==="+msg);

            }
        });
    }

    private static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
// 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);

        final int anchorHeight = anchorView.getHeight();
// 获取屏幕的高宽
//        final int screenHeight = ScreenUtils.getScreenHeight();
//        final int screenWidth = ScreenUtils.getScreenWidth();
        final int screenHeight = ScreenUtils2.getScreenHeight(anchorView.getContext());
        final int screenWidth = ScreenUtils2.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
// 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
// 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;

    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void show(EmoticonSelectedListener listener) {
        this.listener = listener;
        showEmojiView();
    }

    private void showEmojiView() {
        if (emojiView == null) {
            emojiView = new EmojiView(rootView, listener);
        }
        emojiView.show();
    }
}
