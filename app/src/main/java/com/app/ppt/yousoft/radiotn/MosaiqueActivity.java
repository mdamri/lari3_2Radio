package com.app.ppt.yousoft.radiotn;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MosaiqueActivity extends Activity {




    private ImageView call;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaique);




    call =(ImageView) findViewById(R.id.callButton);





        call.setOnClickListener(new View.OnClickListener() {

           public void onClick(View v) {
               String phoneno = "71 113 001";

                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneno));
               startActivity(i);

          }
       });



    }
    public void onBackPressed(){

        super.onBackPressed();

    }





}
