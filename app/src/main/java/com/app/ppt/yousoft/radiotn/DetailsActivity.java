package com.app.ppt.yousoft.radiotn;
/**
 * Created by omar reg on 21/10/2015.
 */

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class DetailsActivity extends ListActivity {
    ListView listView;
    ArrayList<RowItem> rowItems;
    EditText st;
    String[] ractivities={"","MosaiqueActivity","","","","","","","","","","","","","",""};
    String[] titles={"Shems FM","Mosaique FM","Express FM","Cap FM","Diwan FM","Jawhara FM","IFM","Sabra FM","Oasis FM","MFM","Oxygene FM","Ulysse FM","Knooz FM","Radio 6 FM","Twenssa FM","Saraha FM"};
    String[] descriptions={"Tunis","Tunis","Tunis","Nabeul","Sfax","Sousse","Tunis","Kairouan","Gabès","Sousse","Bizerte","Djerba","Sousse","Tunis","Tunis","Tunis"};
    int[] images={R.drawable.station_1, R.drawable.station_2, R.drawable.station_3, R.drawable.station_4, R.drawable.station_5,R.drawable.station_6,R.drawable.station_7,R.drawable.station_8,R.drawable.station_9,R.drawable.station_10,R.drawable.station_11,R.drawable.station_12,R.drawable.station_13,R.drawable.station_14,R.drawable.station_15,R.drawable.station_16};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.app.ppt.yousoft.radiotn.R.layout.activity_details);


        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < titles.length; i++)
        {
            RowItem item = new RowItem(images[i], titles[i], descriptions[i]);
            rowItems.add(item);
        }

        listView = getListView();
        st= (EditText) findViewById(com.app.ppt.yousoft.radiotn.R.id.text_search);

        final ListAdapter1 adapter = new ListAdapter1(this, rowItems);
        listView.setAdapter(adapter);






        st.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                adapter.getFilter().filter(s);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
    protected void onListItemClick(ListView listView, View v, int position, long id) {
        super.onListItemClick(listView, v, position, id);
        String openAct = ractivities[position];

        Intent i = null;
        try {
             Class selected= Class.forName("com.app.ppt.yousoft.radiotn."+openAct);
            i = new Intent(this, selected);
            startActivity(i);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }




    }






}
