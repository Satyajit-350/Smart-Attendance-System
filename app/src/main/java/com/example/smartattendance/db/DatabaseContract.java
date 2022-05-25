package com.example.smartattendance.db;

import android.provider.BaseColumns;

public class DatabaseContract {

    public static class StudentTable implements BaseColumns{

        public static final String TABLE_NAME = "student_table";
        public static final String _ID = "_id";
        public static final String DATE = "date";
        public static final String NAME = "name";
        public static final String TIME = "time";

    }
}
