package ve.com.strikersfran.myfamily.data;

import android.content.Context;
import android.content.SharedPreferences;

import ve.com.strikersfran.myfamily.User;

/**
 * Created by Francisco Carrion on 28/08/2018.
 */

public class SharedPreferenceHelper {

    private static SharedPreferenceHelper instance = null;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static String SHARE_USER_INFO = "userinfo";
    private static String SHARE_KEY_NOMBRE = "nombre";
    private static String SHARE_KEY_PAPELLIDO = "primer_apellido";
    private static String SHARE_KEY_SAPELLIDO = "segundo_apellido";
    private static String SHARE_KEY_EMAIL = "email";
    private static String SHARE_KEY_AVATAR = "avatar";
    private static String SHARE_KEY_UID = "uid";


    private SharedPreferenceHelper() {}

    public static SharedPreferenceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceHelper();
            preferences = context.getSharedPreferences(SHARE_USER_INFO, Context.MODE_PRIVATE);
            editor = preferences.edit();
        }
        return instance;
    }

    public void saveUserInfo(User user) {
        editor.putString(SHARE_KEY_NOMBRE, user.getNombre());
        editor.putString(SHARE_KEY_PAPELLIDO, user.getPrimerApellido());
        editor.putString(SHARE_KEY_SAPELLIDO, user.getSegundoApellido());
        editor.putString(SHARE_KEY_EMAIL, user.getEmail());
        editor.putString(SHARE_KEY_AVATAR, user.getAvatar());
        editor.putString(SHARE_KEY_UID, StaticConfig.UID);
        editor.apply();
    }

    public User getUserInfo(){
        String nombre = preferences.getString(SHARE_KEY_NOMBRE, "");
        String papellido = preferences.getString(SHARE_KEY_PAPELLIDO, "");
        String sapellido = preferences.getString(SHARE_KEY_SAPELLIDO,"");
        String email = preferences.getString(SHARE_KEY_EMAIL,"");
        String avatar = preferences.getString(SHARE_KEY_AVATAR, "default");
        String uid = preferences.getString(SHARE_KEY_UID, "");

        User user = new User(nombre,papellido,sapellido,avatar,email,uid);

        return user;
    }

    public String getUID(){
        return preferences.getString(SHARE_KEY_UID, "");
    }
}
