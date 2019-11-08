package vn.edu.poly.music.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.poly.music.Model.Singer;
import vn.edu.poly.music.R;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.SingerHolder> {
    private Context context;
    private List<Singer> singerList;

    public SingerAdapter(Context context, List<Singer> singerList) {
        this.context = context;
        this.singerList = singerList;
    }

    @NonNull
    @Override
    public SingerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_singer,parent,false);
        return new SingerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingerHolder holder, int position) {
        holder.tvtenCaSi.setText(singerList.get(position).getTenCaSi());
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
