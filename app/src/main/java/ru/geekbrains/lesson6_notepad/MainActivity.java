package ru.geekbrains.lesson6_notepad;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if (savedInstanceState == null) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ListNoteFragment listFragment = new ListNoteFragment();
        fragmentTransaction.replace(R.id.list_notes_fragment, listFragment);
        fragmentTransaction.commit();
//        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (navigateMenu(id)) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            return false;
        });
    }

    private boolean navigateMenu(int id) {
        switch (id) {
            case R.id.action_main:
                Toast.makeText(this, getResources().getString(R.string.main), Toast.LENGTH_LONG).show();
                return true;
            case R.id.nav_settings:
                Toast.makeText(this, getResources().getString(R.string.settings), Toast.LENGTH_LONG).show();
                return true;
            case R.id.nav_about:
                Toast.makeText(this, getResources().getString(R.string.about), Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) search.getActionView();

        MenuItem sort = menu.findItem(R.id.menu_sort);
        MenuItem addPhoto = menu.findItem(R.id.menu_add_photo);
        MenuItem send = menu.findItem(R.id.menu_send);

        send.setOnMenuItemClickListener(this);
        addPhoto.setOnMenuItemClickListener(this);
        sort.setOnMenuItemClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(MainActivity.this, newText, Toast.LENGTH_LONG).show();
                return true;
            }
        });
        return true;
    }

/*    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_sort){
            Toast.makeText(this, "Sort selected", Toast.LENGTH_LONG).show();
            return true;
        }
        if (itemId == R.id.menu_send){
            Toast.makeText(this, "Send selected", Toast.LENGTH_LONG).show();
            return true;
        }
        if (itemId == R.id.menu_add_photo){
            Toast.makeText(this, "Add photo selected", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(this, item.getTitle().toString(), Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean checkLandScapeOrientation() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}