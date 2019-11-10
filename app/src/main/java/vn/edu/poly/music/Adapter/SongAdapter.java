package vn.edu.poly.music.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import vn.edu.poly.music.Activity.MainActivity;
import vn.edu.poly.music.Model.Song;
import vn.edu.poly.music.R;
import vn.edu.poly.music.SQLite.SongDAO;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
    private Context context;
    private List<Song> songList;

    public SongAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    private SongDAO songDAO;



    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, final int position) {
        songDAO = new SongDAO(context);
        holder.tvtenBaiHat.setText(songList.get(position).getTenBaiHat());
        holder.tvtenCaSi.setText(songList.get(position).getTenCaSi());
        holder.imgDelete_Song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có chắc chắn muốn xóa bài hát này không?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        songDAO.delete(songList.get(position).getTenBaiHat());
                        songList.remove(position);
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
        holder.imgAdd_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mediaPlayer.isPlaying()) {
                    MainActivity.mediaPlayer.stop();
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(position);
            }
        });
    }

    private void start(int position) {
        MainActivity.mediaPlayer = new MediaPlayer();
        MainActivity.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Uri uri = Uri.parse(songList.get(position).getFileMp3());
        try {
            MainActivity.mediaPlayer.setDataSource(context, uri);
            MainActivity.mediaPlayer.prepare();
            MainActivity.mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        private TextView tvtenBaiHat, tvtenCaSi;
        private ImageView imgAdd_playlist, imgDelete_Song;

        public SongHolder(@NonNull View itemView) {
            super(itemView);
            tvtenBaiHat = itemView.findViewById(R.id.tvtenBaiHat);
            tvtenCaSi = itemView.findViewById(R.id.tvtenCaSi);
            imgAdd_playlist = itemView.findViewById(R.id.imgAdd_playlist);
            imgDelete_Song = itemView.findViewById(R.id.imgDelete_Song);
        }
    }
}
