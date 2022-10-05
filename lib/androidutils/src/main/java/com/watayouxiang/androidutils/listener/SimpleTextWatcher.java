package com.watayouxiang.androidutils.listener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * author : TaoWang
 * date : 2020-02-13
 * desc :
 */
public class SimpleTextWatcher implements TextWatcher {
    /**
     * 准备改变：准备用after长度的新子串替换掉起始位置为start、长度为count的旧子串
     *
     * @param s     改变前的整个CharSequence
     * @param start 被改变的字符的位置 从0计算 eg:0/1/2...
     * @param count 将被替换掉的旧子串的长度
     * @param after 替换的新子串的长度
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 改变后：位置start处、长度为count的子串替换掉了长度为before的旧子串
     *
     * @param s      改变后的整个CharSequence
     * @param start  被改变的字符的位置
     * @param before 被替换掉的旧子串的长度
     * @param count  替换的新子串的长度
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * 改变后：注意在该方法里可以做改变text的事情，但要避免出现永真循环
     *
     * @param s 改变后的整个CharSequence
     */
    @Override
    public void afterTextChanged(Editable s) {

    }
}
