package vn.edu.poly.music.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.poly.music.Adapter.ThuMucAdapter;
import vn.edu.poly.music.Model.ThuMuc;
import vn.edu.poly.music.R;
import vn.edu.poly.music.SQLite.ThuMucDAO;

public class ThuMuc_Fragment extends Fragment {
    private RecyclerView rvListThuMuc;
    private List<ThuMuc> thuMucList;
    private ThuMucAdapter thuMucAdapter;
    private ThuMucDAO thuMucDAO;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thumuc_fragment,container,false);
        rvListThuMuc = view.findViewById(R.id.rvListThuMuc);
        thuMucDAO = new ThuMucDAO(getActivity());
        thuMucList = thuMucDAO.getAll();
        thuMucAdapter = new ThuMucAdapter(getActivity(),thuMucList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvListThuMuc.setLayoutManager(linearLayoutManager);
        rvListThuMuc.setAdapter(thuMucAdapter);
        return view;
    }
}
