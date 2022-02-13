package com.example.reportapp.network_signuppage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reportapp.R;
import com.example.reportapp.network_getreport.MainActivity;
import com.example.reportapp.network_loginpage.LoginPageActivity;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.luckyfirefly.dropdownmenu.DropDownMenu;
import com.luckyfirefly.dropdownmenu.adapter.BaseDropdownView;
import com.luckyfirefly.dropdownmenu.adapter.DropdownListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupPageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText etName, etLastName,etUsername, etPassword;
    private TextView tv_section,tv_load;
    private Spinner spinner;
    private ProgressBar progressBar;
    Handler handler;
    Runnable runnable;
    Timer timer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup_page);


        find();
        initToolbar();
        Button button = findViewById(R.id.btn_sign_up);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name = etName.getText().toString();
                String last_name = etLastName.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String section = tv_section.getText().toString();


                if (name.isEmpty()) {
                    etName.setError(getString(R.string.write_name));
                    etName.findFocus();
                    return;
                }
                if (last_name.isEmpty()) {
                    etLastName.setError(getString(R.string.write_last_name));
                    etLastName.findFocus();
                    return;
                }
                if (username.isEmpty()) {
                    etUsername.setError(getString(R.string.write_user_name));
                    etUsername.findFocus();
                    return;
                }
                if (password.length() < 6) {
                    etPassword.setError(getString(R.string.write_password));
                    etPassword.findFocus();
                    return;
                }

                if (spinner.getSelectedItemPosition() == 0){
                    Toasty.warning(SignupPageActivity.this, getString(R.string.choose_section), Toasty.LENGTH_SHORT).show();
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

                ApiInterfaceSignup apiInterface = ApiClient.getApiClient().create(ApiInterfaceSignup.class);
                Call<ClassSignup> call = apiInterface.uploadData(name, last_name, username, password, section);

                call.enqueue(new Callback<ClassSignup>() {
                    @Override
                    public void onResponse(Call<ClassSignup> call, Response<ClassSignup> response) {

                        if (!response.isSuccessful()) {

                            Toasty.warning(SignupPageActivity.this, R.string.warning_toast, Toast.LENGTH_SHORT, true).show();

//                            Toast.makeText(SignupPageActivity.this, "the error code is : "+response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ClassSignup modelClass = response.body();

//                        Toast.makeText(SignupPageActivity.this, "server response is success" + modelClass.getResponse(), Toast.LENGTH_SHORT).show();
                        Toasty.success(SignupPageActivity.this, R.string.success_toast, Toast.LENGTH_SHORT, true).show();


                        Intent intent = new Intent(SignupPageActivity.this, LoginPageActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ClassSignup> call, Throwable t) {

                        Toasty.error(SignupPageActivity.this, R.string.error_toast, Toast.LENGTH_SHORT, true).show();

//                        Toast.makeText(SignupPageActivity.this, "server field "+ t, Toast.LENGTH_SHORT).show();
//                        Log.i("TAGS", "onFailure: " + t);

                    }
                });


            }
        });

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.section, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void find() {

        etName = findViewById(R.id.activity_Signup_page_EditText_name);
        etLastName = findViewById(R.id.activity_Signup_page_EditText_lastname);
        etUsername = findViewById(R.id.activity_Signup_page_EditText_username);
        etPassword = findViewById(R.id.activity_Signup_page_EditText_password);
        tv_section = findViewById(R.id.activity_Signup_page_TextView_section);
        spinner = findViewById(R.id.spinner);
        progressBar = findViewById(R.id.spin_kit);
        tv_load = findViewById(R.id.text_view_load);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //these Methodes related to Spinner items

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        String text = adapterView.getItemAtPosition(i).toString();
        tv_section.setText(text);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}

