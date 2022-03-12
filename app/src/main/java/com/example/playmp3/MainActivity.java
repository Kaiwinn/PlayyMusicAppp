package com.example.playmp3;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chootdev.blurimg.BlurImage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView img_Background,img_Play, img_CD;
    MediaPlayer player;
    private boolean playingMusic;
    private boolean hadSong;
    private int timePause = 0;
    SeekBar seekbarSong;
    int timeMax;
    TextView txt_timeStartSong, txt_timeSongAll, txv_loibaihat;
    ArrayList<String> arrLyrics = new ArrayList<>();
    int lyricsposition = 0;
    float rotation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mapping();
        setUp();
        setClick();
    }

    public void init(){
        arrLyrics.add("Lyrics Number 1");
        arrLyrics.add("Lyrics Number 2");
        arrLyrics.add("Lyrics Number 3");
        arrLyrics.add("Lyrics Number 4");
        arrLyrics.add("Lyrics Number 5");

    }

    private void mapping() {
        img_Background= findViewById(R.id.img_Background);
        img_Play= findViewById(R.id.img_Play);
        seekbarSong = findViewById(R.id.seekbarSong);
        txt_timeStartSong = findViewById(R.id.txt_timeStartSong);
        txt_timeSongAll = findViewById(R.id.txt_timeSongAll);
        txv_loibaihat = findViewById(R.id.txv_loibaihat);
        img_CD= findViewById(R.id.img_CD);
    }

    private void setUp() {
        setBackground(R.drawable.nhac1);
        playingMusic = false;
        hadSong = false;
        new CountDownTimer(30000, 50){

            @Override
            public void onTick(long millisUntilFinished) {
                upDate();
            }

            @Override
            public void onFinish() {
                start();
            }
        }.start();
        new CountDownTimer(2000, 100){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                upDateLyrics();
                start();
            }
        }.start();

    }

    private void setClick() {
        img_Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hadSong==false){
                    startsong(R.raw.chanh_long_thuong_co);
                }else{
                    if(playingMusic == true){
                        pausesong();
                    }else{
                        playsong();
                    }
                }
            }
        });
        seekbarSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(hadSong == false || playingMusic== false){
                    return;
                }
                timePause = seekbarSong.getProgress();
                player.pause();
                player.seekTo(timePause);
                player.start();
            }
        });
    }

    private void setBackground(int img){
        BlurImage.withContext(this)
                .setBlurRadius(15.5f)
                .setBitmapScale(0.1f)
                .blurFromResource(img)
                .into(img_Background);
    }

    private void startsong(int song){
        if(player== null){
            player= MediaPlayer.create(this, song);

        }else{
            player.stop();
            player.release();
            player= MediaPlayer.create(this, song);

        }
        player.start();
        img_Play.setImageResource(R.drawable.ic_pause);
        hadSong = true;
        playingMusic = true;
        seekbarSong.setMin(0);
        timeMax = player.getDuration(); //get time Max of Song
        seekbarSong.setMax(timeMax);
        txt_timeSongAll.setText(miniToString(timeMax));
        lyricsposition = -1;
        rotation =0;
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                runEnd();
            }
        });
    }

    private void runEnd(){
        playingMusic = false;
        hadSong = false;
        timePause = 0;
        rotation =0;
        img_Play.setImageResource(R.drawable.ic_play);
    }


    public void pausesong(){
        playingMusic= false;
        img_Play.setImageResource(R.drawable.ic_play);
        player.pause();
        timePause= player.getCurrentPosition();
    }

    public void playsong(){
        playingMusic= true;
        img_Play.setImageResource(R.drawable.ic_pause);
        player.seekTo(timePause);
        player.start();
    }

    private void upDate(){
        if(hadSong == false || playingMusic== false){
            return;
        }
        seekbarSong.setProgress(player.getCurrentPosition());
        txt_timeStartSong.setText(miniToString(player.getCurrentPosition()));
        rotation = rotation + 0.5f;
        if(rotation == 360){
            rotation =0;
        }
        img_CD.setRotation(rotation);
    }

    private String miniToString(int t){
        t = t/1000;
        int p = t/ 60;
        int s = t%60;
        return checkNum10(p) + ":" + checkNum10(s);
    }

    private String checkNum10(int i){
        if(i<10){
            return "0" + i;
        }
        return "" + i;
    }
    private void upDateLyrics(){
        if(hadSong == false || playingMusic== false){
            return;
        }
            lyricsposition++;
            if(arrLyrics.size()==lyricsposition){
            lyricsposition =0;
            }
            txv_loibaihat.startAnimation(AnimationUtils.loadAnimation(this, R.anim.disappear));
            new CountDownTimer(400, 100){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                txv_loibaihat.setText(arrLyrics.get(lyricsposition));
                txv_loibaihat.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.appear));
            }
        }.start();
    }

}