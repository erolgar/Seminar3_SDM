package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.admin.seminar3.R;

import java.util.ArrayList;

import pojoObjects.Quotation;


public class FavouriteQuotationsArrayAdapter extends ArrayAdapter<Quotation> {
    private Context context;
    private ArrayList<Quotation> data;

    public FavouriteQuotationsArrayAdapter(Context context, int resource, ArrayList<Quotation> quotationList) {
        super(context, resource, quotationList);
        this.context = context;
        this.data = quotationList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.quotation_list_row, null);
        }
        Quotation item = data.get(position);
        TextView textViewQuote = view.findViewById(R.id.text_view_quote);
        textViewQuote.setText(item.getQuoteText());
        TextView textViewAuthor = view.findViewById(R.id.text_view_author);
        textViewAuthor.setText(item.getQuoteAuthor());
        return view;
    }
}
