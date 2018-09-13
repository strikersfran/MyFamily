package ve.com.strikersfran.myfamily.data;

import android.provider.BaseColumns;

/**
 * Clase que establece los nombres a usar en la base de datos
 */
public class MyFamilyContract {

    public MyFamilyContract() {
    }

    public static class LastUpdate implements BaseColumns{
        static final String TABLE_NAME = "lastupdate";
        static final String COLUMN_NAME_TABLE = "table";
        static final String COLUMN_NAME_DATE = "date";
    }

    public static class MiFamilia implements BaseColumns{
        static final String TABLE_NAME = "mifamilia";
        static final String COLUMN_NAME_ID = "uid";
        static final String COLUMN_NAME_NAME = "nombre";
        static final String COLUMN_NAME_PAPELLIDO = "primer_apellido";
        static final String COLUMN_NAME_SAPELLIDO = "segundo_apellido";
        static final String COLUMN_NAME_EMAIL = "email";
        static final String COLUMN_NAME_PARENTESCO = "parentesco";
        static final String COLUMN_NAME_AVATAR = "avatar";
        static final String COLUMN_NAME_ESTATUS = "estatus";
        static final String COLUMN_NAME_LAST_UPDATE = "last_update";
    }

    public static class Solicitudes implements BaseColumns{
        static final String TABLE_NAME = "solicitudes";
        static final String COLUMN_NAME_ID = "uid";
        static final String COLUMN_NAME_NAME = "nombre";
        static final String COLUMN_NAME_PAPELLIDO = "primer_apellido";
        static final String COLUMN_NAME_SAPELLIDO = "segundo_apellido";
        static final String COLUMN_NAME_EMAIL = "email";
        static final String COLUMN_NAME_PARENTESCO = "parentesco";
        static final String COLUMN_NAME_AVATAR = "avatar";
        static final String COLUMN_NAME_ESTATUS = "estatus";
        static final String COLUMN_NAME_LAST_UPDATE = "last_update";
    }
}
