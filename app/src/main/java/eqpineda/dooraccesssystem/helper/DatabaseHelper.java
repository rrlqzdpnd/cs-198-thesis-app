package eqpineda.dooraccesssystem.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import eqpineda.dooraccesssystem.model.Keys;

/**
 * Created by eqpineda on 4/10/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Database name
    public static final String DATABASE_NAME = "userkeys";

    // Databse version
    public static final int DATABASE_VERSION = 1;

    // Keys table
    public static final String TBL_KEYS = "keys";
    public static final String TBL_KEYS_KEYID = "keyid";
    public static final String TBL_KEYS_AUTHSTRING = "authstring";
    public static final String TBL_KEYS_INSERTEDON = "insertedon";
    public static final String TBL_KEYS_ISDELETED = "isdeleted";

    public static final String CREATE_TBL_KEYS = "CREATE TABLE " + TBL_KEYS + "( " + TBL_KEYS_KEYID + " INTEGER PRIMARY KEY, " + TBL_KEYS_AUTHSTRING + " VARCHAR(16) REQUIRED, "
            + TBL_KEYS_INSERTEDON + "DATETIME REQUIRED, " + TBL_KEYS_ISDELETED + " DEFAULT false )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL_KEYS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_KEYS);

        onCreate(db);
    }

    public long insertKey(Keys key) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TBL_KEYS_AUTHSTRING, key.getAuthstring());
        values.put(TBL_KEYS_INSERTEDON, getDateTime());
        values.put(TBL_KEYS_ISDELETED, key.getIsdeleted());

        return db.insert(TBL_KEYS, null, values);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
