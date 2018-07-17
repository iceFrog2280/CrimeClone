package com.example.icefrog.crimeclone;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import static com.example.icefrog.crimeclone.CrimeDbScheme.*;

/**
 * Created by icefrog on 2017/8/15 0015.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Crime getCrime(){
        String uuid = getString( getColumnIndex( CrimeTalbe.Cols.UUID));
        String title = getString( getColumnIndex( CrimeTalbe.Cols.TITLE));
        String suspect = getString( getColumnIndex( CrimeTalbe.Cols.SUSPECT));
        long date = getLong( getColumnIndex( CrimeTalbe.Cols.DATE));
        boolean isSolved = getInt( getColumnIndex( CrimeTalbe.Cols.SOLVED)) != 0;

        Crime crime = new Crime( UUID.fromString( uuid));
        crime.setTitle( title);
        crime.setDate( new Date(date));
        crime.setSuspect( suspect);
        crime.setSolved( isSolved);

        return crime;
    }
}
