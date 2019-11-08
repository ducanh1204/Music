package vn.edu.poly.music.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.poly.music.Activity.SingerSongActicity;
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
    public void onBindViewHolder(@NonNull SingerHolder holder, final int position) {
        holder.tvtenCaSi.setText(singerList.get(position).getTenCaSi());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingerSongActicity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tenCaSi",singerList.get(position).getTenCaSi());
                intent.putExtra("truyenIntent2",bundle);
                context.startActivity(intent);
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
