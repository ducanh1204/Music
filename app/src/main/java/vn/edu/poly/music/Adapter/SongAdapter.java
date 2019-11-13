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
import android.widget.Filter;
import android.widget.Filterable;
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
import java.util.ArrayList;
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
import static vn.edu.poly.music.Activity.MainActivity.checkRandom;
import static vn.edu.poly.music.Activity.MainActivity.checkRepeat;
import static vn.edu.poly.music.Activity.MainActivity.i;
import static vn.edu.poly.music.Activity.MainActivity.imgCD;
import static vn.edu.poly.music.Activity.MainActivity.imgNext;
import static vn.edu.poly.music.Activity.MainActivity.imgPlay;
import static vn.edu.poly.music.Activity.MainActivity.imgPrev;
import static vn.edu.poly.music.Activity.MainActivity.imgRandom;
import static vn.edu.poly.music.Activity.MainActivity.imgRepeat;
import static vn.edu.poly.music.Activity.MainActivity.list;
import static vn.edu.poly.music.Activity.MainActivity.load;
import static vn.edu.poly.music.Activity.MainActivity.mediaPlayer;
import static vn.edu.poly.music.Activity.MainActivity.seekBar;
import static vn.edu.poly.music.Activity.MainActivity.tvTenbaiHat;
import static vn.edu.poly.music.Activity.MainActivity.tvThoiGian2;
import static vn.edu.poly.music.Activity.MainActivity.tvThoigian1;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> implements Filterable {
    private Context context;
    private List<Song> songList;
    private RecyclerView rvListSong;
    private String mediaPath;
    private SongDAO songDAO;
    //    private static int i;
    private List<Song> songList2;


    public SongAdapter(Context context, RecyclerView rvListSong, List<Song> songList) {
        this.context = context;
        this.rvListSong = rvListSong;
        this.songList = songList;
        this.songList2 = new ArrayList<>(songList);
    }


    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongHolder holder, final int position) {
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
        if (songList.get(position).getYeuThich() == 1) {
            holder.imgYeuthich.setImageResource(R.drawable.traitimon);
            holder.imgYeuthich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Song song = new Song();
                    song.setTenBaiHat(songList.get(position).getTenBaiHat());
                    song.setTenCaSi(songList.get(position).getTenCaSi());
                    song.setFileMp3(songList.get(position).getFileMp3());
                    song.setYeuThich(0);
                    long result = songDAO.update(song);
                    if (result > 0) {
                        Toast.makeText(context, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        songList = songDAO.getAll();
                        rvListSong.setAdapter(SongAdapter.this);
                    }
                }
            });
        } else {
            holder.imgYeuthich.setImageResource(R.drawable.traitimof);
            holder.imgYeuthich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Song song = new Song();
                    song.setTenBaiHat(songList.get(position).getTenBaiHat());
                    song.setTenCaSi(songList.get(position).getTenCaSi());
                    song.setFileMp3(songList.get(position).getFileMp3());
                    song.setYeuThich(1);
                    long result = songDAO.update(song);
                    if (result > 0) {
                        Toast.makeText(context, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        songList = songDAO.getAll();
                        rvListSong.setAdapter(SongAdapter.this);
                    }
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = position;
                list = 1;
                checkRepeat = 0;
                checkRandom = 0;
                imgRandom.setImageResource(R.drawable.random_off);
                imgRepeat.setImageResource(R.drawable.repeate);
                if (load == 0) {
                    startSong(i);
                } else {
                    mediaPlayer.stop();
                    startSong(i);
                }
            }
        });
    }

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
                            if (load == 1) {
                                mediaPlayer.stop();
                            }
                            if (checkRandom == 0 && checkRepeat == 0) {
                                i++;

                                if (i > songList.size() - 1) {
                                    i = 0;
                                }
                            } else if (checkRandom == 1) {
                                i = startRandom(0, songList.size() - 1);
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


    private int startRandom(int min, int max) {
        int j = (int) (Math.random() * (max - min + 1) + min);
        return j;
    }

    private void createAddPlaylist(final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_dsthumuc);
        dialog.setTitle("Chọn album");
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
                    Toast.makeText(context, "Thêm thành công vào album " + thuMucList.get(i).getTenThuMuc(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Đã được thêm vào album " + thuMucList.get(i).getTenThuMuc(), Toast.LENGTH_SHORT).show();
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

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Song> songs = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                songs.addAll(songList2);
            } else {
                String fillParent = constraint.toString().toLowerCase().trim();
                for (Song item : songList2) {
                    if (item.getTenBaiHat().toLowerCase().contains(fillParent)) {
                        songs.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = songs;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            songList.clear();
            songList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        private TextView tvtenBaiHat, tvtenCaSi;
        private ImageView imgPopupmenu, imgYeuthich;

        public SongHolder(@NonNull View itemView) {
            super(itemView);
            tvtenBaiHat = itemView.findViewById(R.id.tvtenBaiHat);
            tvtenCaSi = itemView.findViewById(R.id.tvtenCaSi);
            imgPopupmenu = itemView.findViewById(R.id.imgPopupMenu);
            imgYeuthich = itemView.findViewById(R.id.imgYeuthich);
        }
    }
}
