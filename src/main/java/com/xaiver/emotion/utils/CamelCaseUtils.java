package com.xaiver.emotion.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

import static com.xaiver.emotion.constants.SchemaConstants.FLAG4;

public class CamelCaseUtils {

    public static String convertToCamelCase(String input) {
        if(!input.contains(FLAG4)){
            return input;
        }
        String[] split = input.split("_");
        StringBuffer buffer = new StringBuffer();
        char char0;
        String sp;
        for (int i = 0; i < split.length; i++) {
            sp = split[i];
            if(null==sp){
                continue;
            }
            sp = sp.trim();
            if (sp.length()<=0) {
                continue;
            }
            char0 = sp.charAt(0);
            if(0 == i){
                buffer.append(97>char0?char0+32:char0).append(sp.substring(1));
            }
            buffer.append(97>char0?char0:char0-32).append(sp.substring(1));
        }
        return buffer.toString();
    }
}
