package com.tiocloud.chat.widget.emotion;

public interface EmoticonSelectedListener {
    /**
     * 选中了emoji
     *
     * @param key 关键字
     */
    void onEmojiSelected(String key);
    void onEmojicollectSelected(String key);

}
