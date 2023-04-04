package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    //widgets
    Button forward_btn,back_btn,play_btn,pause_btn;
    TextView time_txt,song_txt;
    SeekBar seekbar;

    //mediaPlayer
    MediaPlayer mediaplayer;

    //Handler
    Handler handler=new Handler();

    //variables
    double starttime=0,endtime=0;
    int forwardtime=5000,backwardtime=5000;
    static int onetimeonly=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forward_btn=findViewById(R.id.forwardbutton);
        back_btn=findViewById(R.id.backwardbutton);
        play_btn=findViewById(R.id.playbutton);
        pause_btn=findViewById(R.id.pausebutton);

        time_txt=findViewById(R.id.time_remaining);
        song_txt=findViewById(R.id.audiofile_name);


        seekbar=findViewById(R.id.seekBar);

        //assigning song to media player
        mediaplayer=MediaPlayer.create(this,R.raw.sukh_saathi);

        //setting song name
        song_txt.setText(getResources().getIdentifier("sukh_saathi","raw",getPackageName()));

        //setting seekbar
        seekbar.setClickable(false);

        //Adding  functionality to buttons
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });
        
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mediaplayer.pause();
            }
        });

        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp=(int)starttime;
                if(temp<endtime)
                {
                    starttime=starttime+forwardtime;
                    mediaplayer.seekTo((int) starttime);
                }
                else{
                    Toast.makeText(MainActivity.this, "Can't Jump", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp=(int)starttime;
                if((temp-backwardtime)>0)
                {
                    starttime=starttime-backwardtime;
                    mediaplayer.seekTo((int) starttime);
                }
                else{
                    Toast.makeText(MainActivity.this, "Can't go back!", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    //function
    private void playMusic() {
        mediaplayer.start();

        endtime=mediaplayer.getDuration();
        starttime=mediaplayer.getCurrentPosition();

        if(onetimeonly==0)
        {
            seekbar.setMax((int) endtime);
            onetimeonly=1;
        }

        time_txt.setText(String.format(
                "%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) endtime),
                TimeUnit.MILLISECONDS.toSeconds((long) endtime)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) endtime))
        ));

        seekbar.setProgress((int) starttime);
        handler.postDelayed(updateSongTime, 100);
    }

    //Runnable inteface
    private Runnable updateSongTime=new Runnable() {

        @Override
        public void run() {
            starttime=mediaplayer.getCurrentPosition();
            time_txt.setText(String.format(
                    "%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) starttime),
                    TimeUnit.MILLISECONDS.toSeconds((long) starttime)-
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) starttime))
            ));

            seekbar.setProgress((int)starttime);
            handler.postDelayed(this,100);
        }
    };


}