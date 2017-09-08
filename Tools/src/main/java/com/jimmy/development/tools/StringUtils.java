package com.jimmy.development.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jinguochong on 16-8-24.
 */
public class StringUtils {
    public static boolean isNumer(String str) {
        if(str == null){
            str = "";
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public final static String ACCOUNT_INPUT_MATCHER = "^[a-zA-Z0-9_.]+$";
    public final static String PASSWORD_MATCHER = "^[a-zA-Z]w{5,17}$";

    public static boolean isContentLegal(String source, String matcher) {
        if(source == null){
            source = "";
        }
        boolean match = false;
        Pattern p = Pattern.compile(matcher);
        Matcher m = p.matcher(source);
        if (m.find()) {
            match = true;
        }
        return match;
    }

}
