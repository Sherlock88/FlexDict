package in.dipanjan.flexdict;


import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/*
* http://www.androidhive.info/2011/11/android-sqlite-database-tutorial
* http://stackoverflow.com/questions/15383847/how-to-get-all-table-names-in-android-sqlite-database
*/

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private String dbPath;

    public DatabaseHandler(Context context, String dbPath) {
        super(context, dbPath, null, DATABASE_VERSION);
        this.dbPath = dbPath;
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    // Getting single word
    Word getWord(String dbTable, int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + dbTable + " WHERE ID = ?";
        String whereArgs[] = new String[] { String.valueOf(ID) };

        /* http://stackoverflow.com/questions/10600670/sqlitedatabase-query-method */
        Cursor cursor = db.rawQuery(selectQuery, whereArgs);
        if (cursor != null)
            cursor.moveToFirst();
        Word word = new Word(ID, cursor.getInt(1), cursor.getString(2), cursor.getString(3),
                            cursor.getString(4), cursor.getInt(5), cursor.getString(6),
                            cursor.getString(7), cursor.getBlob(8));

        // Return word
        return word;
    }


    // Getting all words
    public ArrayList<Word> getAllWords(String dbTable) {
        ArrayList<Word> wordList = new ArrayList<Word>();

        // Select all query
        String selectQuery = "SELECT * FROM " + dbTable;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Word word = new Word(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3),
                                    cursor.getString(4), cursor.getInt(5), cursor.getString(6),
                                    cursor.getString(7), cursor.getBlob(8));

                wordList.add(word);
            } while (cursor.moveToNext());
        }

        // Return word list
        return wordList;
    }


    // Getting all words
    public ArrayList<String> getAllWordsOnly(String dbTable) {
        ArrayList<String> wordList = new ArrayList<String>();

        // Select all query
        String selectQuery = "SELECT Word FROM " + dbTable;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String word = cursor.getString(0);
                wordList.add(word);
            } while (cursor.moveToNext());
        }

        // Return word list
        return wordList;
    }


    public ArrayList<String> getTableNames() {
        ArrayList<String> wordList = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name", null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String table_name = cursor.getString(cursor.getColumnIndex("name"));
                wordList.add(table_name);
                cursor.moveToNext();
            }
        }

        return wordList;
    }


    public ArrayList<String> getTableCount() {
        ArrayList<String> wordList = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT FROM sqlite_master WHERE type='table' ORDER BY name", null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String table_name = cursor.getString(cursor.getColumnIndex("name"));
                wordList.add(table_name);
                cursor.moveToNext();
            }
        }

        return wordList;
    }


    // Getting word count
    public int getWordCount(String dbTable) {
        String countQuery = "SELECT * FROM " + dbTable;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int wordCount = cursor.getCount();
        cursor.close();

        // Return count
        return wordCount;
    }
}