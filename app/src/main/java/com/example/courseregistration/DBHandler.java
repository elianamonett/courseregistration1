package com.example.courseregistration;

// provides a database that communicates with sqlite

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "studentdb";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "students";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String PRIORITY_COL = "priority";
    private static final String COURSE_COL = "course";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + PRIORITY_COL + " TEXT,"
                + COURSE_COL + " TEXT)";

        db.execSQL(query);
    }

    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(ID_COL);
            int nameIndex = cursor.getColumnIndex(NAME_COL);
            int courseIndex = cursor.getColumnIndex(COURSE_COL);
            int priorityIndex = cursor.getColumnIndex(PRIORITY_COL);

            do {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String course = cursor.getString(courseIndex);
                String priority = cursor.getString(priorityIndex);
                Student student = new Student(name, id, course, priority);
                studentList.add(student);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return studentList;
    }


    public void addNewStudent(String studentName, String waitingCourse, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, studentName);
        values.put(COURSE_COL, waitingCourse);
        values.put(PRIORITY_COL, priority);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void removeStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_COL + "=?", new String[]{String.valueOf(studentId)});
        db.close();
    }

    public void editStudent(int studentId, String newName, String newCourse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, newName);
        values.put(COURSE_COL, newCourse);
        db.update(TABLE_NAME, values, ID_COL + "=?", new String[]{String.valueOf(studentId)});
        db.close();
    }

    public String getPriority(String studentName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String priority = null;
        Cursor cursor = db.query(TABLE_NAME, new String[]{PRIORITY_COL}, NAME_COL + "=?", new String[]{studentName}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int priorityIndex = cursor.getColumnIndex(PRIORITY_COL);
                if (priorityIndex != -1) {
                    priority = cursor.getString(priorityIndex);
                }
            }
            cursor.close();
        }
        return priority;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}