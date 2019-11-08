package vn.edu.poly.music.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.poly.music.Model.Song;
import vn.edu.poly.music.R;

public class Singer_SongAdapter extends RecyclerView.Adapter<Singer_SongAdapter.Singer_SongHolder> {
    private Context context;
    private List<Song> songList;

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
    public void onBindViewHolder(@NonNull Singer_SongHolder holder, int position) {
        holder.tvtenBaiHat.setText(songList.get(position).getTenBaiHat());
        holder.tvtenCaSi.setText(songList.get(position).getTenCaSi());
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
