package com.example.icefrog.crimeclone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.icefrog.crimeclone.CrimeDbScheme.*;

/**
 * Created by icefrog on 2017/8/14 0014.
 */

public class CrimeBaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "CrimeBaseHelper";
    private static String DATABASE_NAME = "crimeBase.db";
    private static int VERSION = 1;//需要更新数据库 需自行更改version
    private static String CREATE_TABLE = "create table "+ CrimeTalbe.NAME + "("
            + "id" + " integer primary key autoincrement,"
            + CrimeTalbe.Cols.UUID + " text,"
            + CrimeTalbe.Cols.DATE + " integer,"
            + CrimeTalbe.Cols.SOLVED + " integer,"
            + CrimeTalbe.Cols.TITLE + " text,"
            + CrimeTalbe.Cols.SUSPECT + " text"
            + ")";


    public CrimeBaseHelper( Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+CrimeTalbe.NAME);
        onCreate( db);
    }
}
