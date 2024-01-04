package com.example.yalcin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;
public class DBManagerBooks {
    private SQLiteDatabase sqlDB;
    static final String DBName = "Books";
    static final String TableName = "BooksTable"; // Projects sınıfı için özel tablo adı
    static final String ColBookName = "BooksName"; // Projects sınıfına özel sütun adı
    static final String ColCurrentSitBO = "CurrentSitBO"; // Projects sınıfına özel sütun adı
    static final int DBVersion = 3;

    static final String CreateTable = "CREATE TABLE " + TableName +
            " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + ColBookName +
            " TEXT, " + ColCurrentSitBO + " TEXT)";

    static class DatabaseHelperBooks extends SQLiteOpenHelper {

        Context context;

        DatabaseHelperBooks(Context context) {
            super(context, DBName, null, DBVersion);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CreateTable);
            Toast.makeText(context, "Books table is created", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TableName);
            onCreate(db);
        }
    }

    public DBManagerBooks(Context context) {
        DatabaseHelperBooks db = new DatabaseHelperBooks(context);
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
