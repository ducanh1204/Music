package vn.edu.poly.music.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import vn.edu.poly.music.Adapter.SongAdapter;
import vn.edu.poly.music.Fragment.ThuMuc_Fragment;
import vn.edu.poly.music.Model.Song;
import vn.edu.poly.music.Model.ThuMuc;
import vn.edu.poly.music.R;
import vn.edu.poly.music.Fragment.Singer_Fragment;
import vn.edu.poly.music.Fragment.Song_Fragment;
import vn.edu.poly.music.SQLite.MySqliteOpenHelper;
import vn.edu.poly.music.SQLite.SongDAO;
import vn.edu.poly.music.SQLite.ThuMucDAO;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bnv;
    private Fragment fragment;
    private MySqliteOpenHelper mySqliteOpenHelper;
    public static MediaPlayer mediaPlayer;
    public static Dialog dialog;
    private List<Song> songList;
    private SongDAO songDAO;
    private ImageView imgCD, imgprev, imgPlay, imgNext;
    private TextView tvtenbaiHat, tvTenCaSi;
    public static int load = 0;
    public static Animation animation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySqliteOpenHelper = new MySqliteOpenHelper(this);
        mySqliteOpenHelper.createDataBase();
        bnv = findViewById(R.id.bnv);
        imgCD = findViewById(R.id.imgCD);
        imgprev = findViewById(R.id.imgPrev);
        imgPlay = findViewById(R.id.imgPlay);
        imgNext = findViewById(R.id.imgNext);
        tvtenbaiHat = findViewById(R.id.tvtenBaiHat);
        tvTenCaSi = findViewById(R.id.tvtenCaSi);
        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);


        songDAO = new SongDAO(this);
        songList = songDAO.getAll();
        fragment = new Song_Fragment(this, songList, imgCD, imgprev, imgPlay, imgNext, tvTenCaSi, tvtenbaiHat);
        loadFragment(fragment);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_song:
                        fragment = new Song_Fragment(MainActivity.this, songList, imgCD, imgprev, imgPlay, imgNext, tvTenCaSi, tvtenbaiHat);
                        loadFragment(fragment);
                        return true;
                    case R.id.item_playlist:
                        fragment = new ThuMuc_Fragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.item_singer:
                        fragment = new Singer_Fragment();
                        loadFragment(fragment);
                        return true;
                }
                return false;
            }
        });
        findViewById(R.id.ll2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_music);
                dialog.show();
                dialog.findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_AddSong:
                createDialogAddSong();
                break;
            case R.id.item_AddPlaylist:
                createDialogAddPlaylist();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void createDialogAddSong() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_add_song);
        dialog.show();
        dialog.setTitle("Thêm bài hát");
        final EditText edtAddSong, edtAddSinger, edtAddURL;
        edtAddSinger = dialog.findViewById(R.id.edtAddSinger);
        edtAddSong = dialog.findViewById(R.id.edtAddSong);
        edtAddURL = dialog.findViewById(R.id.edtAddURL);
        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAddSinger.getText().toString().trim().equals("") || edtAddSong.getText().toString().trim().equals("") || edtAddURL.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "Nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                } else {
                    Song song = new Song();
                    song.setTenBaiHat(edtAddSong.getText().toString().trim());
                    song.setTenCaSi(edtAddSinger.getText().toString().trim());
                    song.setFileMp3(edtAddURL.getText().toString().trim());
                    long result = songDAO.insert(song);
                    if (result > 0) {
                        Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        fragment = new Song_Fragment(MainActivity.this, songList, imgCD, imgprev, imgPlay, imgNext, tvTenCaSi, tvtenbaiHat);
                        loadFragment(fragment);
                    } else {
                        Toast.makeText(MainActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            }
        });
    }

    private void createDialogAddPlaylist() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_add_playlist);
        dialog.show();
        dialog.setTitle("Thêm bài hát");
        final EditText edtAddPlaylist;
        edtAddPlaylist = dialog.findViewById(R.id.edtAddPlaylist);
        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ThuMucDAO thuMucDAO = new ThuMucDAO(MainActivity.this);
        List<ThuMuc> thuMucList=thuMucDAO.getAll();
        dialog.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAddPlaylist.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "Nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                }
            }
        });
    }
}
