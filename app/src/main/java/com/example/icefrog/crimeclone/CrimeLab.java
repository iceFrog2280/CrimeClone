package com.example.icefrog.crimeclone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.icefrog.crimeclone.CrimeDbScheme.*;

/**
 * Created by icefrog on 2017/8/14 0014.
 */

public class CrimeLab {
    private static String TAG = "CrimeLab";
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private CrimeBaseHelper mCrimeBaseHelper;
    private SQLiteDatabase mCrimeDatabase;

    private CrimeLab(Context context){
        mContext = context.getApplicationContext();
        mCrimeBaseHelper = new CrimeBaseHelper(mContext);
        mCrimeDatabase = mCrimeBaseHelper.getWritableDatabase();
    }

    public static CrimeLab get( Context context){
        if( sCrimeLab == null){
            sCrimeLab = new CrimeLab( context);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimeList(){
        List<Crime> list = new ArrayList<>();
        CrimeCursorWrapper cursorWrapper = queryDatabase(null, null);
        // TODO: 2017/8/15 0015 换一种思路实现
        try{
            if(cursorWrapper.moveToFirst()){
                do {
                    list.add( cursorWrapper.getCrime());
                }while (cursorWrapper.moveToNext());
            }
        }finally {
            cursorWrapper.close();
        }
        return list;
    }

    public Crime getCrime(UUID uuid){
        CrimeCursorWrapper cursorWrapper = queryDatabase( CrimeTalbe.Cols.UUID + " = ?", new String[]{ uuid.toString()});
        try{
            if( cursorWrapper.getCount() == 0) return null;
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        }finally {
            cursorWrapper.close();
        }
    }

    public void addCrime( Crime crime){
        mCrimeDatabase.insert( CrimeTalbe.NAME, null, getContentValues(crime));
    }

    public void updataCrime( Crime crime){
        ContentValues values = getContentValues( crime);
        mCrimeDatabase.update( CrimeTalbe.NAME, values, CrimeTalbe.Cols.UUID+" = ?", new String[]{ crime.getId().toString()});
    }

    public void deletCrime( Crime crime){
        mCrimeDatabase.delete( CrimeTalbe.NAME, CrimeTalbe.Cols.UUID + " = ?", new String[]{ crime.getId().toString()});
    }

    private CrimeCursorWrapper queryDatabase(String selection, String[] selectionArgs){
        Cursor cursor = mCrimeDatabase.query(
                CrimeTalbe.NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        return new CrimeCursorWrapper(cursor);
    }

    private ContentValues getContentValues( Crime crime){
        //// TODO: 2017/8/14 0014  
        ContentValues values = new ContentValues();
        values.put( CrimeTalbe.Cols.UUID, crime.getId().toString());
        values.put( CrimeTalbe.Cols.TITLE, crime.getTitle());
        values.put( CrimeTalbe.Cols.DATE, crime.getDate().getTime());
        values.put( CrimeTalbe.Cols.SOLVED, crime.isSolved()?1:0 );//??
        values.put( CrimeTalbe.Cols.SUSPECT, crime.getSuspect());
        return  values;
    }

    public File getPhotoFile( Crime crime){
        File extraDir = mContext.getExternalFilesDir( Environment.DIRECTORY_PICTURES);
        if( extraDir == null) return  null;
        return new File( extraDir,crime.getPhotoFileName());
    }
}
