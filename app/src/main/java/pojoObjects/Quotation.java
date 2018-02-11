package pojoObjects;

/**
 * Created by x on 08/02/2018.
 */

public class Quotation {
    private String quoteText;
    private String quoteAuthor;

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
