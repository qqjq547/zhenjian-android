package com.tiocloud.chat.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author : TaoWang
 * date : 2020/3/4
 * desc :
 */
public class KeywordUtil {
    /**
     * 关键字高亮变色
     *
     * @param color   变化的色值
     * @param text    文字
     * @param keyword 文字中的关键字
     * @return 结果SpannableString
     */
    public static SpannableString matcherSearchTitle(int color, String text, String keyword) {
        if (text == null) text = "";
        if (keyword == null) keyword = "";

//        keyword = escapeExprSpecialWord(keyword);
//        text = escapeExprSpecialWord(text);

        SpannableString s = new SpannableString(text);
        try {
            Pattern p = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(s);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

//    /**
//     * 转义正则特殊字符 （$()*+.[]?\^{},|）
//     *
//     * @param keyword
//     * @return keyword
//     */
//    public static String escapeExprSpecialWord(String keyword) {
//        if (!TextUtils.isEmpty(keyword)) {
//            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
//            for (String key : fbsArr) {
//                if (keyword.contains(key)) {
//                    keyword = keyword.replace(key, "\\" + key);
//                }
//            }
//        }
//        return keyword;
//    }
}
