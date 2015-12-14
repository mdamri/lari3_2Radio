package com.app.ppt.yousoft.radiotn;
/// Troisième version
///checked by jassem ben

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=(Button)findViewById(com.app.ppt.yousoft.radiotn.R.id.acces);
        if(!networkcheked())
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Attention")
                    .setMessage("Pas de connexion Internet,Activez une Connexion puis reessayez")
                    .setPositiveButton("Fermer", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    }).show();
        }
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this,
                        StationList.class);
                startActivity(it);
                finish();
            }
        });

    }
    private boolean networkcheked()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

}

