package com.example.wsapp1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import com.example.wsapp1.model.DataX;
import com.example.wsapp1.model.GetToken;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;


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
    List<DataX> models;
    int state = 0;
    Disposable disposable;


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
                List<DataX> dm = new ArrayList<>();

                // TODO Sort
                //       Sort.Companion.sort();

                for (DataX dataModel : App.dm.getmGetAutoFrom().getData()) {
                    String name = dataModel.getName().toLowerCase();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(this);

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


                            disposable = Observable.interval(1, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnDispose(()->{
                                        zapis.setBackgroundColor(Color.GRAY);
                                    })
                                    .subscribe(
                                            o->{
                                                if (state==0) {
                                                    zapis.setBackgroundColor(Color.RED);
                                                    state=1;
                                                }
                                                else {
                                                    zapis.setBackgroundColor(Color.GREEN);
                                                    state=0;
                                                }
                                            },n -> { Log.e("errrrrrror",n.toString()); }

                                    );





                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        disposable.dispose();

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


        App.apiHelper.getUserToken("admin", "admin")
                .subscribeOn(Schedulers.io())
                .flatMap(s -> App.apiHelper.getAutoForUser(s.getData().getHash()))

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    App.dm.setCarInfoForUser(o);
                    Toast.makeText(getBaseContext(), o.getData().get(0).getName(), Toast.LENGTH_SHORT).show();


                    recyc.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new AdapterM();
                    adapter.setDas(App.dm.getmGetAutoFrom().getData());
                    adapter.setDoOnClick(this);
                    recyc.setAdapter(adapter);

                    models = App.dm.getmGetAutoFrom().getData();
                }, t -> {
                    if (t instanceof UnknownHostException) {
                        Toast.makeText(getBaseContext(), "Отсутсвует интернет", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("ERROR", t.toString());
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
        startActivityForResult(mCameraIntent, 4);
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

        File file = new File(filename);
        RequestBody imagePart = RequestBody.create(MediaType.parse("image/*"), file);
        //.createFormData("profileImage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        App.apiHelper.addPhoto(7, "9048fb696c5f9045a78a5054888e05d5fbc4ad1e861c1bbafa0eb9daaa1e7c7e", imagePart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {

                }, t -> {
                    Log.e("Добавление фото", t.toString());
                });
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
            Bitmap mBit = null;
            try {
                mBit = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                mImg.setImageBitmap(mBit);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 4 && resultCode == RESULT_OK) {
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
        ArrayList<DataX> dm = new ArrayList<>();
        dm.addAll(models);
        models = Sort.Companion.sort(dm);
        adapter.setDas(models);
    }


    @OnClick(R.id.button_obr)
    void doSortObr() {
        adapter.setDas(App.dm.getmGetAutoFrom().getData());
    }


    @Override
    public void doClick(DataX das, int pos) {
        Toast.makeText(getBaseContext(), das.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            App.apiHelper.addAuto("9048fb696c5f9045a78a5054888e05d5fbc4ad1e861c1bbafa0eb9daaa1e7c7e", "31231312312321312", "Lada")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                        Toast.makeText(getBaseContext(), "Машина добавлена", Toast.LENGTH_SHORT).show();
                        //  adapter.setDas(o);
                    }, t -> {
                    });


        } else if (id == R.id.nav_gallery) {

            // MediaPlayer mediaPlayer= MediaPlayer.create(getBaseContext(),Uri.parse(filename));

            Uri uri = Uri.parse("http://q-arp.net:3030/" + App.dm.getmGetAutoFrom().getData().get(3).getPhotos().get(0).getUrl()) ;
            MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
            mediaPlayer.start();


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
