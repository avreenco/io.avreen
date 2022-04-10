package io.avreen.http.channel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class testRegx
{
    public static void main(String[] args) {
        String   regx = "v\\$+|p\\$+" ;
        System.out.println(regx);
        String   val = "v$F_SMS_CONTENT" ;
        Pattern  pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(val);
        System.out.println(matcher.find());
    }
}
