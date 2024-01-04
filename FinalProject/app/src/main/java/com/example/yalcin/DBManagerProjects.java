package com.example.yalcin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;
public class DBManagerProjects {
    private SQLiteDatabase sqlDB;
    static final String DBName = "Projects";
    static final String TableName = "ProjectsTable"; // Projects sınıfı için özel tablo adı
    static final String ColProjectsName = "ProjectsName"; // Projects sınıfına özel sütun adı
    static final String ColCurrentSitP = "CurrentSitP"; // Projects sınıfına özel sütun adı
    static final int DBVersion = 3;

    static final String CreateTable = "CREATE TABLE " + TableName +
            " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + ColProjectsName +
            " TEXT, " + ColCurrentSitP + " TEXT)";

    static class DatabaseHelperProjects extends SQLiteOpenHelper {

        Context context;

        DatabaseHelperProjects(Context context) {
            super(context, DBName, null, DBVersion);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CreateTable);
            Toast.makeText(context, "Projects table is created", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TableName);
            onCreate(db);
        }
    }

    public DBManagerProjects(Context context) {
        DatabaseHelperProjects db = new DatabaseHelperProjects(context);
        sqlDB = db.getWritableDatabase();
    }

    public long Insert(ContentValues values) {
        long ID = sqlDB.insert(TableName, "", values);

        return ID;
    }

    public Cursor query(String[] Projection, String Selection, String[] SelectionArgs, String SortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TableName);

        Cursor cursor = qb.query(sqlDB, Projection, Selection, SelectionArgs, null, null, SortOrder);
        return cursor;
    }

    public int delete(long id) {
        // Silme işlemi için sorgu oluştur
        String whereClause = "ID = ?";
        String[] whereArgs = {String.valueOf(id)};

        // Silme işlemini gerçekleştir
        int rowCount = sqlDB.delete(TableName, whereClause, whereArgs);

        // Silinen satır sayısını döndür
        return rowCount;
    }
}
