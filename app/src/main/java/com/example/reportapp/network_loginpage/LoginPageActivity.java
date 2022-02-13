package com.example.reportapp.network_loginpage;

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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reportapp.network_getreport.MainActivity;
import com.example.reportapp.network_signuppage.ApiClient;
import com.example.reportapp.network_signuppage.SignupPageActivity;
import com.example.reportapp.R;
import com.example.reportapp.util.MySharedPreferences;

import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPageActivity extends AppCompatActivity {

    EditText et_username,et_password;
    MySharedPreferences preferences;
    ProgressBar progressBar;
    TextView tv_load;
    Handler handler;
    Runnable runnable;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_page);

        initToolbar();


        preferences = MySharedPreferences.getInstance(LoginPageActivity.this);

        if(preferences.getSignInStatus()){
            Intent intent = new Intent(LoginPageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        progressBar = findViewById(R.id.spin_kit_login);
        tv_load = findViewById(R.id.text_view_load_login);
        et_username = findViewById(R.id.activity_login_page_EditText_username);
        et_password = findViewById(R.id.activity_login_page_EditText_password);

                TextView textView = findViewById(R.id.text_view_sign_up);
                textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginPageActivity.this, SignupPageActivity.class);
                startActivity(intent);
            }
        });
    }

    public void btn_signin(View view) {

        final String user_name = et_username.getText().toString();
        final String password = et_password.getText().toString();

        if (user_name.isEmpty()){
            et_username.setError(getString(R.string.write_user_name));
            et_username.findFocus();
            return;
        }
        if (password.length() < 6){
            et_password.setError(getString(R.string.write_password));
            et_password.findFocus();
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


        ApiInterfaceLoginPage apiInterface = ApiClient.getApiClient().create(ApiInterfaceLoginPage.class);
        Call<Employee> class_loginPageCall = apiInterface.login(user_name,password);

        class_loginPageCall.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {

                if (!response.isSuccessful())    {
                    Toasty.warning(LoginPageActivity.this, R.string.warning_toast, Toast.LENGTH_SHORT, true).show();
//                    Toast.makeText(LoginPageActivity.this, "the error code is : "+response.code(), Toast.LENGTH_SHORT).show();
//                    return;
                }
               else {

                    Employee employee = response.body();

                    if (employee != null && employee.getSignIn() == null) {
                        preferences.setId(employee.getId());
                        preferences.setName(employee.getName());
                        preferences.setLastName(employee.getLast_Name());
                        preferences.setTeam(employee.getSection());

                        Toasty.success(LoginPageActivity.this, R.string.success_toast, Toast.LENGTH_SHORT, true).show();


                        Intent intentForMainAct = new Intent(LoginPageActivity.this, MainActivity.class);
                        startActivity(intentForMainAct);

                        preferences.setSignInStatus(true);

                        finish();

                    }
                    else {
                        Toasty.info(LoginPageActivity.this, R.string.info_toast, Toast.LENGTH_SHORT, true).show();
//                    Toast.makeText(LoginPageActivity.this, "Username or Password is wrong!", Toast.LENGTH_SHORT).show();
                    }
                    }
                }
            @Override
            public void onFailure(Call<Employee> call, Throwable t) {

                Toasty.error(LoginPageActivity.this, R.string.error_toast, Toast.LENGTH_SHORT, true).show();

//                Toast.makeText(LoginPageActivity.this, "server field "+ t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.i("TAGS", "onFailure: " + t.getMessage());
            }
        });


    }

    private void initToolbar() {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
