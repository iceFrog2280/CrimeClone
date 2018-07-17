package com.example.icefrog.crimeclone;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by icefrog on 2017/8/14 0014.
 */

public class StyleFormat {
    public static String Date(Date date){
        return DateFormat.format("EEE,MM dd,yyyy", date).toString();
    }

    public static void showNoWork(Context context){
        Toast.makeText( context, "the unit don't work", Toast.LENGTH_LONG);
    }
}
