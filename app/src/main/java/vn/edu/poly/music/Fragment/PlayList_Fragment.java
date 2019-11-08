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

import java.util.ArrayList;
import java.util.List;

import vn.edu.poly.music.Adapter.PlaylistAdapter;
import vn.edu.poly.music.Model.ThuMuc;
import vn.edu.poly.music.R;

public class PlayList_Fragment extends Fragment {
    private RecyclerView rvListPlaylist;
    private List<ThuMuc> thuMucList;
    private PlaylistAdapter playlistAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlist_fragment,container,false);
        rvListPlaylist = view.findViewById(R.id.rvListPlaylist);
        thuMucList = new ArrayList<>();
        for(int i = 0;i<10;i++){
            ThuMuc thuMuc = new ThuMuc();
            thuMuc.setTenThuMuc("Nhac tre");
            thuMucList.add(thuMuc);
        }
        playlistAdapter = new PlaylistAdapter(getActivity(),thuMucList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvListPlaylist.setLayoutManager(linearLayoutManager);
        rvListPlaylist.setAdapter(playlistAdapter);
        return view;
    }
}
