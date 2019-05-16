package com.example.wsapp1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wsapp1.model.DataModel;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;



public class MainActivity extends AppCompatActivity implements AdapterM.DoOnClick, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.Button_zap)
    Button zapis;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView nav_view;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyc)
    RecyclerView recyc;
    @BindView(R.id.img_main)
    ImageView mImg;
    SearchView searchView;
    AdapterM adapter;
    String filename;
    MediaRecorder mediaRecorder;
    List<DataModel> models;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__menu, menu);
        MenuItem sr = menu.findItem(R.id.search);
        searchView = (SearchView) sr.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                List<DataModel> dm = new ArrayList<>();

                // TODO Sort
                //       Sort.Companion.sort();

                for (DataModel dataModel : App.dm.getDataModels()) {
                    String name = dataModel.getModel().toLowerCase();
                    if (name.contains(newText)) {
                        dm.add(dataModel);
                    }

                }
                adapter.setSearch(dm);
                return true;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        models = App.dm.getDataModels();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(this);

        recyc.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterM();
        adapter.setDas(App.dm.getDataModels());
        adapter.setDoOnClick(this);
        recyc.setAdapter(adapter);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);

        zapis.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        filename = Environment.getExternalStorageDirectory() + "/" + UUID.randomUUID() + ".3gpp";

                        try {
                            if (mediaRecorder != null) {
                                mediaRecorder.release();
                                mediaRecorder = null;
                            }
                            mediaRecorder = new MediaRecorder();
                            zapis.setText("Идет запись...");
                            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            mediaRecorder.setOutputFile(filename);
                            mediaRecorder.prepare();
                            mediaRecorder.start();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        if (mediaRecorder != null) {
                            mediaRecorder.stop();
                            Toast.makeText(getBaseContext(), "Запись успешна", Toast.LENGTH_SHORT).show();
                            zapis.setText("Записать");
                        }
                        break;
                    }
                }
                return true;

            }
        });
    }


    @OnLongClick(R.id.img_main)
    void doPicture() {
        String[] mStr = {"Загрузить", "Сохранить", "Сфотографировать"};
        AlertDialog mDial = new AlertDialog.Builder(this)
                .setItems(mStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0: {
                                loadPhoto();
                                Toast.makeText(getBaseContext(), "Загружено", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case 1: {
                                savePhoto();
                                Toast.makeText(getBaseContext(), "Сохранено", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case 2: {
                                doPhoto();
                                Toast.makeText(getBaseContext(), "Сфоткано", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                })
                .setTitle("Работа с фото")
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void doPhoto() {
        Intent mCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(mCameraIntent,4);
    }

    private void savePhoto() {
        /**
         *
         *  Метод сохранения фото
         *
         * author: Danil` Shakirov
         *
         */
        String mFileSavePhotoDir = Environment.getExternalStorageDirectory() + "/" + UUID.randomUUID() + ".png";
        Bitmap mBitmImg = ((BitmapDrawable) mImg.getDrawable()).getBitmap();
        try {
            OutputStream mOut = new FileOutputStream(mFileSavePhotoDir);
            mBitmImg.compress(Bitmap.CompressFormat.PNG, 100, mOut);
            mOut.flush();
            mOut.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPhoto() {
        /**
         *
         *  Метод загрузки фото
         *
         * author: Danil` Shakirov
         *
         */
        Intent intent = new Intent();
        intent.setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Сохр"), 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        /**
         *
         *  Метод получения ответов
         *
         * author: Danil` Shakirov
         *
         */
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri mUri = data.getData();
            try {
                Bitmap mBit = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                mImg.setImageBitmap(mBit);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 4 && resultCode == RESULT_OK)
        {
            Bitmap mThumbBit = (Bitmap) data.getExtras().get("data");
            mImg.setImageBitmap(mThumbBit);
        }
    }

    @OnClick(R.id.button_sort)
    void doSort() {
        /**
         *
         *  Метод сортировки
         *
         * author: Danil` Shakirov
         *
         */
        ArrayList<DataModel> dm = new ArrayList<>();
        dm.addAll(models);
        models = Sort.Companion.sort(dm);
        adapter.setDas(models);
    }




    @OnClick(R.id.button_obr)
    void doSortObr() {
        adapter.setDas(App.dm.getDataModels());
    }


    @Override
    public void doClick(DataModel das, int pos) {
        Toast.makeText(getBaseContext(), das.getModel(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
