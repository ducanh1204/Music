package vn.edu.poly.music.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.edu.poly.music.Fragment.ThuMuc_Fragment;
import vn.edu.poly.music.R;
import vn.edu.poly.music.Fragment.Singer_Fragment;
import vn.edu.poly.music.Fragment.Song_Fragment;
import vn.edu.poly.music.SQLite.MySqliteOpenHelper;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bnv;
    private Fragment fragment;
    private MySqliteOpenHelper mySqliteOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySqliteOpenHelper = new MySqliteOpenHelper(this);
        mySqliteOpenHelper.createDataBase();
        bnv = findViewById(R.id.bnv);
        fragment = new Song_Fragment();
        loadFragment(fragment);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_song:
                        fragment = new Song_Fragment();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
