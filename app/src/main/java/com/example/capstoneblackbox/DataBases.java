package com.example.capstoneblackbox;

import android.provider.BaseColumns;

public final class DataBases {
    public static final class CreateDB implements BaseColumns {
        public static final String VideoName = "videoID";
        public static final String _TABLENAME0 = "impactTable";
        public static final String ImpactTime = "impactTime";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +VideoName+" text not null , "
                +ImpactTime+" integer not null );";
    }
}
