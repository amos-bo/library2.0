package com.xb.amosboutilslibrary.amosboutils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : Amos_bo
 * @package: com.xb.myapplication.amosboutils
 * @Created Time: 2018/8/9 10:30
 * @Changed Time: 2018/8/9 10:30
 * @email: 284285624@qq.com
 * @Org: SZKT
 * @version: V1.0
 * @describe: //TODO 添加描述
 */
public class StringUtils {
    /**
     * @param tags list<String>
     * @return string
     */
    public static String listToString(List<String> tags) {
        String tagStr = "";
        for (String tag : tags) {
            if (TextUtils.isEmpty(tagStr)) {
                tagStr += tag;
            } else {
                tagStr += "," + tag;
            }
        }
        return tagStr;
    }

    /**
     * 转utf-8
     *
     * @param s String
     * @return String
     */
    private static String toUtf8String(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = String.valueOf(c).getBytes("utf-8");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) {
                        k += 256;
                    }
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 字符串转数组
     *
     * @param s   源字符
     * @param tag 分隔符
     * @return
     */
    public static List<String> strings2ListByTag(String s, String tag) {
        return Arrays.asList(s.split(tag));
    }

    /**
     * 截取数字
     *
     * @param content String
     * @return String
     */
    @SuppressWarnings("AlibabaAvoidPatternCompileInMethod")
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "0";
    }

    /**
     * 处理url中的空白符和中文
     *
     * @param url String
     * @return String
     */
    public static String dealWithStringSpace(String url) {
        try {
            url = toUtf8String(url);
            if (url.contains(" ")) {
                url = url.replaceAll(" ", "%20");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 是否是邮箱
     *
     * @param email CharSequence
     * @return boolean
     */
    public static boolean isValidEmail(@NonNull CharSequence email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 是否是电话号码
     *
     * @param phonenumber String
     * @return boolean
     */
    public boolean isPhoneNumber(@NonNull CharSequence phonenumber) {
        Matcher ma = PHONENUMBER_PATTERN.matcher(phonenumber);
        return phonenumber != null && ma.matches();
    }

    /**
     * 是否是数字
     *
     * @param str String
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = NUMBER_PATTERN;
        return pattern.matcher(str).matches();
    }

    /**
     * 是否是合法的密码
     *
     * @param pwd String
     * @return boolean
     */
    public boolean isLegalPwd(String pwd) {
        //匹配英文符号((?=[\x21-\x7e]+)[^A-Za-z0-9])
        //非数字、字母、中文
        Pattern symbolPa = Pattern.compile("[^\\da-zA-Z\\u4e00-\\u9fa5]");
        Pattern numPa = Pattern.compile("\\d");
        Pattern letterPa = Pattern.compile("[a-zA-Z]");
        int localInt = 0;
        Matcher symbolMa = symbolPa.matcher(pwd);
        Matcher numMa = numPa.matcher(pwd);
        Matcher letterMa = letterPa.matcher(pwd);
        if (symbolMa.find()) {
            localInt++;
        }
        if (numMa.find()) {
            localInt++;
        }
        if (letterMa.find()) {
            localInt++;
        }
        return localInt >= 2;
    }

    /**
     * @param string
     * @return
     * @description判断String是否为空,true代表空，false代表非空
     * @date 2015年8月19日
     */
    public static boolean isEmpty(String string) {
        if (string == null) {
            return true;
        } else if ("".equalsIgnoreCase(string.trim())) {
            return true;
        }
        return false;
    }

    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    public static final Pattern PHONENUMBER_PATTERN = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
    public static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]*");
}
