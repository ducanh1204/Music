package vn.edu.poly.music.Adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import vn.edu.poly.music.Model.Song;
import vn.edu.poly.music.R;
import vn.edu.poly.music.SQLite.SongDAO;

import static vn.edu.poly.music.Activity.MainActivity.animation;
import static vn.edu.poly.music.Activity.MainActivity.checkRandom;
import static vn.edu.poly.music.Activity.MainActivity.checkRepeat;
import static vn.edu.poly.music.Activity.MainActivity.i;
import static vn.edu.poly.music.Activity.MainActivity.imgCD;
import static vn.edu.poly.music.Activity.MainActivity.imgPlay;
import static vn.edu.poly.music.Activity.MainActivity.imgRandom;
import static vn.edu.poly.music.Activity.MainActivity.imgRepeat;
import static vn.edu.poly.music.Activity.MainActivity.list;
import static vn.edu.poly.music.Activity.MainActivity.load;
import static vn.edu.poly.music.Activity.MainActivity.mediaPlayer;
import static vn.edu.poly.music.Activity.MainActivity.seekBar;
import static vn.edu.poly.music.Activity.MainActivity.tvTenbaiHat;
import static vn.edu.poly.music.Activity.MainActivity.tvThoiGian2;
import static vn.edu.poly.music.Activity.MainActivity.tvThoigian1;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder> {


    private Context context;
    private List<Song> songList;

    public FavouriteAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    private SongDAO songDAO;


    @NonNull
    @Override
    public FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fvr, parent, false);
        return new FavouriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteHolder holder, final int position) {
        songDAO = new SongDAO(context);
        holder.tvtenCaSi.setText(songList.get(position).getTenCaSi());
        holder.tvtenBaiHat.setText(songList.get(position).getTenBaiHat());
        holder.imgYeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Song song = new Song();
                song.setTenBaiHat(songList.get(position).getTenBaiHat());
                song.setTenCaSi(songList.get(position).getTenCaSi());
                song.setFileMp3(songList.get(position).getFileMp3());
                song.setYeuThich(0);
                long result = songDAO.update(song);
                if (result > 0) {
                    songList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = position;

                list = 4;
                checkRepeat = 0;
                checkRandom = 0;
                imgRandom.setImageResource(R.drawable.random_off);
                imgRepeat.setImageResource(R.drawable.repeate);
                if (load == 0) {
                    startSong(i);
                } else {
                    mediaPlayer.stop();
                    startSong(i);
                }
            }
        });

    }

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
                            if (load == 1) {
                                mediaPlayer.stop();
                            }
                            if (checkRandom == 0 && checkRepeat == 0) {
                                i++;
                                if (i > songList.size() - 1) {
                                    i = 0;
                                }
                            } else if (checkRandom == 1) {
                                i = startRandom(0, songList.size() - 1);
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


    private int startRandom(int min, int max) {
        int j = (int) (Math.random() * (max - min + 1) + min);
        return j;
    }


    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class FavouriteHolder extends RecyclerView.ViewHolder {
        private TextView tvtenBaiHat, tvtenCaSi;
        private ImageView imgYeuthich;

        public FavouriteHolder(@NonNull View itemView) {
            super(itemView);
            tvtenBaiHat = itemView.findViewById(R.id.tvtenBaiHat);
            tvtenCaSi = itemView.findViewById(R.id.tvtenCaSi);
            imgYeuthich = itemView.findViewById(R.id.imgYeuthich);
        }
    }
}
