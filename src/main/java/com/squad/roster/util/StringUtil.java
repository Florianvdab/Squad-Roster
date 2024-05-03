package com.squad.roster.util;

public abstract class StringUtil {
    public static boolean isNotNullOrEmpty(String string) {
        return string != null && !string.isEmpty();
    }
}
