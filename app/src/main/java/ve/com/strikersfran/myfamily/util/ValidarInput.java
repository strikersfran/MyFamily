package ve.com.strikersfran.myfamily.util;

/**
 * Created by Francisco Carrion on 29/08/2018.
 */

public class ValidarInput {

    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+");
    }

}
