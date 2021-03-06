package eqpineda.dooraccesssystem.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import eqpineda.dooraccesssystem.model.Keys;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database name
    private static final String DATABASE_NAME = "userkeys";

    // Database version
    private static final int DATABASE_VERSION = 5;

    // Keys table
    private static final String TBL_KEYS = "keys";
    private static final String TBL_KEYS_KEYID = "keyid";
    private static final String TBL_KEYS_AUTHSTRING = "authstring";
    private static final String TBL_KEYS_PIN = "pin";
    private static final String TBL_KEYS_DESCRIPTION = "description";
    private static final String TBL_KEYS_NOTES = "notes";
    private static final String TBL_KEYS_INSERTEDON = "insertedon";
    private static final String TBL_KEYS_ISDELETED = "isdeleted";

    private static final String CREATE_TBL_KEYS = "CREATE TABLE " + TBL_KEYS + "( " + TBL_KEYS_KEYID
            + " INTEGER PRIMARY KEY NOT NULL, " + TBL_KEYS_AUTHSTRING + " VARCHAR(16) NOT NULL, " +
            TBL_KEYS_PIN + " VARCHAR(4) NOT NULL, " + TBL_KEYS_DESCRIPTION +
            " VARCHAR(32) NOT NULL, " + TBL_KEYS_NOTES + " TEXT, " + TBL_KEYS_INSERTEDON +
            " DATETIME, " + TBL_KEYS_ISDELETED + " INTEGER NOT NULL DEFAULT 0 )";

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
     * @param key Keys object to be inserted into the table
     * @return keyId of inserted key
     */
    public long insertKey(Keys key) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TBL_KEYS_AUTHSTRING, key.getAuthstring());
        values.put(TBL_KEYS_PIN, key.getPin());
        values.put(TBL_KEYS_DESCRIPTION, key.getDescription());
        values.put(TBL_KEYS_NOTES, key.getNotes());
        values.put(TBL_KEYS_ISDELETED, key.getIsdeleted());
        values.put(TBL_KEYS_INSERTEDON, getDateTime());

        return db.insert(TBL_KEYS, null, values);
    }

    /**
     * Gets all active keys
     * @return List of Keys object
     */
    public List<Keys> getKeys() {
        List<Keys> keys = new ArrayList<>();
        String query = "SELECT * FROM " + TBL_KEYS + " WHERE " + TBL_KEYS_ISDELETED + " = 0 ORDER" +
                " BY " + TBL_KEYS_INSERTEDON + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()) {
            do {
                Keys key = new Keys();
                key.setKeyid(c.getInt(c.getColumnIndex(TBL_KEYS_KEYID)));
                key.setAuthstring(c.getString(c.getColumnIndex(TBL_KEYS_AUTHSTRING)));
                key.setPin(c.getString(c.getColumnIndex(TBL_KEYS_PIN)));
                key.setDescription(c.getString(c.getColumnIndex(TBL_KEYS_DESCRIPTION)));
                key.setNotes(c.getString(c.getColumnIndex(TBL_KEYS_NOTES)));
                key.setInsertedon(c.getString(c.getColumnIndex(TBL_KEYS_INSERTEDON)));

                keys.add(key);
            } while(c.moveToNext());
        }
        c.close();

        return keys;
    }

    public Keys getKeyDetails(int keyId) {
        Keys key = null;
        String query = "SELECT * FROM " + TBL_KEYS + " WHERE " + TBL_KEYS_ISDELETED + " = 0 AND " +
                TBL_KEYS_KEYID + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, new String[] { keyId + "" });

        if(c.getCount() == 1) {
            c.moveToFirst();
            key = new Keys();
            key.setKeyid(c.getInt(c.getColumnIndex(TBL_KEYS_KEYID)));
            key.setAuthstring(c.getString(c.getColumnIndex(TBL_KEYS_AUTHSTRING)));
            key.setPin(c.getString(c.getColumnIndex(TBL_KEYS_PIN)));
            key.setDescription(c.getString(c.getColumnIndex(TBL_KEYS_DESCRIPTION)));
            key.setNotes(c.getString(c.getColumnIndex(TBL_KEYS_NOTES)));
            key.setInsertedon(c.getString(c.getColumnIndex(TBL_KEYS_INSERTEDON)));
        }

        c.close();
        db.close();

        return key;
    }

    public void deleteKey(int keyId) {
        String where = TBL_KEYS_KEYID + " = ?";
        String[] whereArgs = { keyId + "" };
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TBL_KEYS, where, whereArgs);
        db.close();
    }

    public void clearAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TBL_KEYS, null, null);
        db.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
