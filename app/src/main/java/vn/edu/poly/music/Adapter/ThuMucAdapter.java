package vn.edu.poly.music.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.poly.music.Model.Playlist;
import vn.edu.poly.music.Model.ThuMuc;
import vn.edu.poly.music.R;
import vn.edu.poly.music.SQLite.PlaylistDAO;
import vn.edu.poly.music.SQLite.ThuMucDAO;

public class ThuMucAdapter extends RecyclerView.Adapter<ThuMucAdapter.ThuMUctHolder> {
    private Context context;
    private List<ThuMuc> thuMucList;
    private ThuMucDAO thuMucDAO;
    private PlaylistDAO playlistDAO;
    private List<Playlist> playlistList;
    private PlaylistAdapter playlistAdapter;
    private RecyclerView rvListPlaylist;


    public ThuMucAdapter(Context context, List<ThuMuc> thuMucList) {
        this.context = context;
        this.thuMucList = thuMucList;
    }

    @NonNull
    @Override
    public ThuMUctHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thumuc, parent, false);
        return new ThuMUctHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThuMUctHolder holder, final int position) {


        playlistDAO = new PlaylistDAO(context);
        holder.tvtenThuMuc.setText(thuMucList.get(position).getTenThuMuc());
        holder.imgDelete_ThuMuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có chắc chắn muốn xóa thư mục này không?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        thuMucDAO.delete(thuMucList.get(position).getTenThuMuc());
                        thuMucList.remove(position);
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
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_playlist);
                dialog.show();
                rvListPlaylist = dialog.findViewById(R.id.rvListPlaylist);
                dialog.setTitle("Danh sách phát " + thuMucList.get(position).getTenThuMuc());
                playlistList = playlistDAO.getAll(thuMucList.get(position).getTenThuMuc());
                playlistAdapter = new PlaylistAdapter(context, playlistList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                rvListPlaylist.setLayoutManager(linearLayoutManager);
                rvListPlaylist.setAdapter(playlistAdapter);

            }
        });
    }

    @Override
    public int getItemCount() {
        return thuMucList.size();
    }

    public class ThuMUctHolder extends RecyclerView.ViewHolder {
        private TextView tvtenThuMuc;
        private ImageView imgDelete_ThuMuc;

        public ThuMUctHolder(@NonNull View itemView) {
            super(itemView);
            tvtenThuMuc = itemView.findViewById(R.id.tvtenThuMuc);
            imgDelete_ThuMuc = itemView.findViewById(R.id.imgDelete_ThuMuc);
        }
    }
}
