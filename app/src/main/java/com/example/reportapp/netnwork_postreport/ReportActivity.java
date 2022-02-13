package com.example.reportapp.netnwork_postreport;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.reportapp.network_getreport.MainActivity;
import com.example.reportapp.R;
import com.example.reportapp.network_getreport.RecyclerViewAdapter;
import com.example.reportapp.network_loginpage.LoginPageActivity;
import com.example.reportapp.network_signuppage.ApiClient;
import com.example.reportapp.network_signuppage.SignupPageActivity;
import com.example.reportapp.util.MySharedPreferences;

import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {

    private EditText et_report;
    private MySharedPreferences preferences;
    private int id;
    private ProgressBar progressBar;
    private TextView tv_load;
    Handler handler;
    Runnable runnable;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        find();
        initToolbar();

        //sharedPrefereces
        preferences = MySharedPreferences.getInstance(this);
        id = preferences.getId();
//        Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();

        Button button = findViewById(R.id.btn_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String time = et_time.getText().toString();
                String report = et_report.getText().toString();

                if (report.isEmpty()) {
                    et_report.setError(getString(R.string.write_report));
                    et_report.findFocus();
                    return;
                }

                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
                tv_load.setVisibility(View.VISIBLE);

                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        timer.cancel();
                        progressBar.setVisibility(View.GONE);
                        tv_load.setVisibility(View.GONE);
                    }
                };

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        handler.post(runnable);
                    }
                },3000,1000);


                //Connecting with Server

                ApiInterfaceReport apiInterface = ApiClient.getApiClient().create(ApiInterfaceReport.class);
                Call<ClassReport> call = apiInterface.uploadReport(report, id);

                call.enqueue(new Callback<ClassReport>() {
                    @Override
                    public void onResponse(Call<ClassReport> call, Response<ClassReport> response) {

                        if (!response.isSuccessful()) {

                            Toasty.warning(ReportActivity.this, R.string.warning_toast, Toast.LENGTH_SHORT, true).show();

//                            Toast.makeText(ReportActivity.this, "the error code is : " + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ClassReport modelClass = response.body();

                        Toasty.success(ReportActivity.this, R.string.success_toast, Toast.LENGTH_SHORT, true).show();

//                        Toast.makeText(ReportActivity.this, "server response is success" + modelClass.getResponse(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ClassReport> call, Throwable t) {

                        Toasty.error(ReportActivity.this, R.string.error_toast, Toast.LENGTH_SHORT, true).show();
//                        Toast.makeText(ReportActivity.this, "server field " + t, Toast.LENGTH_SHORT).show();
//                        Log.i("TAGS", "onFailure: " + t);

                    }
                });


            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void find() {


        et_report = findViewById(R.id.activity_report_EditText_report);
        progressBar = findViewById(R.id.spin_kit_report);
        tv_load = findViewById(R.id.text_view_load_report);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

//            Intent intent = new Intent(ReportActivity.this, MainActivity.class);
//            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

}
