package com.example.fcmusicapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity {
    Button bPlay,bNext,bForward,bRewind,bPrevious,bLike;
    TextView txtsname, txtsstart, txtsstop;
    SeekBar seekmusic;
    ImageView imageView;
    String sname;

    public static MediaPlayer mediaPlayer;
    public static int position;
    public static ArrayList<File> mySongs;
    public static String songName;
    public Thread updateSeekbar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        getSupportActionBar().setTitle("Reproduciendo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bPlay = findViewById(R.id.playButton);
        bNext = findViewById(R.id.nextButton);
        bForward = findViewById(R.id.forwardButton);
        bRewind = findViewById(R.id.rewindButton);
        bPrevious = findViewById(R.id.previousButton);
        bLike = findViewById(R.id.heartButton);
        txtsname = findViewById(R.id.txtsn);
        txtsstart = findViewById(R.id.txtStart);
        txtsstop = findViewById(R.id.txtStop);
        seekmusic = findViewById(R.id.seekbar);
        imageView = findViewById(R.id.imageview);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        boolean actionButton = bundle.getBoolean("actionButton");
        if (!actionButton) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
            songName = i.getStringExtra("songname");
            position = bundle.getInt("pos", 0);
            txtsname.setSelected(true);
            Uri uri = Uri.parse(mySongs.get(position).toString());

            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            String endTime=createTime(mediaPlayer.getDuration());
            txtsstop.setText(endTime);
            mediaPlayer.start();
        }
        sname = mySongs.get(position).getName().replace(".mp3","");
        txtsname.setText(sname);
        DataBaseHelperCancion db = new DataBaseHelperCancion(MusicPlayer.this);
        boolean like = db.getLike(songName);
        if(like)
            bLike.setBackgroundResource(R.drawable.full_heart);
        else
            bLike.setBackgroundResource(R.drawable.heart);
        db.close();

        updateSeekbar=new Thread(){
            @Override
            public void run(){
                int totalDuration=mediaPlayer.getDuration();
                int currentPos=0;
                while (currentPos<totalDuration){
                    try{
                        sleep(500);
                        currentPos=mediaPlayer.getCurrentPosition();
                        seekmusic.setProgress(currentPos);
                    }catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        seekmusic.setMax(mediaPlayer.getDuration());
        updateSeekbar.start();
        seekmusic.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        seekmusic.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);

        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        String endTime=createTime(mediaPlayer.getDuration());
        txtsstop.setText(endTime);
        final Handler handler=new Handler();
        final int delay=1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime=createTime(mediaPlayer.getCurrentPosition());
                txtsstart.setText(currentTime);
                handler.postDelayed(this,delay);
            }
        },delay);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    bPlay.setBackgroundResource(R.drawable.play);
                    mediaPlayer.pause();
                } else {
                    bPlay.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position=((position+1)%mySongs.size());
                Uri u=Uri.parse(mySongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mySongs.get(position).getName().replace(".mp3","");;
                txtsname.setText(sname);
                String endTime=createTime(mediaPlayer.getDuration());
                txtsstop.setText(endTime);
                seekmusic.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                bPlay.setBackgroundResource(R.drawable.pause);
                DataBaseHelperCancion db = new DataBaseHelperCancion(MusicPlayer.this);
                boolean like = db.getLike(sname);
                if(like)
                    bLike.setBackgroundResource(R.drawable.full_heart);
                else
                    bLike.setBackgroundResource(R.drawable.heart);
                db.close();
                startAnimationNext(imageView);
            }
        });

        bPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position=((position-1)<0)?(mySongs.size()-1):(position-1);
                Uri u=Uri.parse(mySongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mySongs.get(position).getName().replace(".mp3","");
                txtsname.setText(sname);
                mediaPlayer.start();
                String endTime=createTime(mediaPlayer.getDuration());
                seekmusic.setMax(mediaPlayer.getDuration());
                txtsstop.setText(endTime);
                bPlay.setBackgroundResource(R.drawable.pause);
                DataBaseHelperCancion db = new DataBaseHelperCancion(MusicPlayer.this);
                boolean like = db.getLike(sname);
                if(like)
                    bLike.setBackgroundResource(R.drawable.full_heart);
                else
                    bLike.setBackgroundResource(R.drawable.heart);
                db.close();
                startAnimationBack(imageView);
            }
        });

        bForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
                }
            }
        });

        bRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                }
            }
        });

        bLike .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelperCancion db = new DataBaseHelperCancion(MusicPlayer.this);
                boolean like = db.getLike(sname);
                if(like)
                    bLike.setBackgroundResource(R.drawable.heart);
                else
                    bLike.setBackgroundResource(R.drawable.full_heart);
                db.likeSong(sname);
                db.close();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                bNext.performClick();
            }
        });
    }
    public void startAnimationNext(View view){
        ObjectAnimator animator=ObjectAnimator.ofFloat(imageView,"rotation",0f,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }
    public void startAnimationBack(View view){
        ObjectAnimator animator=ObjectAnimator.ofFloat(imageView,"rotation",360f,0f);
        animator.setDuration(1000);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public String createTime(int duration){
        String time="";
        int min =duration/60000;
        int seconds=duration/1000%60;

        time+=min+":";
        return (seconds<10)?time+'0'+seconds:time+seconds;
    }
}