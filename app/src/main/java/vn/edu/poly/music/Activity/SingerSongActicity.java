package vn.edu.poly.music.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import vn.edu.poly.music.Adapter.Singer_SongAdapter;
import vn.edu.poly.music.Model.Song;
import vn.edu.poly.music.R;
import vn.edu.poly.music.SQLite.SongDAO;

public class SingerSongActicity extends AppCompatActivity {

    private RecyclerView rvListSinger_Song;
    private List<Song> songList;
    private SongDAO songDAO;
    private String tenCaSi;
    private Singer_SongAdapter singer_songAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer_song_acticity);
        rvListSinger_Song = findViewById(R.id.rvListSinger_Song);
        songDAO = new SongDAO(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("truyenIntent2");
        tenCaSi = bundle.getString("tenCaSi");

        songList = songDAO.getAll_Singer_Song(tenCaSi);
        singer_songAdapter = new Singer_SongAdapter(this,songList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvListSinger_Song.setLayoutManager(linearLayoutManager);
        rvListSinger_Song.setAdapter(singer_songAdapter);

    }
}
