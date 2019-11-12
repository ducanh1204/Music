package vn.edu.poly.music.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.poly.music.Activity.MainActivity;
import vn.edu.poly.music.Model.Singer;
import vn.edu.poly.music.Model.Song;
import vn.edu.poly.music.R;
import vn.edu.poly.music.SQLite.SongDAO;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.SingerHolder> {
    private Context context;
    private List<Singer> singerList;
    private List<Song> songList;
    private Singer_SongAdapter singer_songAdapter;
    private SongDAO songDAO;
    private RecyclerView rvListSinger_Song;

    public SingerAdapter(Context context, List<Singer> singerList,List<Song> songList) {
        this.context = context;
        this.singerList = singerList;
        this.songList = songList;
    }

    @NonNull
    @Override
    public SingerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_singer, parent, false);
        return new SingerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingerHolder holder, final int position) {
        songDAO = new SongDAO(context);
        holder.tvtenCaSi.setText(singerList.get(position).getTenCaSi());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imgClose;
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_singer_song_acticity);
                dialog.show();
                dialog.setTitle("Bài hát của ca sĩ " + singerList.get(position).getTenCaSi());
                rvListSinger_Song = dialog.findViewById(R.id.rvListSinger_Song);
                songList = songDAO.getAll_Singer_Song(singerList.get(position).getTenCaSi());
                MainActivity.tenCasi = singerList.get(position).getTenCaSi();
                singer_songAdapter = new Singer_SongAdapter(context, songList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                rvListSinger_Song.setLayoutManager(linearLayoutManager);
                rvListSinger_Song.setAdapter(singer_songAdapter);
                imgClose = dialog.findViewById(R.id.imgClose);
                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return singerList.size();
    }

    public class SingerHolder extends RecyclerView.ViewHolder {
        private TextView tvtenCaSi;

        public SingerHolder(@NonNull View itemView) {
            super(itemView);
            tvtenCaSi = itemView.findViewById(R.id.tvtenCaSi);
        }
    }
}
