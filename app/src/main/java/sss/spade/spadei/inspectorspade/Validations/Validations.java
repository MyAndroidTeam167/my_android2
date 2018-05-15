package sss.spade.spadei.inspectorspade.Validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hp on 19-02-2018.
 */

public class Validations {

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            // if(phone.length() < 6 || phone.length() > 13) {
            if (phone.length() != 10) {
                check = false;
                //txtPhone.setError("Not Valid Number");
            } else {
                if(phone.startsWith("0"))
                {check=false;}
                else{
                    check = true;}
            }
        } else {
            check = false;
        }
        return check;
    }
}
