package org.ckCoder.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Verification {
    public static boolean numeric(String value) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(value);
        return  matcher.matches();
    }

    public static boolean emailVerificatio(String email) {
        Pattern pattern = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
