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

import vn.edu.poly.music.Adapter.SongAdapter;
import vn.edu.poly.music.Model.Song;
import vn.edu.poly.music.R;
import vn.edu.poly.music.SQLite.SongDAO;

public class Song_Fragment extends Fragment {
    private RecyclerView rvListSong;
    private SongAdapter songAdapter;
    private List<Song> songList;
    private SongDAO songDAO;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_fragment,container,false);
        rvListSong = view.findViewById(R.id.rvListSong);
        songDAO = new SongDAO(getActivity());
        songList = songDAO.getAll();
        songAdapter = new SongAdapter(getActivity(),songList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvListSong.setLayoutManager(linearLayoutManager);
        rvListSong.setAdapter(songAdapter);
        return view;
    }
}