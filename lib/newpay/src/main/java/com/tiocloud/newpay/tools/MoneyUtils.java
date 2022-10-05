package com.tiocloud.newpay.tools;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/17
 *     desc   :
 * </pre>
 */
public class MoneyUtils {
    /**
     * 两数相减（num1 - num2）
     */
    @Nullable
    public static String subtract(String num1, String num2) {
        try {
            BigDecimal _num1 = new BigDecimal(num1);
            BigDecimal _num2 = new BigDecimal(num2);
            return _num1.subtract(_num2).toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 两数相乘（num1 * num2）
     */
    @Nullable
    public static String multiply(String num1, String num2) {
        try {
            BigDecimal _num1 = new BigDecimal(num1);
            BigDecimal _num2 = new BigDecimal(num2);
            return _num1.multiply(_num2).toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 两数相除（num1 / num2）
     */
    @Nullable
    public static String divide(String num1, String num2) {
        try {
            BigDecimal _num1 = new BigDecimal(num1);
            BigDecimal _num2 = new BigDecimal(num2);
            return _num1.divide(_num2).toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 分 -> 元(保留两位小数)
     */
    @Nullable
    public static String fen2yuan(String fen) {
        try {
            BigDecimal _money = new BigDecimal(fen);
            BigDecimal _100 = new BigDecimal(100);
            BigDecimal _result = _money.divide(_100);
            return new DecimalFormat("0.00").format(_result);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 分 -> 元(保留两位小数)
     */
    @Nullable
    public static String fen2yuan(BigDecimal fen) {
        try {
            BigDecimal _100 = new BigDecimal(100);
            BigDecimal _yuan = fen.divide(_100);
            return new DecimalFormat("0.00").format(_yuan);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 分 -> 元(保留两位小数)
     */
    @Nullable
    public static String fen2yuan(long fen) {
        return fen2yuan("" + fen);
    }

    /**
     * 元 -> 分(不保留小数)
     */
    @Nullable
    public static String yuan2fen(String yuan) {
        try {
            BigDecimal _yuan = new BigDecimal(yuan);
            BigDecimal _100 = new BigDecimal(100);
            BigDecimal _result = _yuan.multiply(_100);
            return String.valueOf(_result.longValue());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 格式化 元(保留两位小数)
     */
    @Nullable
    public static String formatYuan(String yuan) {
        try {
            BigDecimal _yuan = new BigDecimal(yuan);
            return new DecimalFormat("0.00").format(_yuan);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取银行卡后四位
     */
    @Nullable
    public static String getBankCardLast4No(String bankCardNo) {
        // 3471
        if (bankCardNo != null && bankCardNo.length() >= 4) {
            return bankCardNo.substring(bankCardNo.length() - 4);
        }
        return null;
    }

    /**
     * 获取部分隐藏得手机号
     */
    @NonNull
    public static String getHiddenPhone(String phone) {
        // 188****0544
        String showPhone = "";
        if (phone != null && phone.length() == 11) {
            String start = phone.substring(0, 3);
            String end = phone.substring(phone.length() - 4);
            showPhone = StringUtils.format("%s****%s", start, end);
        }
        return showPhone;
    }
}
