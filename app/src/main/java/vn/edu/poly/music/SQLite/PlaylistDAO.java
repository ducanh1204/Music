package vn.edu.poly.music.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.edu.poly.music.Model.Playlist;

public class PlaylistDAO {
    private MySqliteOpenHelper mySqliteOpenHelper;

    public PlaylistDAO(Context context) {
        this.mySqliteOpenHelper = new MySqliteOpenHelper(context);
    }

    private String USER_TABLE = "playlist";

    public String tenBaiHat = "tenaihat";
    public String tenCaSi = "tencasi";
    public String fileMp3 = "filemp3";
    public String tenThuMuc = "tenthumuc";

    public List<Playlist> getAll(String tenthumuc) {
        List<Playlist> playlistList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getReadableDatabase();

        String SQL = "SELECT * FROM " + USER_TABLE + " WHERE tenthumuc =? "  ;

        Cursor cursor = sqLiteDatabase.rawQuery(SQL, new String[]{tenthumuc});

        if (cursor != null) {
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    Playlist playlist = new Playlist();

                    playlist.setTenBaiHat(cursor.getString(cursor.getColumnIndex(tenBaiHat)));
                    playlist.setTenCaSi(cursor.getString(cursor.getColumnIndex(tenCaSi)));
                    playlist.setFileMp3(cursor.getString(cursor.getColumnIndex(fileMp3)));
                    playlist.setTenThuMuc(cursor.getString(cursor.getColumnIndex(tenThuMuc)));

                    playlistList.add(playlist);
                    cursor.moveToNext();

                }
                cursor.close();
            }
        }

        return playlistList;
    }


    public long insert(Playlist playlist) {
        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(tenBaiHat, playlist.getTenBaiHat());
        contentValues.put(tenCaSi, playlist.getTenCaSi());
        contentValues.put(fileMp3, playlist.getFileMp3());
        contentValues.put(tenThuMuc, playlist.getTenThuMuc());

        long result = sqLiteDatabase.insert(USER_TABLE, null, contentValues);
        sqLiteDatabase.close();
        return result;
    }

    public void delete(String id) {
        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getWritableDatabase();

        sqLiteDatabase.delete(USER_TABLE, tenBaiHat + "=?", new String[]{id});

    }
}
