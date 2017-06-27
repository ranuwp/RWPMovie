package id.ranuwp.greetink.rwpmovie.moviecontentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import id.ranuwp.greetink.rwpmovie.moviecontentprovider.MovieContract.MovieTable;
/**
 * Created by ranuwp on 6/24/2017.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moviesDB.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE =
                "CREATE TABLE "+ MovieContract.MovieTable.TABLE_NAME + " ("+
                        MovieTable._ID+" INTEGER PRIMARY KEY, "+
                        MovieTable.COLUMN_TITLE+" TEXT NOT NULL, "+
                        MovieTable.COLUMN_POSTER_URL+" TEXT NOT NULL, "+
                        MovieTable.COLUMN_BACKDROP_URL+" TEXT NOT NULL, "+
                        MovieTable.COLUMN_OVERVIEW+" TEXT NOT NULL, "+
                        MovieTable.COLUMN_VOTE_AVERAGE+" REAL , "+
                        MovieTable.COLUMN_RELEASE_DATE+" TEXT NOT NULL );";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+MovieTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
