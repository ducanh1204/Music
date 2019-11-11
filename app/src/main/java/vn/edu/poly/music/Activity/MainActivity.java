package vn.edu.poly.music.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
    public static int list;
    public static Animation animation;
    private String mediaPath = "";
    public static final int REQUEST_CODE = 1;
    Animation animation2;


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
        animation2=AnimationUtils.loadAnimation(this, R.anim.disc_rotate);


        songDAO = new SongDAO(this);
        songList = songDAO.getAll();


        fragment = new Song_Fragment(this,songList,imgCD,imgprev,imgPlay,imgNext,tvtenbaiHat,tvTenCaSi);
        loadFragment(fragment);

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_song:
                        fragment = new Song_Fragment(MainActivity.this, songList,imgCD,imgprev,imgPlay,imgNext,tvtenbaiHat,tvTenCaSi);
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
                final ImageView btnPlay, btnPrev, btnNext, cd_music_icon;
                cd_music_icon = dialog.findViewById(R.id.cd_music_icon);
                btnPrev = dialog.findViewById(R.id.btnprev);
                btnPlay = dialog.findViewById(R.id.btnplay);
                btnNext = dialog.findViewById(R.id.btnnext);
                if (load == 0) {
                    btnPlay.setImageResource(R.drawable.play);
                } else {
                    btnPlay.setImageResource(R.drawable.pause);
                    cd_music_icon.startAnimation(animation2);
                }
                btnPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvtenbaiHat.getText().toString().equals("Chưa có bài hát nào")) {
                            Toast.makeText(MainActivity.this, "Chưa có bài hát nào", Toast.LENGTH_SHORT).show();
                        } else {
                            if (mediaPlayer.isPlaying() == true) {
                                mediaPlayer.pause();
                                imgPlay.setImageResource(R.drawable.play);
                                btnPlay.setImageResource(R.drawable.play);
                                MainActivity.load = 0;
                                cd_music_icon.clearAnimation();
                                cd_music_icon.setAnimation(null);
                            } else {
                                mediaPlayer.start();

                                imgPlay.setImageResource(R.drawable.pause);
                                btnPlay.setImageResource(R.drawable.pause);

                                cd_music_icon.startAnimation(animation2);
                                imgCD.startAnimation(animation);
                                MainActivity.load = 1;
                            }
                        }
                    }
                });




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

    // Selected Item
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


    // open Fragment
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Thêm bài hát
    private void createDialogAddSong() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_add_song);
        dialog.show();
        dialog.setTitle("Thêm bài hát");
        final EditText edtAddSong, edtAddSinger;
        Button btnAddFile;
        edtAddSinger = dialog.findViewById(R.id.edtAddSinger);
        edtAddSong = dialog.findViewById(R.id.edtAddSong);
        btnAddFile = dialog.findViewById(R.id.btnAddFile);
        dialog.setCancelable(false);
        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mediaPath = "";
            }
        });
        btnAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });
        dialog.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAddSinger.getText().toString().trim().equals("") || edtAddSong.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "Nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                } else if (mediaPath.equals("")) {
                    Toast.makeText(MainActivity.this, "Chọn 1 file", Toast.LENGTH_SHORT).show();
                } else {
                    Song song = new Song();
                    song.setTenBaiHat(edtAddSong.getText().toString().trim());
                    song.setTenCaSi(edtAddSinger.getText().toString().trim());
                    song.setFileMp3(mediaPath);
                    long result = songDAO.insert(song);
                    if (result > 0) {
                        Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        fragment = new Song_Fragment(MainActivity.this, songList,imgCD,imgprev,imgPlay,imgNext,tvtenbaiHat,tvTenCaSi);
                        loadFragment(fragment);
                    } else {
                        Toast.makeText(MainActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                    mediaPath = "";
                    dialog.dismiss();
                }
            }
        });
    }

    //Thêm danh sách phát
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
        List<ThuMuc> thuMucList = thuMucDAO.getAll();
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

    // Tìm file
    public void startSearch() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == -1) {
                    Uri fileUri = data.getData();
                    mediaPath = fileUri.toString();
                }

                break;
        }
    }
}
