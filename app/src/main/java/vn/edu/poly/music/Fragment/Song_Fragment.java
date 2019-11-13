package vn.edu.poly.music.Fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

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
    private Context context;
    private List<Song> songList2;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public Song_Fragment(Context context,List<Song> songList) {
        this.context = context;
        this.songList=songList;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_fragment,container,false);
        rvListSong = view.findViewById(R.id.rvListSong);


        songDAO = new SongDAO(getActivity());
        songList = songDAO.getAll();
        songAdapter = new SongAdapter(getActivity(),rvListSong,songList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvListSong.setLayoutManager(linearLayoutManager);
        rvListSong.setAdapter(songAdapter);

        return view;
    }


    // Create menu, and search song
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setQueryHint("Nhập tên bài hát");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                songAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
