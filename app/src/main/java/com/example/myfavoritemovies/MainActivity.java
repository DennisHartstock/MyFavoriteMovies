package com.example.myfavoritemovies;

import android.os.Bundle;

import com.example.myfavoritemovies.model.Genre;
import com.example.myfavoritemovies.model.Movie;
import com.example.myfavoritemovies.viewmodel.MainActivityViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myfavoritemovies.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private MainActivityViewModel mainActivityViewModel;
    private MainActivityClickHandlers mainActivityClickHandlers;
    private Genre selectedGenre;
    private ArrayList<Genre> genreArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.toolbar);

        mainActivityViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(MainActivityViewModel.class);

        mainActivityViewModel.getGenres().observe(this, new Observer<List<Genre>>() {
            @Override
            public void onChanged(List<Genre> genres) {
                genreArrayList = (ArrayList<Genre>) genres;

                for (Genre genre : genres) {
                    Log.d("myTag", genre.getGenreName());
                }

                showInSpinner();
            }

            private void showInSpinner() {
                ArrayAdapter<Genre> genreArrayAdapter = new ArrayAdapter<Genre>(this,
                        R.layout.spinner_item, genreArrayList);
                genreArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                binding.setSpinnerAdapter(genreArrayAdapter);
            }
        });

        mainActivityViewModel.getGenreMovies(2).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                for (Movie movie : movies) {
                    Log.d("myTag", movie.getMovieName());
                }
            }
        });

        mainActivityClickHandlers = new MainActivityClickHandlers();
        binding.setClickHandlers(mainActivityClickHandlers);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    public class MainActivityClickHandlers {

        public void onFabClicked(View view) {
            Toast.makeText(MainActivity.this, "Button is clicked", Toast.LENGTH_SHORT).show();
        }

        public void onSelectedItem(AdapterView<?> parent, View view, int position, long id) {
            selectedGenre = (Genre) parent.getItemAtPosition(position);
            String message = "id is " + selectedGenre.getId() + "\n name is " + selectedGenre.getGenreName();
            Toast.makeText(parent.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }


}