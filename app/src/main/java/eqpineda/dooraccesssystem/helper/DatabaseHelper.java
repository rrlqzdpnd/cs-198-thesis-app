package eqpineda.dooraccesssystem.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    /**
     * Inserts authKey to table
     * @param key
     * @return keyId of inserted key
     */
    public long insertKey(Keys key) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TBL_KEYS_AUTHSTRING, key.getAuthstring());
        values.put(TBL_KEYS_ISDELETED, key.getIsdeleted());
        values.put(TBL_KEYS_INSERTEDON, getDateTime());

        return db.insert(TBL_KEYS, null, values);
    }

    /**
     * Gets all active keys
     * @return
     */
    public List<Keys> getKeys() {
        List<Keys> keys = new ArrayList<Keys>();
        String query = "SELECT * FROM " + TBL_KEYS + " WHERE isdeleted = false";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()) {
            do {
                Keys key = new Keys();
                key.setKeyid(c.getInt(c.getColumnIndex(TBL_KEYS_KEYID)));
                key.setAuthstring(c.getString(c.getColumnIndex(TBL_KEYS_AUTHSTRING)));
                key.setInsertedon(c.getString(c.getColumnIndex(TBL_KEYS_INSERTEDON)));

                keys.add(key);
            } while(c.moveToNext());
        }

        return keys;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
