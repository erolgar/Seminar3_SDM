package databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import pojoObjects.Quotation;

/**
 * Created by x on 15/02/2018.
 */

public class SQLiteOpenHelperSeminarDatabase extends SQLiteOpenHelper {
    private static final String QUOTATIONS_TABLE_NAME = "quotations";
    private static final String COLUMN_QUOTE = "quote";
    private static final String COLUMN_AUTHOR = "author";

    private static SQLiteOpenHelperSeminarDatabase uniqueInstance = null;

    private SQLiteOpenHelperSeminarDatabase(Context context) {
        super(context, "quotation_database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        try {
            database.execSQL("CREATE TABLE quotations (quotation_id INTEGER PRIMARY KEY AUTOINCREMENT, quote TEXT NOT NULL, author TEXT, UNIQUE(quote));");
        } catch (SQLException sqle) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int c, int v) {
    }

    public static synchronized SQLiteOpenHelperSeminarDatabase getInstance(Context context) {
        if (uniqueInstance == null) {
            uniqueInstance = new SQLiteOpenHelperSeminarDatabase(context);
        }
        return uniqueInstance;
    }

    public ArrayList<Quotation> getQuotations(Context context) {
        SQLiteOpenHelperSeminarDatabase helper = SQLiteOpenHelperSeminarDatabase.getInstance(context);

        SQLiteDatabase database = helper.getReadableDatabase();
        helper.onCreate(database);
        Cursor cursor = database.query("quotations", new String[]{COLUMN_QUOTE, COLUMN_AUTHOR}, null, null, null, null, null);

        ArrayList<Quotation> quotations = new ArrayList<>();

        while (cursor.moveToNext()) {
            quotations.add(new Quotation(cursor.getString(cursor.getColumnIndex(COLUMN_QUOTE)), cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR))));
        }
        cursor.close();
        database.close();
        return quotations;
    }

    public Boolean isQuotationFavourite(Context context, Quotation quotation) {
        SQLiteOpenHelperSeminarDatabase helper = SQLiteOpenHelperSeminarDatabase.getInstance(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        helper.onCreate(database);
        Cursor cursor = database.query(QUOTATIONS_TABLE_NAME, null, COLUMN_QUOTE + "=?", new String[]{quotation.getQuoteText()}, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            database.close();
            return true;
        }
        cursor.close();
        database.close();
        return false;
    }

    public void insertQuotation(Context context, String quote, String author) {
        SQLiteOpenHelperSeminarDatabase helper = SQLiteOpenHelperSeminarDatabase.getInstance(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues quotation = new ContentValues();
        quotation.put(COLUMN_QUOTE, quote);
        quotation.put(COLUMN_AUTHOR, author);

        database.insert(QUOTATIONS_TABLE_NAME, null, quotation);
        database.close();
    }

    public void deleteAllQuotations(Context context) {
        SQLiteOpenHelperSeminarDatabase helper = SQLiteOpenHelperSeminarDatabase.getInstance(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        database.delete(QUOTATIONS_TABLE_NAME, null, null);
        database.close();
    }

    public void deleteQuotation(Context context, Quotation quotation) {
        SQLiteOpenHelperSeminarDatabase helper = SQLiteOpenHelperSeminarDatabase.getInstance(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        database.delete(QUOTATIONS_TABLE_NAME, COLUMN_QUOTE + "=?", new String[]{quotation.getQuoteText()});
        database.close();
    }
}
