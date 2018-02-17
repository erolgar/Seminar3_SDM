package databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

import pojoObjects.Quotation;

/**
 * Created by x on 17/02/2018.
 */
@Dao
public interface QuotationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addQuotation(Quotation quotation);

    @Delete
    void deleteQuotation(Quotation quotation);

    @Query("SELECT * FROM quotations")
    ArrayList<Quotation> getQuotations();

    @Query("SELECT * FROM quotations WHERE quote LIKE :quotation_text")
    Quotation getQuotation(String quotation_text);

    @Query("DELETE FROM quotations")
    void deleteAllQuotations();
}
