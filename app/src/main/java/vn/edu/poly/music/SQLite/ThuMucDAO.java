package vn.edu.poly.music.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.edu.poly.music.Model.ThuMuc;

public class ThuMucDAO {
    private MySqliteOpenHelper mySqliteOpenHelper;

    public ThuMucDAO(Context context) {
        this.mySqliteOpenHelper = new MySqliteOpenHelper(context);
    }

    private String USER_TABLE = "thumuc";

    public String tenThuMuc = "tenThuMuc";

    public List<ThuMuc> getAll() {
        List<ThuMuc> thuMucList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getReadableDatabase();

        String SQL = "SELECT * FROM " + USER_TABLE ;

        Cursor cursor = sqLiteDatabase.rawQuery(SQL, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    ThuMuc thuMuc = new ThuMuc();

                    thuMuc.setTenThuMuc(cursor.getString(cursor.getColumnIndex(tenThuMuc)));

                    thuMucList.add(thuMuc);
                    cursor.moveToNext();

                }
                cursor.close();
            }
        }

        return thuMucList;
    }

    public long insert(ThuMuc thuMuc) {
        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(tenThuMuc, thuMuc.getTenThuMuc());

        long result = sqLiteDatabase.insert(USER_TABLE, null, contentValues);
        sqLiteDatabase.close();
        return result;
    }

    public void delete(String id) {
        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getWritableDatabase();

        sqLiteDatabase.delete(USER_TABLE, tenThuMuc + "=?", new String[]{id});

    }

}
