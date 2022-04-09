//<!--Archie Yarr S1821533-->
package com.example.yarr_archie_s1821533;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;





public class ItemDescActivity extends AppCompatActivity {

    TextView tv_title;

    TextView tv_description;

    TextView tv_duration;

    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //display item description/details layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desc_item);

        //call intent class https://developer.android.com/reference/android/content/Intent
        Intent intent = getIntent();
        tv_title = findViewById(R.id.itemTitle);
        tv_description = findViewById(R.id.itemD1);



        tv_duration = findViewById(R.id.itemDuration);





        String title = intent.getExtras().getString("title");

        String dur = intent.getExtras().getString("dur");
        String description = intent.getExtras().getString("desc0");
        Boolean incident = intent.getExtras().getBoolean("inc");

        Log.e("MyTag","title "+title);
        Log.e("MyTag","desc "+description);
        Log.e("MyTag","desc "+description);



        tv_title.setText(title);
        tv_description.setText(description);
        tv_duration.setText(dur);

    }


}

