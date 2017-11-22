package com.emotilog.app.emotilog;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.data;
import static android.R.attr.id;
import static android.R.attr.name;
import static android.os.Build.ID;
import static com.emotilog.app.emotilog.R.id.bar;
import static com.emotilog.app.emotilog.R.id.date;
import static com.emotilog.app.emotilog.R.id.faccina;
import static com.emotilog.app.emotilog.R.id.picoftheday_orizontal;
import static com.emotilog.app.emotilog.R.id.picoftheday_vertical;
import static com.emotilog.app.emotilog.R.id.score;

import android.support.v7.app.ActionBar;

public class DiaryEntryActivity extends AppCompatActivity {
    private ImageView emjoy;
    private ImageView photo_orizontal;
    private ImageView photo_vertical;
    private ImageView score_bar;
    private ImageView shaking_score_text;
    private TextView diaryText;
    public TextView display_date;
    public TextView shake_score;
    private Button showinmaps;
    private TextView locText;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);
        dbHelper = new MyDatabaseHelper(this, MyDatabaseHelper.DATABASE_NAME, null, 1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3498db")));
        this.getWindow().setStatusBarColor(Color.parseColor("#2979af"));

        int id = getIntent().getIntExtra("id",0);
        final Entry e = dbHelper.getEntry(id);

        //tutti gli associamenti con xml
        diaryText = (TextView) findViewById(R.id.textView);
        display_date = (TextView) findViewById(R.id.date);
        showinmaps = (Button) findViewById(R.id.showLocation);
        emjoy=(ImageView)findViewById(faccina);
        photo_orizontal=(ImageView)findViewById(picoftheday_orizontal);
        photo_vertical=(ImageView)findViewById(picoftheday_vertical);
        shake_score= (TextView)findViewById(score);
        score_bar= (ImageView) findViewById(bar);


        showinmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e.getLocation()!=null) {
                    Uri gmmIntentUri = Uri.parse(e.LOCATION.toString());
                    Intent ShowInMapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    ShowInMapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(ShowInMapIntent);
                }
                else Toast.makeText(DiaryEntryActivity.this, "No location in this entry",
                        Toast.LENGTH_LONG).show();
            }
        });

        if(e.PHOTO!=null) {
            Bitmap  photo = null ;
            photo=BitmapFactory.decodeByteArray(e.PHOTO, 0, e.PHOTO.length);
            if(photo.getHeight()>=photo.getWidth()){
                photo_vertical.setImageBitmap(photo);
                photo_orizontal.setVisibility(View.INVISIBLE);
            }
            else{
                photo_orizontal.setImageBitmap(photo);
                photo_vertical.setVisibility(View.INVISIBLE);
            }

        }
        else {
            photo_vertical.setVisibility(View.INVISIBLE);
            photo_orizontal.setVisibility(View.INVISIBLE);
        }


        if (e.FEALING == 1)
            emjoy.setImageResource(R.drawable.laugh_color);
        else if (e.FEALING == 2)
            emjoy.setImageResource(R.drawable.smile_color);
        else if(e.FEALING==3)
            emjoy.setImageResource(R.drawable.sad_color);
        else if(e.FEALING==4)
            emjoy.setImageResource(R.drawable.cry_color);
        else if(e.FEALING==5)
            emjoy.setImageResource(R.drawable.angry_color);

        display_date.setText(e.DATE_TIME);

        diaryText.setText(e.TEXT);
        //e.SHAKESCORE = 80;
        if(e.getShakescore()<40){
            shake_score.setText("No Shaking Score");
            score_bar.setVisibility(View.INVISIBLE);
        }
        else {
            shake_score.setText("" + e.getShakescore());
            if (e.getShakescore() >= 90) score_bar.setImageResource(R.drawable.top_score);
            else if (e.getShakescore() >= 80) score_bar.setImageResource(R.drawable.score_80);
            else if (e.getShakescore() >= 70) score_bar.setImageResource(R.drawable.score_70);
            else if (e.getShakescore() >= 60) score_bar.setImageResource(R.drawable.score_60);
            else if (e.getShakescore() >= 50) score_bar.setImageResource(R.drawable.score_50);
            else if (e.getShakescore() >= 40) score_bar.setImageResource(R.drawable.score_40);
        }
    }
}
