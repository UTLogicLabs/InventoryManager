package Common;

import java.util.regex.Pattern;

public class Data {
    private static final Pattern pattern = Pattern.compile("\\d+");

    public static boolean isNumeric(String strNumb) {
        if(strNumb == null) {
            return false;
        }
        return pattern.matcher(strNumb.replaceAll(" ", "")).matches();
    }
}
