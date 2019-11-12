package vn.edu.poly.music.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import vn.edu.poly.music.Activity.MainActivity;
import vn.edu.poly.music.Model.Playlist;
import vn.edu.poly.music.Model.Song;
import vn.edu.poly.music.Model.ThuMuc;
import vn.edu.poly.music.R;
import vn.edu.poly.music.SQLite.PlaylistDAO;
import vn.edu.poly.music.SQLite.SongDAO;
import vn.edu.poly.music.SQLite.ThuMucDAO;

import static vn.edu.poly.music.Activity.MainActivity.animation;
import static vn.edu.poly.music.Activity.MainActivity.i;
import static vn.edu.poly.music.Activity.MainActivity.imgCD;
import static vn.edu.poly.music.Activity.MainActivity.imgNext;
import static vn.edu.poly.music.Activity.MainActivity.imgPlay;
import static vn.edu.poly.music.Activity.MainActivity.imgPrev;
import static vn.edu.poly.music.Activity.MainActivity.list;
import static vn.edu.poly.music.Activity.MainActivity.load;
import static vn.edu.poly.music.Activity.MainActivity.mediaPlayer;
import static vn.edu.poly.music.Activity.MainActivity.seekBar;
import static vn.edu.poly.music.Activity.MainActivity.tvTenbaiHat;
import static vn.edu.poly.music.Activity.MainActivity.tvThoiGian2;
import static vn.edu.poly.music.Activity.MainActivity.tvThoigian1;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
    private Context context;
    private List<Song> songList;
    private RecyclerView rvListSong;
    private String mediaPath;
    private SongDAO songDAO;
//    private static int i;

    public SongAdapter(Context context, RecyclerView rvListSong, List<Song> songList) {
        this.context = context;
        this.rvListSong = rvListSong;
        this.songList = songList;
    }


    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, final int position) {
        songDAO = new SongDAO(context);
        holder.tvtenBaiHat.setText(songList.get(position).getTenBaiHat());
        holder.tvtenCaSi.setText(songList.get(position).getTenCaSi());
        holder.imgPopupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itemThemDSPhat:
                                createAddPlaylist(position);
                                break;
                            case R.id.itemSua:
//                                createDialogUpdateSong(position);
                                break;
                            case R.id.itemXoa:
                                createAlertDialag(position);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = position;
                list=1;
                if (load == 0) {
                    startSong(i);
                } else {
                    mediaPlayer.stop();
                    startSong(i);
                }
            }
        });


//        imgPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkPlay();
//
//            }
//        });
//        imgNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nextSong();
//            }
//        });
//        imgPrev.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                prevSong();
//            }
//        });
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                if (tvTenbaiHat.getText().toString().equals("Chưa chọn bài hát")) {
//                    Toast.makeText(context, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
//                } else {
//                    mediaPlayer.seekTo(seekBar.getProgress());
//                }
//            }
//        });
    }

//    private void checkPlay() {
//        if (tvTenbaiHat.getText().toString().equals("Chưa chọn bài hát")) {
//            Toast.makeText(context, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
//        } else {
//            if (mediaPlayer.isPlaying() == true) {
//                mediaPlayer.pause();
//                imgPlay.setImageResource(R.drawable.play);
//                load = 0;
//                imgCD.clearAnimation();
//            } else {
//                imgCD.startAnimation(animation);
//                mediaPlayer.start();
//                imgPlay.setImageResource(R.drawable.pause);
//                load = 1;
//            }
//
//            setTimeTotal();
//            updateTimeSong();
//        }
//    }

    private void startSong(int position) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Uri uri = Uri.parse(songList.get(position).getFileMp3());
        try {
            mediaPlayer.setDataSource(context, uri);
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
            Toast.makeText(context, "Loi phat nhac", Toast.LENGTH_SHORT).show();
        }
        tvTenbaiHat.setText(songList.get(position).getTenBaiHat() + " - " + songList.get(position).getTenCaSi());
        setTimeTotal();
        updateTimeSong();
    }

//    private void nextSong() {
//        if (tvTenbaiHat.getText().toString().equals("Chưa chọn bài hát")) {
//            Toast.makeText(context, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
//        } else {
//            i++;
//            if (load == 1) {
//                mediaPlayer.stop();
//            }
//            if (i > songList.size() - 1) {
//                i = 0;
//            }
//            startSong(i);
//            imgCD.startAnimation(animation);
//            setTimeTotal();
//            updateTimeSong();
//        }
//    }
//
//    private void prevSong() {
//        if (tvTenbaiHat.getText().toString().equals("Chưa chọn bài hát")) {
//            Toast.makeText(context, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
//        } else {
//            i--;
//            if (load == 1) {
//                mediaPlayer.stop();
//            }
//            if (i < 0) {
//                i = songList.size() - 1;
//            }
//            startSong(i);
//            imgCD.startAnimation(animation);
//            setTimeTotal();
//            updateTimeSong();
//        }
//    }

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
                        if (tvTenbaiHat.getText().toString().equals("Chưa chọn bài hát")) {
                            Toast.makeText(context, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
                        } else {
                            i++;
                            if (load == 1) {
                                mediaPlayer.stop();
                            }
                            if (i > songList.size() - 1) {
                                i = 0;
                            }
                            startSong(i);
                            imgCD.startAnimation(animation);
                            setTimeTotal();
                            updateTimeSong();
                        }
                    }
                });

                handler.postDelayed(this, 500);
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 100);
    }



    private void createAddPlaylist(final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_dsthumuc);
        dialog.setTitle("Chọn danh sách phát");
        dialog.show();
        ImageView imgClose;
        imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ListView lvListThuMuc;
        lvListThuMuc = dialog.findViewById(R.id.lvListThuMuc);
        ThuMucDAO thuMucDAO = new ThuMucDAO(context);
        final List<ThuMuc> thuMucList = thuMucDAO.getAll();
        LV_ThuMucAdapter lv_thuMucAdapter = new LV_ThuMucAdapter(context, thuMucList);
        lvListThuMuc.setAdapter(lv_thuMucAdapter);
        final PlaylistDAO playlistDAO = new PlaylistDAO(context);
        lvListThuMuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Playlist playlist = new Playlist();
                playlist.setTenBaiHat(songList.get(position).getTenBaiHat());
                playlist.setTenCaSi(songList.get(position).getTenCaSi());
                playlist.setFileMp3(songList.get(position).getFileMp3());
                playlist.setTenThuMuc(thuMucList.get(i).getTenThuMuc());
                long result = playlistDAO.insert(playlist);
                if (result > 0) {
                    Toast.makeText(context, "Thêm thành công vào danh sách phát " + thuMucList.get(i).getTenThuMuc(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Đã được thêm vào danh sách phát " + thuMucList.get(i).getTenThuMuc(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    private void createAlertDialag(final int position) {
        final PlaylistDAO playlistDAO = new PlaylistDAO(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có chắc chắn muốn xóa bài hát này không?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playlistDAO.delete_song(songList.get(position).getTenBaiHat() + "");
                songDAO.delete(songList.get(position).getTenBaiHat());
                songList.remove(position);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        private TextView tvtenBaiHat, tvtenCaSi;
        private ImageView imgPopupmenu;

        public SongHolder(@NonNull View itemView) {
            super(itemView);
            tvtenBaiHat = itemView.findViewById(R.id.tvtenBaiHat);
            tvtenCaSi = itemView.findViewById(R.id.tvtenCaSi);
            imgPopupmenu = itemView.findViewById(R.id.imgPopupMenu);
        }
    }
// Sửa bài hát
//    private void createDialogUpdateSong(final int position) {
//        final Dialog dialog = new Dialog(context);
//        dialog.setContentView(R.layout.dialog_update_song);
//        dialog.show();
//        dialog.setTitle("Thêm bài hát");
//        final EditText edtUpSinger, edtUpURL;
//        edtUpSinger = dialog.findViewById(R.id.edtUpSinger);
//        edtUpURL = dialog.findViewById(R.id.edtUpURL);
//        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (edtUpSinger.getText().toString().trim().equals("") || edtUpURL.getText().toString().trim().equals("")) {
//                    Toast.makeText(context, "Nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
//                } else {
//                    Song song = new Song();
//                    song.setTenBaiHat(songList.get(position).getTenBaiHat());
//                    song.setTenCaSi(edtUpSinger.getText().toString().trim());
//                    song.setFileMp3(edtUpURL.getText().toString().trim());
//                    long result = songDAO.update(song);
//                    if (result > 0) {
//                        Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
//                        songList = songDAO.getAll();
//                        rvListSong.setAdapter(SongAdapter.this);
//                    } else {
//                        Toast.makeText(context, "Sửa thất bại", Toast.LENGTH_SHORT).show();
//                    }
//                    dialog.dismiss();
//                }
//            }
//        });
//    }


}
