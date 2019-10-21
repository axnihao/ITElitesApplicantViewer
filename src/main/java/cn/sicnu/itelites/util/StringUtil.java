package cn.sicnu.itelites.util;

public class StringUtil {
    private StringUtil() {}
    public static String initCap(String str) {
        if (str == null || "".equals(str)) {
            return str ;
        }
        if (str.length() == 1) {
            return str.toUpperCase() ;
        }
        return str.substring(0,1).toUpperCase() + str.substring(1) ;
    }
}
