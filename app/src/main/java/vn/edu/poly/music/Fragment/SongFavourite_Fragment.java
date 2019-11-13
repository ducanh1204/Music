package vn.edu.poly.music.Fragment;

import android.content.Context;
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

import vn.edu.poly.music.Adapter.FavouriteAdapter;
import vn.edu.poly.music.Model.Song;
import vn.edu.poly.music.R;
import vn.edu.poly.music.SQLite.SongDAO;

public class SongFavourite_Fragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private Context context;
    private RecyclerView rvListFvr;
    private FavouriteAdapter favouriteAdapter;
    private SongDAO songDAO;
    private List<Song> songList;

    public SongFavourite_Fragment(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.songfavourite_fragment,container,false);
        rvListFvr = view.findViewById(R.id.rvListFvr);


        songDAO = new SongDAO(getActivity());
        songList = songDAO.getAll_Song_favourite("1");
        favouriteAdapter = new FavouriteAdapter(getActivity(),songList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvListFvr.setLayoutManager(linearLayoutManager);
        rvListFvr.setAdapter(favouriteAdapter);
        return view;
    }
}
