package vn.edu.poly.music.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.poly.music.Model.Playlist;
import vn.edu.poly.music.R;
import vn.edu.poly.music.SQLite.PlaylistDAO;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PLaylistHolder> {
    private Context context;
    private List<Playlist> playlistList;
    private PlaylistDAO playlistDAO;

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
                        playlistDAO.delete(playlistList.get(position).getTenBaiHat());
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
