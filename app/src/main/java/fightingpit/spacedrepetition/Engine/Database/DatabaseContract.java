//package fightingpit.spacedrepetition.Engine.Database;
//
//import android.provider.BaseColumns;
//
//import com.raizlabs.android.dbflow.annotation.Database;
//
///**
// * Database Schema.
// */
//@Database(name = DatabaseContract.DATABASE_NAME, version = DatabaseContract.DATABASE_VERSION)
//public final class DatabaseContract {
//
//    public static final int DATABASE_VERSION = 1;
//    public static final String DATABASE_NAME = "database.db";
//    public static final String[] SQL_CREATE_TABLE_ARRAY = {
//            RepetitionPattern.CREATE_TABLE,
//            RepetitionPatternSpace.CREATE_TABLE,
//            TaskDetails.CREATE_TABLE,
//            ScheduledTasks.CREATE_TABLE,
//    };
//    public static final String[] SQL_DROP_TABLE_ARRAY = {
//            RepetitionPattern.DROP_TABLE,
//            RepetitionPatternSpace.DROP_TABLE,
//            TaskDetails.DROP_TABLE,
//            ScheduledTasks.DROP_TABLE,
//    };
//    private static final String COMMA_SEP = ", ";
//
//
//    // To prevent someone from accidentally instantiating the contract class,
//    // give it an empty private constructor.
//    private DatabaseContract() {
//    }
//
//    public static abstract class RepetitionPattern implements BaseColumns {
//
//        public static final String TABLE_NAME = "REPETITION_PATTERN";
//        public static final String ID = "ID";
//        public static final String NAME = "NAME";
//        public static final String REPETITIONS = "REPETITIONS";
//
//        public static final String CREATE_TABLE = "CREATE TABLE " +
//                TABLE_NAME + " (" +
//                ID + " TEXT PRIMARY KEY NOT NULL" + COMMA_SEP +
//                NAME + " TEXT NOT NULL" + COMMA_SEP +
//                REPETITIONS + " INTEGER NOT NULL" +
//                " )";
//
//        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
//    }
//
//    public static abstract class RepetitionPatternSpace implements BaseColumns {
//
//        public static final String TABLE_NAME = "REPETITION_PATTERN_SPACE";
//        public static final String ID = "ID";
//        public static final String REPETITION_NUMBER = "REPETITION_NUMBER";
//        public static final String SPACE = "SPACE";
//
//        public static final String CREATE_TABLE = "CREATE TABLE " +
//                TABLE_NAME + " (" +
//                ID + " TEXT NOT NULL" + COMMA_SEP +
//                REPETITION_NUMBER + " INTEGER NOT NULL" + COMMA_SEP +
//                SPACE + " INTEGER NOT NULL" + COMMA_SEP +
//                " PRIMARY KEY (" + ID + COMMA_SEP + REPETITION_NUMBER + ")" +
//                " )";
//
//        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
//    }
//
//    public static abstract class TaskDetails implements BaseColumns {
//
//        public static final String TABLE_NAME = "TASK_DETAILS";
//        public static final String ID = "ID";
//        public static final String NAME = "NAME";
//        public static final String COMMENT = "COMMENT";
//        public static final String PATTERN_ID = "PATTERN_ID";
//        public static final String CURRENT_REPETITION = "CURRENT_REPETITION";
//
//        public static final String CREATE_TABLE = "CREATE TABLE " +
//                TABLE_NAME + " (" +
//                ID + " TEXT PRIMARY KEY NOT NULL" + COMMA_SEP +
//                NAME + " TEXT NOT NULL" + COMMA_SEP +
//                COMMENT + " TEXT " + COMMA_SEP +
//                PATTERN_ID + " TEXT NOT NULL" + COMMA_SEP +
//                CURRENT_REPETITION + " INTEGER" +
//                " )";
//
//        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
//    }
//
//    public static abstract class ScheduledTasks implements BaseColumns {
//
//        public static final String TABLE_NAME = "SCHEDULED_TASKS";
//        public static final String ID = "ID";
//        public static final String NAME = "NAME";
//        public static final String TIME = "TIME";
//
//        public static final String CREATE_TABLE = "CREATE TABLE " +
//                TABLE_NAME + " (" +
//                ID + " TEXT PRIMARY KEY NOT NULL" + COMMA_SEP +
//                NAME + " TEXT NOT NULL" + COMMA_SEP +
//                TIME + " TEXT NOT NULL" +
//                " )";
//
//        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
//    }
//
//}
