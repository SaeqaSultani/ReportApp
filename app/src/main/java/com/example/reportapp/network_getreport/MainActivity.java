package com.example.reportapp.network_getreport;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reportapp.About;
import com.example.reportapp.BuildConfig;
import com.example.reportapp.netnwork_postreport.ReportActivity;
import com.example.reportapp.R;

import com.example.reportapp.network_loginpage.LoginPageActivity;
import com.example.reportapp.network_signuppage.ApiClient;
import com.example.reportapp.util.MySharedPreferences;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Locale;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import es.dmoral.toasty.Toasty;
import mehdi.sakout.aboutpage.AboutPage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private List<ClassGetReport> get_data_class;
    private ApiInterfaceGetReport apiInterface;
    private MySharedPreferences preferences;
    private long aLong;
    Toast toast;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    AvatarView avatarView;
    IImageLoader imageLoader;
    TextView tv_name,tv_lastname,tv_section;
    TextView tv_no_item;
    ImageView image_no_item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        tv_no_item = findViewById(R.id.txt_no_item);
        image_no_item = findViewById(R.id.image_no_item);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //sharedPrefereces
        preferences = MySharedPreferences.getInstance(this);
        final int id = preferences.getId();

        apiInterface = ApiClient.getApiClient().create(ApiInterfaceGetReport.class);

        Call<List<ClassGetReport>> getReportCall = apiInterface.GetReport(id);
        getReportCall.enqueue(new Callback<List<ClassGetReport>>() {
            @Override
            public void onResponse(Call<List<ClassGetReport>> call, Response<List<ClassGetReport>> response) {

                if (!response.isSuccessful()){

                    Toasty.warning(MainActivity.this, R.string.warning_toast, Toast.LENGTH_SHORT, true).show();

//                    Toast.makeText(MainActivity.this, "the error cod is"+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }


                get_data_class =  response.body();
                adapter = new RecyclerViewAdapter(get_data_class,MainActivity.this);
                recyclerView.setAdapter(adapter);


               // when add report the Visibility of image and text become gone

                if (!get_data_class.isEmpty()){

                    tv_no_item.setVisibility(View.GONE);
                    image_no_item.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<ClassGetReport>> call, Throwable t) {

                Toasty.error(MainActivity.this, R.string.error_toast, Toast.LENGTH_SHORT, true).show();

//                Toast.makeText(getApplicationContext(),"onFailure"+t.getMessage(),Toast.LENGTH_LONG).show();
//                Log.i("TAGS", "onFailure: " + t);

            }
        });


        tv_name = navigationView.getHeaderView(0).findViewById(R.id.text_view_name);
        tv_name.setText(preferences.getName());
        tv_lastname = navigationView.getHeaderView(0).findViewById(R.id.text_view_last_name);
        tv_lastname.setText(preferences.getLastName());
        tv_section = navigationView.getHeaderView(0).findViewById(R.id.text_view_section);
        tv_section.setText(preferences.getTeam());
        avatarView = navigationView.getHeaderView(0).findViewById(R.id.user_avatar);




        imageLoader = new PicassoLoader();
        imageLoader.loadImage(avatarView, "http:/example.com/user/someUserAvatar.png", preferences.getName());

        getSupportActionBar().setSubtitle(getString( R.string.user) + " : " + preferences.getName() + " " + preferences.getLastName());

        actionBarDrawerToggle.syncState();



    }


    @Override
    public void onBackPressed() {

        if (aLong + 2000 > System.currentTimeMillis()){
            toast.cancel();
            super.onBackPressed();
            return;
        }else {

            toast = Toast.makeText(this, R.string.exist, Toast.LENGTH_SHORT);
            toast.show();
        }
        aLong = System.currentTimeMillis();

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int id=item.getItemId();
        Intent intent;

        switch (id){

            case R.id.add_report:

                 intent = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intent);

                break;

            case R.id.abuot:

                 intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);

                break;

            case R.id.logout:
                    logoutDialog();
                break;

            case R.id.share:
                ShareApp();

        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }



    private void logoutDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logout);
        builder.setMessage(R.string.logout_text);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this,LoginPageActivity.class);
                        preferences.setSignInStatus(false);
                        preferences.clearAll();
                        startActivity(intent);
                        finish();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        Intent intent;
        switch (id){

            case R.id.action_about:
                intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
                break;
            case R.id.action_logout:
                logoutDialog();
                break;
//            case R.id.action_deleteAll:
//                Toast.makeText(this, "DeleteAll", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage = "\nLet me recommend you application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.choose_one)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
