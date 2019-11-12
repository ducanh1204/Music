package vn.edu.poly.music.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import vn.edu.poly.music.Adapter.SongAdapter;
import vn.edu.poly.music.Fragment.ThuMuc_Fragment;
import vn.edu.poly.music.Model.Playlist;
import vn.edu.poly.music.Model.Song;
import vn.edu.poly.music.Model.ThuMuc;
import vn.edu.poly.music.R;
import vn.edu.poly.music.Fragment.Singer_Fragment;
import vn.edu.poly.music.Fragment.Song_Fragment;
import vn.edu.poly.music.SQLite.MySqliteOpenHelper;
import vn.edu.poly.music.SQLite.PlaylistDAO;
import vn.edu.poly.music.SQLite.SongDAO;
import vn.edu.poly.music.SQLite.ThuMucDAO;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bnv;
    private Fragment fragment;
    private MySqliteOpenHelper mySqliteOpenHelper;
    public static MediaPlayer mediaPlayer;
    private SongDAO songDAO;
    private String mediaPath = "";
    public static final int REQUEST_CODE = 1;


    public static Animation animation;
    public static int load = 0;
    public static ImageView imgCD, imgPrev, imgPlay, imgNext, imgRandom, imgRepeat;
    public static TextView tvTenbaiHat, tvThoigian1, tvThoiGian2;
    public static SeekBar seekBar;
    public static int i;
    public static int list;

    private List<Song> songList1;
    private List<Song> songList2;
    private List<Playlist> playlistList;
    private ThuMucDAO thuMucDAO;

    public static String tenCasi;
    public static String tenThuMuc;
    private PlaylistDAO playlistDAO;
    public static int checkRandom = 0;
    public static int checkRepeat = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySqliteOpenHelper = new MySqliteOpenHelper(this);
        mySqliteOpenHelper.createDataBase();
        bnv = findViewById(R.id.bnv);
        imgCD = findViewById(R.id.imgCD);
        imgPrev = findViewById(R.id.imgPrev);
        imgPlay = findViewById(R.id.imgPlay);
        imgNext = findViewById(R.id.imgNext);
        tvTenbaiHat = findViewById(R.id.tvtenBaiHat);
        imgRandom = findViewById(R.id.imgRandom);
        imgRepeat = findViewById(R.id.imgRepeat);
        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);

        seekBar = findViewById(R.id.SeekBar);
        tvThoigian1 = findViewById(R.id.tvthoigian1);
        tvThoiGian2 = findViewById(R.id.tvthoigian2);

        playlistDAO = new PlaylistDAO(this);
        songDAO = new SongDAO(this);
        thuMucDAO = new ThuMucDAO(this);


        fragment = new Song_Fragment(this, songList1);
        loadFragment(fragment);


        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_song:
                        fragment = new Song_Fragment(MainActivity.this, songList1);
                        loadFragment(fragment);
                        return true;
                    case R.id.item_playlist:
                        fragment = new ThuMuc_Fragment(MainActivity.this, playlistList);
                        loadFragment(fragment);
                        return true;
                    case R.id.item_singer:
                        fragment = new Singer_Fragment(MainActivity.this, songList2);
                        loadFragment(fragment);
                        return true;
                }
                return false;
            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPlay();

            }
        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });
        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevSong();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (tvTenbaiHat.getText().toString().equals("Chưa chọn bài hát")) {
                    Toast.makeText(MainActivity.this, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
                } else {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });

        imgRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRandom == 0) {
                    imgRandom.setImageResource(R.drawable.random_on);
                    checkRandom = 1;
                    if (checkRepeat == 1) {
                        checkRepeat = 0;
                        imgRepeat.setImageResource(R.drawable.repeate);
                    }
                } else {
                    checkRandom = 0;
                    imgRandom.setImageResource(R.drawable.random_off);
                }
            }
        });
        imgRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRepeat == 0) {
                    imgRepeat.setImageResource(R.drawable.repeat_one);
                    checkRepeat = 1;
                    if (checkRandom == 1) {
                        checkRandom = 0;
                        imgRandom.setImageResource(R.drawable.random_off);
                    }
                } else {
                    checkRepeat = 0;
                    imgRepeat.setImageResource(R.drawable.repeate);
                }
            }
        });


    }


    private void checkPlay() {
        if (tvTenbaiHat.getText().toString().equals("Chưa chọn bài hát")) {
            Toast.makeText(MainActivity.this, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
        } else {
            if (mediaPlayer.isPlaying() == true) {
                mediaPlayer.pause();
                imgPlay.setImageResource(R.drawable.play);
                load = 0;
                imgCD.clearAnimation();
            } else {
                imgCD.startAnimation(animation);
                mediaPlayer.start();
                imgPlay.setImageResource(R.drawable.pause);
                load = 1;
            }

            setTimeTotal();
            updateTimeSong();
        }
    }

    private void startSong(int position) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (list == 1) {
            Uri uri = Uri.parse(songList1.get(position).getFileMp3());
            tvTenbaiHat.setText(songList1.get(position).getTenBaiHat() + " - " + songList1.get(position).getTenCaSi());
            check(uri);
        } else if (list == 2) {
            Uri uri = Uri.parse(playlistList.get(position).getFileMp3());
            tvTenbaiHat.setText(playlistList.get(position).getTenBaiHat() + " - " + playlistList.get(position).getTenCaSi());
            check(uri);
        } else if (list == 3) {
            Uri uri = Uri.parse(songList2.get(position).getFileMp3());
            tvTenbaiHat.setText(songList2.get(position).getTenBaiHat() + " - " + songList2.get(position).getTenCaSi());
            check(uri);
        }
        setTimeTotal();
        updateTimeSong();
    }

    private void check(Uri uri) {
        try {
            mediaPlayer.setDataSource(MainActivity.this, uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            imgCD.startAnimation(animation);
            load = 1;
            if (mediaPlayer.isPlaying() == true) {
                imgPlay.setImageResource(R.drawable.pause);
            } else {
                imgPlay.setImageResource(R.drawable.play);
            }
        } catch (IOException e) {
            e.printStackTrace();
            imgCD.clearAnimation();
            Toast.makeText(MainActivity.this, "Loi phat nhac", Toast.LENGTH_SHORT).show();
        }
    }

    private void nextSong() {
        if (tvTenbaiHat.getText().toString().equals("Chưa chọn bài hát")) {
            Toast.makeText(MainActivity.this, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
        } else {
            if (load == 1) {
                mediaPlayer.stop();
            }
            if (checkRandom == 0 && checkRepeat == 0) {
                i++;
                if (list == 1) {
                    songList1 = songDAO.getAll();
                    if (i > songList1.size() - 1) {
                        i = 0;
                    }
                } else if (list == 2) {
                    playlistList = playlistDAO.getAll(tenThuMuc);
                    if (i > playlistList.size() - 1) {
                        i = 0;
                    }
                } else if (list == 3) {
                    songList2 = songDAO.getAll_Singer_Song(tenCasi);
                    if (i > songList2.size() - 1) {
                        i = 0;
                    }
                }
            } else if (checkRandom == 1) {
                if (list == 1) {
                    songList1 = songDAO.getAll();
                    i = startRandom(0, songList1.size() - 1);
                } else if (list == 2) {
                    playlistList = playlistDAO.getAll(tenThuMuc);
                    i = startRandom(0, playlistList.size() - 1);
                } else if (list == 3) {
                    songList2 = songDAO.getAll_Singer_Song(tenCasi);
                    i = startRandom(0, songList2.size() - 1);
                }
            } else if (checkRepeat == 1) {
                if (list == 1) {
                    songList1 = songDAO.getAll();
                } else if (list == 2) {
                    playlistList = playlistDAO.getAll(tenThuMuc);

                } else if (list == 3) {
                    songList2 = songDAO.getAll_Singer_Song(tenCasi);
                }
            }
            startSong(i);
            imgCD.startAnimation(animation);
            setTimeTotal();
            updateTimeSong();
        }

    }

    private void prevSong() {
        if (tvTenbaiHat.getText().toString().equals("Chưa chọn bài hát")) {
            Toast.makeText(MainActivity.this, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
        } else {
            i--;
            if (load == 1) {
                mediaPlayer.stop();
            }
            if (list == 1) {
                songList1 = songDAO.getAll();
                if (i < 0) {
                    i = songList1.size() - 1;
                }
            } else if (list == 2) {
                playlistList = playlistDAO.getAll(tenThuMuc);
                if (i < 0) {
                    i = playlistList.size() - 1;
                }
            } else if (list == 3) {
                songList2 = songDAO.getAll_Singer_Song(tenCasi);
                if (i < 0) {
                    i = songList2.size() - 1;
                }
            }
            startSong(i);
            imgCD.startAnimation(animation);
            setTimeTotal();
            updateTimeSong();
        }
    }

    private void setTimeTotal() {
        SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
        tvThoiGian2.setText(dinhDangGio.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void updateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
                tvThoigian1.setText(dinhDangGio.format(mediaPlayer.getCurrentPosition()));
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nextSong();
                    }
                });

                handler.postDelayed(this, 500);
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 100);
    }

    private int startRandom(int min, int max) {
        int j = (int) (Math.random() * (max - min + 1) + min);
        return j;
    }

    // Create menu
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
                        fragment = new Song_Fragment(MainActivity.this, songList1);
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
        dialog.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAddPlaylist.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "Nhập dữ liệu", Toast.LENGTH_SHORT).show();
                } else {
                    ThuMuc thuMuc = new ThuMuc();
                    thuMuc.setTenThuMuc(edtAddPlaylist.getText().toString().trim());
                    long result = thuMucDAO.insert(thuMuc);
                    if (result > 0) {
                        Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        fragment = new ThuMuc_Fragment(MainActivity.this, playlistList);
                        loadFragment(fragment);
                    } else {
                        Toast.makeText(MainActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
