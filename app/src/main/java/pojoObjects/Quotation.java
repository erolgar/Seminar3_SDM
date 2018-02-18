package pojoObjects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


/**
 * Created by x on 08/02/2018.
 */
@Entity(tableName = "quotations", indices = {@Index(value = {"quote"}, unique = true)})
public class Quotation {

    public int getQuotation_id() {
        return quotation_id;
    }

    public void setQuotation_id(int quotation_id) {
        this.quotation_id = quotation_id;
    }

    @PrimaryKey(autoGenerate = true)
    private int quotation_id;
    @ColumnInfo(name = "quote")
    @NonNull
    private String quoteText;
    @ColumnInfo(name = "author")
    private String quoteAuthor;

    public Quotation() {

    }

    public Quotation(String quoteText, String quoteAuthor) {
        this.quoteText = quoteText;
        this.quoteAuthor = quoteAuthor;
    }

    public String getQuoteText() {
        return this.quoteText;
    }

    public void setQuoteText(String text) {
        this.quoteText = text;
    }

    public String getQuoteAuthor() {
        return this.quoteAuthor;
    }

    public void setQuoteAuthor(String text) {
        this.quoteAuthor = text;
    }
}
