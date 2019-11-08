package vn.edu.poly.music.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import vn.edu.poly.music.Adapter.PlaylistAdapter;
import vn.edu.poly.music.Model.Playlist;
import vn.edu.poly.music.R;
import vn.edu.poly.music.SQLite.PlaylistDAO;

public class PlaylistActivity extends AppCompatActivity {
    private List<Playlist> playlistList;
    private PlaylistDAO playlistDAO;
    private RecyclerView rvListPlaylist;
    private PlaylistAdapter playlistAdapter;
    private String tenThuMuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        rvListPlaylist = findViewById(R.id.rvListPlaylist);
        playlistDAO = new PlaylistDAO(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("truyenIntent");
        tenThuMuc = bundle.getString("tenThuMuc");

        playlistList = playlistDAO.getAll(tenThuMuc);
        playlistAdapter = new PlaylistAdapter(this, playlistList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvListPlaylist.setLayoutManager(linearLayoutManager);
        rvListPlaylist.setAdapter(playlistAdapter);
    }
}
