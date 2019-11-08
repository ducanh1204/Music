package vn.edu.poly.music.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.edu.poly.music.Model.Song;

public class SongDAO {
    private MySqliteOpenHelper mySqliteOpenHelper;

    public SongDAO(Context context) {
        this.mySqliteOpenHelper = new MySqliteOpenHelper(context);
    }

    private String USER_TABLE = "baihat";

    public String tenBaiHat = "tenBaiHat";
    public String tenCaSi = "tenCaSi";
    public String fileMp3 = "fileMp3";

    public List<Song> getAll() {
        List<Song> songList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getReadableDatabase();

        String SQL = "SELECT * FROM " + USER_TABLE ;

        Cursor cursor = sqLiteDatabase.rawQuery(SQL, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    Song song = new Song();

                    song.setTenBaiHat(cursor.getString(cursor.getColumnIndex(tenBaiHat)));
                    song.setTenCaSi(cursor.getString(cursor.getColumnIndex(tenCaSi)));
                    song.setFileMp3(Integer.parseInt(cursor.getString(cursor.getColumnIndex(fileMp3))));

                    songList.add(song);
                    cursor.moveToNext();

                }
                cursor.close();
            }
        }

        return songList;
    }

    public long insertSong(Song song) {
        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(tenBaiHat, song.getTenCaSi());
        contentValues.put(tenCaSi, song.getTenCaSi());
        contentValues.put(fileMp3, song.getFileMp3());

        long result = sqLiteDatabase.insert(USER_TABLE, null, contentValues);
        sqLiteDatabase.close();
        return result;
    }

    public void deleteUser(String id) {
        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getWritableDatabase();

        sqLiteDatabase.delete(USER_TABLE, tenBaiHat + "=?", new String[]{id});

    }
}
