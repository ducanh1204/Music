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

import vn.edu.poly.music.Adapter.SingerAdapter;
import vn.edu.poly.music.Model.Singer;
import vn.edu.poly.music.R;
import vn.edu.poly.music.SQLite.SongDAO;

public class Singer_Fragment extends Fragment {
    private RecyclerView rvListSinger;
    private SongDAO songDAO;
    private List<Singer> singerList;
    private SingerAdapter singerAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.singer_fragment,container,false);
        rvListSinger = view.findViewById(R.id.rvListSinger);

        songDAO = new SongDAO(getActivity());
        singerList = songDAO.getAllSinger();
        singerAdapter = new SingerAdapter(getActivity(),singerList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvListSinger.setLayoutManager(linearLayoutManager);
        rvListSinger.setAdapter(singerAdapter);
        return view;
    }
}
