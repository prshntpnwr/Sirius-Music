package com.example.prashant.siriusmusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player_screen extends AppCompatActivity implements View.OnClickListener {

    static MediaPlayer mp;
    ArrayList mysongs;

    Button btn1, btn2, btn4, btn6, btn5;
    SeekBar sb;
    int position;
    Uri u;
    Thread updatesb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_screen);

        btn1 = (Button)findViewById(R.id.button);
        btn2 = (Button)findViewById(R.id.button2);
        btn4 = (Button)findViewById(R.id.button4);
        btn5 = (Button)findViewById(R.id.button5);
        btn6 = (Button)findViewById(R.id.button6);

        sb = (SeekBar) findViewById(R.id.seekBar);
        updatesb = new Thread(){

            @Override
            public void run(){
                int totalDuration = mp.getDuration();
                int currentPosition = 0;

                while(currentPosition < totalDuration){
                    try{
                        sleep(500);
                        currentPosition = mp.getCurrentPosition();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);

        if(mp != null){
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mysongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos", 0);

        u = Uri.parse(mysongs.get(position).toString());
        // Log.v("THERE IS URI : ", String.valueOf(u));
        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.start();
        sb.setMax(mp.getDuration());
        updatesb.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });

    }

    @Override
    public void onClick(View view) {
        int id  = view.getId();

        switch (id){
            case R.id.button4:
                if(mp.isPlaying()){
                    btn4.setText(">");
                    mp.pause();
                }
                else{
                    btn4.setText("||");
                    mp.start();
                }
                break;
            case R.id.button5:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.button2:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.button6:
                mp.stop();
                mp.release();
                position = (position + 1) % mysongs.size();
                u = Uri.parse(mysongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;
            case R.id.button:
                mp.stop();
                mp.release();
                position = (position - 1 < 0) ? mysongs.size()-1:position-1;
                u = Uri.parse(mysongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
