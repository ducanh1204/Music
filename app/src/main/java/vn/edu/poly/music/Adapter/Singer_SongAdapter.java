package vn.edu.poly.music.Adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import vn.edu.poly.music.Model.Song;
import vn.edu.poly.music.R;

import static vn.edu.poly.music.Activity.MainActivity.animation;
import static vn.edu.poly.music.Activity.MainActivity.i;
import static vn.edu.poly.music.Activity.MainActivity.imgCD;
import static vn.edu.poly.music.Activity.MainActivity.imgNext;
import static vn.edu.poly.music.Activity.MainActivity.imgPlay;
import static vn.edu.poly.music.Activity.MainActivity.imgPrev;
import static vn.edu.poly.music.Activity.MainActivity.list;
import static vn.edu.poly.music.Activity.MainActivity.load;
import static vn.edu.poly.music.Activity.MainActivity.mediaPlayer;
import static vn.edu.poly.music.Activity.MainActivity.seekBar;
import static vn.edu.poly.music.Activity.MainActivity.tvTenbaiHat;
import static vn.edu.poly.music.Activity.MainActivity.tvThoiGian2;
import static vn.edu.poly.music.Activity.MainActivity.tvThoigian1;

public class Singer_SongAdapter extends RecyclerView.Adapter<Singer_SongAdapter.Singer_SongHolder> {
    private Context context;
    private List<Song> songList;
//    private static int i;

    public Singer_SongAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public Singer_SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_singer_song,parent,false);
        return new Singer_SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Singer_SongHolder holder, final int position) {
        holder.tvtenBaiHat.setText(songList.get(position).getTenBaiHat());
        holder.tvtenCaSi.setText(songList.get(position).getTenCaSi());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = position;
                list=3;
                if (load == 0) {
                    startSong(i);
                } else {
                    mediaPlayer.stop();
                    startSong(i);
                }
            }
        });


//        imgPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkPlay();
//
//            }
//        });
//        imgNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nextSong();
//            }
//        });
//        imgPrev.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                prevSong();
//            }
//        });
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                if (tvTenbaiHat.getText().toString().equals("Chưa chọn bài hát")) {
//                    Toast.makeText(context, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
//                } else {
//                    mediaPlayer.seekTo(seekBar.getProgress());
//                }
//            }
//        });
    }

//    private void checkPlay() {
//        if (tvTenbaiHat.getText().toString().equals("Chưa chọn bài hát")) {
//            Toast.makeText(context, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
//        } else {
//            if (mediaPlayer.isPlaying() == true) {
//                mediaPlayer.pause();
//                imgPlay.setImageResource(R.drawable.play);
//                load = 0;
//                imgCD.clearAnimation();
//            } else {
//                imgCD.startAnimation(animation);
//                mediaPlayer.start();
//                imgPlay.setImageResource(R.drawable.pause);
//                load = 1;
//            }
//
//            setTimeTotal();
//            updateTimeSong();
//        }
//    }

    private void startSong(int position) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Uri uri = Uri.parse(songList.get(position).getFileMp3());
        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            imgCD.startAnimation(animation);
            load = 1;
            if (mediaPlayer.isPlaying() == true) {
                imgPlay.setImageResource(R.drawable.pause);
            } else {
                imgPlay.setImageResource(R.drawable.play);
            }
        } catch (IOException e) {
            e.printStackTrace();
            imgCD.clearAnimation();
            Toast.makeText(context, "Loi phat nhac", Toast.LENGTH_SHORT).show();
        }
        tvTenbaiHat.setText(songList.get(position).getTenBaiHat() + " - " + songList.get(position).getTenCaSi());
        setTimeTotal();
        updateTimeSong();
    }

//    private void nextSong() {
//        if (tvTenbaiHat.getText().toString().equals("Chưa chọn bài hát")) {
//            Toast.makeText(context, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
//        } else {
//            i++;
//            if (load == 1) {
//                mediaPlayer.stop();
//            }
//            if (i > songList.size() - 1) {
//                i = 0;
//            }
//            startSong(i);
//            imgCD.startAnimation(animation);
//            setTimeTotal();
//            updateTimeSong();
//        }
//    }
//
//    private void prevSong() {
//        if (tvTenbaiHat.getText().toString().equals("Chưa chọn bài hát")) {
//            Toast.makeText(context, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
//        } else {
//            i--;
//            if (load == 1) {
//                mediaPlayer.stop();
//            }
//            if (i < 0) {
//                i = songList.size() - 1;
//            }
//            startSong(i);
//            imgCD.startAnimation(animation);
//            setTimeTotal();
//            updateTimeSong();
//        }
//    }

    private void setTimeTotal() {
        SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
        tvThoiGian2.setText(dinhDangGio.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void updateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
                tvThoigian1.setText(dinhDangGio.format(mediaPlayer.getCurrentPosition()));
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (tvTenbaiHat.getText().toString().equals("Chưa chọn bài hát")) {
                            Toast.makeText(context, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
                        } else {
                            i++;
                            if (load == 1) {
                                mediaPlayer.stop();
                            }
                            if (i > songList.size() - 1) {
                                i = 0;
                            }
                            startSong(i);
                            imgCD.startAnimation(animation);
                            setTimeTotal();
                            updateTimeSong();
                        }
                    }
                });

                handler.postDelayed(this, 500);
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 100);
    }
    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class Singer_SongHolder extends RecyclerView.ViewHolder {
        private TextView tvtenBaiHat,tvtenCaSi;
        public Singer_SongHolder(@NonNull View itemView) {
            super(itemView);
            tvtenBaiHat = itemView.findViewById(R.id.tvtenBaiHat);
            tvtenCaSi = itemView.findViewById(R.id.tvtenCaSi);
        }
    }
}
