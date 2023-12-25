package com.example.tablereservation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TableReservation.db";
    private static final int DATABASE_VERSION = 1;

    // Table names and column names for user authentication
    private static final String TABLE_USERS = "allusers";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Table names and column names for booking information
    private static final String TABLE_REVIEWS = "reviews";
    private static final String COLUMN_REVIEW_ID = "review_id";
    private static final String COLUMN_FOOD_RATING = "food";
    private static final String COLUMN_SERVICE_RATING = "service";
    private static final String COLUMN_DESCRIPTION = "description";


    // SQL query to create the "allusers" table
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + "(" +
                    COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                    COLUMN_PASSWORD + " TEXT)";

    // SQL query to create the "review" table
    private static final String CREATE_TABLE_REVIEWS =
            "CREATE TABLE " + TABLE_REVIEWS + "(" +
                    COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_FOOD_RATING + " TEXT," +
                    COLUMN_SERVICE_RATING + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT)" ;

    // SQL query to drop the "allusers" and "bookings" tables if they exist
    private static final String DROP_TABLE_USERS = "DROP TABLE IF EXISTS " + TABLE_USERS;
    private static final String DROP_TABLE_REVIEWS = "DROP TABLE IF EXISTS " + TABLE_REVIEWS;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_REVIEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_USERS);
        db.execSQL(DROP_TABLE_REVIEWS);
        onCreate(db);
    }

    public boolean insertReviewData(String food, String service, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FOOD_RATING, food);
        contentValues.put(COLUMN_SERVICE_RATING, service);
        contentValues.put(COLUMN_DESCRIPTION, description);


        long result = db.insert(TABLE_REVIEWS, null, contentValues);
        return result != -1;
    }

    // Method to insert user data into the "allusers" table
    public boolean insertUserData(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    // Method to check if a username exists in the "allusers" table
    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});
        boolean usernameExists = cursor.getCount() > 0;
        cursor.close();
        return usernameExists;
    }

    // Method to check if a username and password combination exists in the "allusers" table
    public boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }}
