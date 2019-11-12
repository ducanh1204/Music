package vn.edu.poly.music.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import vn.edu.poly.music.Model.Playlist;
import vn.edu.poly.music.R;
import vn.edu.poly.music.SQLite.PlaylistDAO;

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

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PLaylistHolder> {
    private Context context;
    private List<Playlist> playlistList;
    private PlaylistDAO playlistDAO;

//    private static int i;

    public PlaylistAdapter(Context context, List<Playlist> playlistList) {
        this.context = context;
        this.playlistList = playlistList;
    }

    @NonNull
    @Override
    public PLaylistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist,parent,false);
        return new PLaylistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PLaylistHolder holder, final int position) {
        playlistDAO = new PlaylistDAO(context);
        holder.tvtenBaiHat.setText(playlistList.get(position).getTenBaiHat());
        holder.tvtenCaSi.setText(playlistList.get(position).getTenCaSi());
        holder.imgDelete_Song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có chắc chắn muốn xóa bài hát khỏi danh sách phát này không?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playlistDAO.delete_theotm(playlistList.get(position).getTenThuMuc());
                        playlistList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = position;
                list=2;
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
        Uri uri = Uri.parse(playlistList.get(position).getFileMp3());
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
        tvTenbaiHat.setText(playlistList.get(position).getTenBaiHat() + " - " + playlistList.get(position).getTenCaSi());
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
//            if (i > playlistList.size() - 1) {
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
//                i = playlistList.size() - 1;
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
                            if (i > playlistList.size() - 1) {
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
        return playlistList.size();
    }

    public class PLaylistHolder extends RecyclerView.ViewHolder {
        private TextView tvtenBaiHat,tvtenCaSi;
        private ImageView imgDelete_Song;
        public PLaylistHolder(@NonNull View itemView) {
            super(itemView);
            tvtenBaiHat = itemView.findViewById(R.id.tvtenBaiHat);
            tvtenCaSi = itemView.findViewById(R.id.tvtenCaSi);
            imgDelete_Song = itemView.findViewById(R.id.imgDelete_Song);
        }
    }
}
