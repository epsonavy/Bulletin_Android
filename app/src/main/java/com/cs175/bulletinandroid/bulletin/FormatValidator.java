package com.cs175.bulletinandroid.bulletin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenyulong on 12/2/16.
 */
public class FormatValidator {

    private static final String EMAIL_PATTERN = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
    private static final String USER_NAME_PATTERN = "\\A\\w{3,22}\\z";
    private Pattern pattern;
    private Matcher matcher;
    public boolean validateEmail (String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validateUserName (String userName) {
        pattern = Pattern.compile(USER_NAME_PATTERN);
        matcher = pattern.matcher(userName);
        return matcher.matches();
    }


}
