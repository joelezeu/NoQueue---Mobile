package com.noqueue.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Joel on 5/26/2016.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "noqueue";
    private static final String TABLE_USER = "customers";

    private static final String CUSTOMER_ID = "c_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    private static final String UNIQUE_ID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + CUSTOMER_ID + " INTEGER PRIMARY KEY," + FIRST_NAME + " TEXT,"
                + LAST_NAME + " TEXT," + EMAIL + " TEXT UNIQUE,"
                + UNIQUE_ID + " TEXT," + ")";
        sqLiteDatabase.execSQL(CREATE_LOGIN_TABLE);
        Log.d(TAG, "Database tables created");
        System.out.println("SQL syntax "+CREATE_LOGIN_TABLE);
        Log.d("SOL Syntax ", CREATE_LOGIN_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);
    }
    public void createUser(String first_name, String last_name, String email, String uid){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIRST_NAME, first_name);
        contentValues.put(LAST_NAME, last_name);
        contentValues.put(EMAIL, email);
        contentValues.put(UNIQUE_ID, uid);

        long c_id = sqLiteDatabase.insert(TABLE_USER, null, contentValues);
        sqLiteDatabase.close();

        Log.d(TAG, "New user inserted: " +c_id);
    }
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> customer = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            customer.put("first_name", cursor.getString(1));
            customer.put("last_name", cursor.getString(2));
            customer.put("email", cursor.getString(3));
            customer.put("uid", cursor.getString(4));
        }
        cursor.close();
        sqLiteDatabase.close();
        // return customer
        Log.d(TAG, "Fetching customer from Sqlite: " + customer.toString());

        return customer;
    }
    public void deleteUsers() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // Delete All Rows
        sqLiteDatabase.delete(TABLE_USER, null, null);
        sqLiteDatabase.close();
        Log.d(TAG, "Deleted all user info from sqlite");
    }
}