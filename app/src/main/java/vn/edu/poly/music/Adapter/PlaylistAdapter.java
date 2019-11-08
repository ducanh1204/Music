package vn.edu.poly.music.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.poly.music.Model.ThuMuc;
import vn.edu.poly.music.R;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder> {
    private Context context;
    private List<ThuMuc> thuMucList;

    public PlaylistAdapter(Context context, List<ThuMuc> thuMucList) {
        this.context = context;
        this.thuMucList = thuMucList;
    }

    @NonNull
    @Override
    public PlaylistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist,parent,false);
        return new PlaylistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistHolder holder, int position) {

        holder.tvtenThuMuc.setText(thuMucList.get(position).getTenThuMuc());
    }

    @Override
    public int getItemCount() {
        return thuMucList.size();
    }

    public class PlaylistHolder extends RecyclerView.ViewHolder {
        private TextView tvtenThuMuc;
        private ImageView imgDelete_ThuMuc;
        public PlaylistHolder(@NonNull View itemView) {
            super(itemView);
            tvtenThuMuc = itemView.findViewById(R.id.tvtenThuMuc);
            imgDelete_ThuMuc = itemView.findViewById(R.id.imgDelete_ThuMuc);
        }
    }
}
