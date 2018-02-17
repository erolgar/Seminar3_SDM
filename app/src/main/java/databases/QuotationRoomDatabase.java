package databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import pojoObjects.Quotation;

/**
 * Created by x on 17/02/2018.
 */

@Database(entities = {Quotation.class}, version = 1)
public abstract class QuotationRoomDatabase extends RoomDatabase {

    public abstract QuotationDao quotationDao();

    private static QuotationRoomDatabase quotationRoomDatabase;

    public synchronized static QuotationRoomDatabase getInstance(Context context) {
        if (quotationRoomDatabase == null) {
            quotationRoomDatabase = Room.databaseBuilder(context, QuotationRoomDatabase.class, "quotation_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return quotationRoomDatabase;
    }
}
