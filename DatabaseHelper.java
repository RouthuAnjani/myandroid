package com.example.kpitusers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EmployeeDB";
    private static final String TABLE_NAME = "employees";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_IMAGE_URI = "image_uri";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT,"
                + COLUMN_PHONE + " TEXT," + COLUMN_IMAGE_URI + " TEXT" + ")";
        db.execSQL(CREATE_EMPLOYEES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addEmployee(Employee employee, Uri imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, employee.getName());
        values.put(COLUMN_PHONE, employee.getPhoneNumber());
        // Check if image URI is not null before putting it in the ContentValues
        if (imageUri != null) {
            values.put(COLUMN_IMAGE_URI, imageUri.toString());
        }
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public Cursor getLastEmployee() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";
        return db.rawQuery(query, null);
    }

    public long updateEmployee(Employee employee, Uri imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, employee.getName());
        values.put(COLUMN_PHONE, employee.getPhoneNumber());
        // Check if image URI is not null before putting it in the ContentValues
        if (imageUri != null) {
            values.put(COLUMN_IMAGE_URI, imageUri.toString());
        }
        long rowsAffected = db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(employee.getId())});
        db.close();
        return rowsAffected;
    }

    // Method to get all employees from the database
    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Employee employee = new Employee();
                employee.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                employee.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                employee.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
                // Retrieve the image URI from the cursor and set it in the Employee object
                String imageUriString = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URI));
                if (imageUriString != null) {
                    employee.setImageUri(Uri.parse(imageUriString));
                }
                employeeList.add(employee);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return employeeList;
    }

    // Method to delete an employee from the database
    public boolean deleteEmployee(long employeeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(employeeId)});
        return deletedRows > 0;
    }
}
