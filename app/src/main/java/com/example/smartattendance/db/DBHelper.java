package com.example.smartattendance.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartattendance.Model.StudentModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;
    private Context context;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        final String SQL_CREATE_STUDENT_TABLE = "CREATE TABLE " +
                DatabaseContract.StudentTable.TABLE_NAME + " ( " +
                DatabaseContract.StudentTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.StudentTable.NAME + " TEXT, " +
                DatabaseContract.StudentTable.DATE + " TEXT,  " +
                DatabaseContract.StudentTable.TIME + " TEXT  " +
                ")";


        db.execSQL(SQL_CREATE_STUDENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.StudentTable.TABLE_NAME);

        onCreate(db);
    }

    public long insertUser(StudentModel model){
        db=getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put( DatabaseContract.StudentTable.NAME,model.getsName());
        cv.put( DatabaseContract.StudentTable.DATE,model.getsDate());
        cv.put( DatabaseContract.StudentTable.TIME,model.getsTime());

        long id=db.insert(DatabaseContract.StudentTable.TABLE_NAME,null,cv);

        return id;
    }

    public void deleteAll() {
//        db=getWritableDatabase();
//        db.delete(DatabaseContract.StudentTable.TABLE_NAME,D)
        context.deleteDatabase(DATABASE_NAME);
    }

    public ArrayList<StudentModel> getAllStudents(){
        db=getReadableDatabase();
        ArrayList<StudentModel>list=new ArrayList<>();
        Cursor cursor=db.rawQuery("SELECT * FROM " +DatabaseContract.StudentTable.TABLE_NAME,null);

        if(cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.StudentTable.NAME));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.StudentTable.DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.StudentTable.TIME));
                list.add(new StudentModel(name, date, time));
            } while (cursor.moveToNext());
        }
        return list;
    }

}
