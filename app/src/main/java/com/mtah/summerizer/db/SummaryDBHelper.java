package com.mtah.summerizer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.mtah.summerizer.model.Summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class SummaryDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Summary.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "summary";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TEXT = "text";

    private SQLiteDatabase db;

    public SummaryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        final String CREATE_SUMMARY_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_NAME + " VARCHAR PRIMARY KEY, " +
                COLUMN_TEXT + " VARCHAR)";

        db.execSQL(CREATE_SUMMARY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void addSummary(Summary summary){
        ContentValues value = new ContentValues();
        value.put("name", summary.getName());
        value.put("text", summary.getText());
        db = getWritableDatabase();
        db.insert(TABLE_NAME, null, value);
    }

    public void saveSummary(String name, String text){
        addSummary(new Summary(name, text));
    }

    public List<Summary> getAllSummaries(){
        List<Summary> summaryList = new ArrayList<>();
        db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if(cursor.moveToFirst()){
            do {
                Summary summary = new Summary();
                summary.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                summary.setText(cursor.getString(cursor.getColumnIndex(COLUMN_TEXT)));
                summaryList.add(summary);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return summaryList;
    }
}
