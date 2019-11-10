package vn.edu.poly.music.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import vn.edu.poly.music.Model.ThuMuc;
import vn.edu.poly.music.R;

public class LV_ThuMucAdapter extends BaseAdapter {
    private Context context;
    private List<ThuMuc> thuMucList;

    public LV_ThuMucAdapter(Context context, List<ThuMuc> thuMucList) {
        this.context = context;
        this.thuMucList = thuMucList;
    }

    @Override
    public int getCount() {
        return thuMucList.size();
    }

    @Override
    public ThuMuc getItem(int position) {
        return thuMucList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView tvThuMuc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
         ThuMuc thuMuc = (ThuMuc) getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_thumuc, parent, false);
            viewHolder.tvThuMuc = convertView.findViewById(R.id.tvThuMuc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvThuMuc.setText(thuMuc.getTenThuMuc());
        return convertView;
    }
}
