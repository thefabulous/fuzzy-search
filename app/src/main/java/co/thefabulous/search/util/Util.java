package co.thefabulous.search.util;


import javax.annotation.Nullable;

public class Util {
    public static int compare(int lhs, int rhs) {
        return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }

    public static boolean isNullOrEmpty(@Nullable String s) {
        return s == null || s.length() == 0;
    }
}
