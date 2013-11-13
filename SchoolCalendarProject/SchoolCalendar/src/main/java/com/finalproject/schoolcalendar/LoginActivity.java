package com.finalproject.schoolcalendar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import com.finalproject.schoolcalendar.data.DataPersister;
import com.finalproject.schoolcalendar.data.HttpResponse;
import com.finalproject.schoolcalendar.helpers.NavigationDrawerHelper;
import com.finalproject.schoolcalendar.models.UserModel;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends Activity {
    private HandlerThread mHandledThread;
    private Handler mHandler;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupButtons();
        this.mGson = new Gson();
        this.mHandledThread = new HandlerThread("UserServiceThread");
        this.mHandledThread.start();

        Looper looper = this.mHandledThread.getLooper();
        if (looper != null) {
            this.mHandler = new Handler(looper);
        }
    }

    @Override
    public void onDestroy() {
        this.mHandledThread.quit();
        this.mHandledThread = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setupButtons() {
        Button loginButton = (Button) this.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLoginButton();
            }
        });

        Button registerButton = (Button) this.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegisterButton();
            }
        });

        CheckBox showPassword = (CheckBox) this.findViewById(R.id.login_showPassword);
        final EditText passwordTextField = (EditText) this.findViewById(R.id.login_password);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    passwordTextField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    passwordTextField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }

    private void handleRegisterButton() {
        final UserModel userModel = this.createUserModel();
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                DataPersister.Register(userModel);
                HttpResponse response = DataPersister.Login(userModel);
                handleLoginResponse(response);
            }
        });
    }

    private void handleLoginButton() {
        final UserModel userModel = this.createUserModel();
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                HttpResponse response = DataPersister.Login(userModel);
                handleLoginResponse(response);
            }
        });
    }

    private UserModel createUserModel(){
        EditText usernameTextField = (EditText) this.findViewById(R.id.login_username);
        EditText passwordTextField = (EditText) this.findViewById(R.id.login_password);

        final String username = usernameTextField.getText().toString();
        final String password = passwordTextField.getText().toString();
        final String authCode = generateSha1(username, password);
        // TODO Validation Username, password, authCode
        UserModel userModel = new UserModel(username, authCode);

        return userModel;
    }

    private void handleLoginResponse (HttpResponse response) {
        if(response.isStatusOk()){
            UserModel resultUserModel = this.mGson.fromJson(response.getMessage(), UserModel.class);
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private void changeFragment(){
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), MainActivity.class);
//        intent.putExtra("IsLoggedIn", true);
//        //TODO Add resultUserModel as Extra
//        startActivity(intent);
    }

    private String generateSha1(String username, String password) {
        String toHash = username + password;
        String hash = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return hash;
    }
}