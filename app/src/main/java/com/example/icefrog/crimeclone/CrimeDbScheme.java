package com.example.icefrog.crimeclone;

/**
 * Created by icefrog on 2017/8/14 0014.
 */

public class CrimeDbScheme {
    public static final class CrimeTalbe{
        public static final String NAME = "crimes";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
