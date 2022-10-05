package com.tiocloud.chat.feature.session.common.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.feature.session.common.adapter.model.TioMsgType;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioMsg;
import com.tiocloud.chat.feature.session.common.adapter.viewholder.base.MsgBaseViewHolder;
import com.tiocloud.chat.mvp.addfriend.AddFriendPresenter;
import com.tiocloud.chat.mvp.card.CardPresenter;
import com.tiocloud.chat.mvp.download.DownloadPresenter;
import com.watayouxiang.androidutils.recyclerview.BaseMultiItemFetchLoadAdapter;
import com.watayouxiang.audiorecord.TioAudioPlayer;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgApply;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgImage;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author : TaoWang
 * date : 2020-02-07
 * desc :
 */
public class MsgAdapter extends BaseMultiItemFetchLoadAdapter<TioMsg, BaseViewHolder> {

    @NonNull
    private final String chatLinkId;

    public MsgAdapter(RecyclerView recyclerView, List<TioMsg> data, @NonNull String chatLinkId) {
        super(recyclerView, data);

        this.chatLinkId = chatLinkId;

        Set<Map.Entry<TioMsgType, Class<? extends MsgBaseViewHolder>>> entries = MsgViewHolderFactory.getViewHolders().entrySet();
        for (Map.Entry<TioMsgType, Class<? extends MsgBaseViewHolder>> entry : entries) {
            addItemType(entry.getKey().getValue(), MsgViewHolderFactory.getLayoutResId(), entry.getValue());
        }
    }

    @Override
    protected int getViewType(TioMsg message) {
        return MsgViewHolderFactory.getViewType(message);
    }

    @Override
    protected String getItemKey(TioMsg item) {
        return item.getId();
    }

    /**
     * 资源释放
     */
    public void release() {
        if (cardPresenter != null) {
            cardPresenter.detachView();
            cardPresenter = null;
        }
        if (addFriendPresenter != null) {
            addFriendPresenter.detachView();
            addFriendPresenter = null;
        }
        if (downloadPresenter != null) {
            downloadPresenter.detachView();
            downloadPresenter = null;
        }
        TioAudioPlayer.getInstance().release();
    }

    // ====================================================================================
    // public
    // ====================================================================================

    /**
     * 根据 mid 查找消息位置
     */
    private int getMsgPositionByMid(long mid) {
        int position = -1;
        List<TioMsg> data = getData();
        for (int i = 0, size = data.size(); i < size; i++) {
            String _mid = data.get(i).getId();
            if (_mid != null && _mid.equals(String.valueOf(mid))) {
                position = i;
                break;
            }
        }
        return position;
    }

    /**
     * 根据 mid 查找消息
     */
    private TioMsg getMsgByMid(long mid) {
        int position = getMsgPositionByMid(mid);
        if (position != -1) {
            return getData().get(position);
        }
        return null;
    }

    /**
     * 根据 mid 删除消息
     */
    public void deleteMsgByMid(long mid) {
        // 获取 item 的位置
        int position = getMsgPositionByMid(mid);
        // 存在则删除
        if (position != -1) {
            remove(position);
        }
    }

    /**
     * 已读 全部消息
     */
    public void readAllMsg() {
        List<TioMsg> data = getData();
        for (int i = 0, size = data.size(); i < size; i++) {
            data.get(i).setReadMsg(true);
        }
        notifyDataSetChanged();
    }

    /**
     * 处理模拟消息
     * @param tioMsg
     */
    public void handleFaultMsg(TioMsg tioMsg) {
        int size = getDataSize();
        if (size == 0) {
            getData().add(tioMsg);
        } else {
            TioMsg msg = getData().get(size - 1);
            if (msg.isFault()) {
                InnerMsgImage faultMsgImage = (InnerMsgImage) tioMsg.getContentObj();
                InnerMsgImage innerMsgImage = (InnerMsgImage) msg.getContentObj();
                innerMsgImage.progress = faultMsgImage.progress;
            } else {
                getData().add(tioMsg);
            }
        }
        notifyDataSetChanged();
    }


    /**
     * 将 申请消息 标记为 已处理
     */
    public void handleApplyMsg(long mid) {
        int position = getMsgPositionByMid(mid);
        if (position != -1) {
            TioMsg msg = getData().get(position);
            if (msg != null) {
                Object contentObj = msg.getContentObj();
                if (contentObj instanceof InnerMsgApply) {
                    InnerMsgApply apply = (InnerMsgApply) contentObj;
                    // 修改数据
                    apply.status = 1;
                    // 刷新 ui
                    notifyDataItemChanged(position);
                }
            }
        }
    }

    /**
     * 长按头像
     */
    public boolean onAvatarLongClick(View v, TioMsg msg) {
        return false;
    }

    /**
     * 单击头像
     */
    public View.OnClickListener onAvatarClick(TioMsg msg) {
        return null;
    }

    /**
     * 会话id
     */
    @NonNull
    public String getChatLinkId() {
        return chatLinkId;
    }

    // ====================================================================================
    // presenter
    // ====================================================================================

    private CardPresenter cardPresenter;
    private AddFriendPresenter addFriendPresenter;
    private DownloadPresenter downloadPresenter;

    @NonNull
    public CardPresenter getCardPresenter() {
        if (cardPresenter == null) {
            cardPresenter = new CardPresenter();
        }
        return cardPresenter;
    }

    @NonNull
    public AddFriendPresenter getAddFriendPresenter() {
        if (addFriendPresenter == null) {
            addFriendPresenter = new AddFriendPresenter();
        }
        return addFriendPresenter;
    }

    @NonNull
    public DownloadPresenter getDownloadPresenter() {
        if (downloadPresenter == null) {
            downloadPresenter = new DownloadPresenter();
        }
        return downloadPresenter;
    }
}
