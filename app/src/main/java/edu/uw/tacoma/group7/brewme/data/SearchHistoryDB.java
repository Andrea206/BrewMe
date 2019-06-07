/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/

package edu.uw.tacoma.group7.brewme.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import edu.uw.tacoma.group7.brewme.R;

public class SearchHistoryDB {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "SearchHistoryDB.db";
    private SQLiteDatabase mSQLiteDatabase;

    public SearchHistoryDB(Context context) {
        SearchHistoryDBHelper mSearchHistoryDBHelper = new SearchHistoryDBHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mSearchHistoryDBHelper.getWritableDatabase();
    }

    /**
     * Inserts the search history into the local sqlite table. Returns true if successful, false otherwise.
     * @param searchInput the text being typed
     * @return true or false
     */
    public boolean insertSearchHistory(String searchInput) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("searchInput", searchInput);
        long rowId = mSQLiteDatabase.insert("History", null, contentValues);
        return rowId != -1;
    }

    /**
     * Delete all the data from the Course
     */
    public void deleteHistory() {
        mSQLiteDatabase.delete("History", null, null);
    }


    /** Returns the search history from the local History table.
     * @return list ArrayList of Strings containing local search history.
     */
    public ArrayList<String> getHistory() {
        String[] columns = {"searchInput"};
        Cursor c = mSQLiteDatabase.query(
                "History",  // The table to query
                columns,         // The columns to return
                null,    // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null,    // don't group the rows
                null,     // don't filter by row groups
                null       // The sort order
        );
        c.moveToFirst();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i<c.getCount(); i++) {
            String searchInput = c.getString(0);
            list.add(searchInput);
            c.moveToNext();
        }
        return list;
    }

    /**
     * Checks existing table for data present. If data is present in table, returns false.
     * @return true if table is empty (no data in table).
     */
    public boolean isTableEmpty(){
        String count = "SELECT count(*) FROM History";
        Cursor cursor = mSQLiteDatabase.rawQuery(count, null);
        cursor.moveToFirst();
        int rCount = cursor.getInt(0);
        return rCount == 0;
    }

}//end SearchHistoryDB

/**
 * SearchHistoryDBHelper for creating sqlite tables.
 */
class SearchHistoryDBHelper extends SQLiteOpenHelper {

    private final String CREATE_HISTORY_SQL;
    private final String DROP_HISTORY_SQL;

    public SearchHistoryDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        CREATE_HISTORY_SQL = context.getString(R.string.CREATE_HISTORY_SQL);
        DROP_HISTORY_SQL = context.getString(R.string.DROP_HISTORY_SQL);
    }

    /**
     * OnCreate for executing table creation.
     * @param sqLiteDatabase search history local database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_HISTORY_SQL);
    }

    /**
     * onUpgrade execute drop table sql command.
     * @param sqLiteDatabase search history local database.
     * @param i integer value.
     * @param i1 second integer value.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_HISTORY_SQL);
        onCreate(sqLiteDatabase);
    }

}//end SearchHistoryDBHelper
