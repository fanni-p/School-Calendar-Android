package com.finalproject.schoolcalendar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.finalproject.schoolcalendar.R;
import com.finalproject.schoolcalendar.data.DataPersister;
import com.finalproject.schoolcalendar.data.HttpResponseHelper;
import com.finalproject.schoolcalendar.helpers.SessionManager;
import com.finalproject.schoolcalendar.helpers.Sha1Generator;
import com.finalproject.schoolcalendar.models.UserModel;
import com.google.gson.Gson;

public class LoginActivity extends Activity {

    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 30;
    private static final int ACCESSTOKEN_LENGTH = 50;

    private Gson mGson;
    private String mAuthCode;
    private UserModel mUserModel;
    private Handler mHandler;
    private HandlerThread mHandledThread;
    private SessionManager mSessionManager;
    private boolean mValidationSuccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupButtons();

        this.mGson = new Gson();
        this.mValidationSuccess = false;
        this.mAuthCode = null;
        this.mUserModel = null;
        this.mSessionManager = new SessionManager(getApplicationContext());
        this.mHandledThread = new HandlerThread("UserServiceThread");
        this.mHandledThread.start();

        Looper looper = this.mHandledThread.getLooper();
        if (looper != null) {
            this.mHandler = new Handler(looper);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mHandledThread.quit();
        this.mHandledThread = null;
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
        if (userModel != null) {
            this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    DataPersister.Register(userModel);
                    HttpResponseHelper response = DataPersister.Login(userModel);
                    handleLoginResponse(response);
                }
            });
        }
    }

    private void handleLoginButton() {
        final UserModel userModel = this.createUserModel();
        if (userModel != null) {
            this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    HttpResponseHelper response = DataPersister.Login(userModel);
                    handleLoginResponse(response);
                }
            });
        }
    }

    private void handleLoginResponse(HttpResponseHelper response) {
        if (response.isStatusOk()) {
            UserModel resultUserModel = this.mGson.fromJson(response.getMessage(), UserModel.class);
            if (resultUserModel != null && resultUserModel.getAccessToken().length() == ACCESSTOKEN_LENGTH) {
                this.mSessionManager.createLoginSession(resultUserModel.getUsername(), resultUserModel.getAccessToken());
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                this.startActivity(intent);
                this.finish();
            }
        } else {
            Toast.makeText(this, "Login failed. Please try again!", Toast.LENGTH_LONG).show();
        }
    }

    private UserModel createUserModel() {
        EditText usernameTextField = (EditText) this.findViewById(R.id.login_username);
        EditText passwordTextField = (EditText) this.findViewById(R.id.login_password);

        String username = usernameTextField.getText().toString();
        String password = passwordTextField.getText().toString();

        if (username.trim().length() > 0 && password.trim().length() > 0) {
            this.mValidationSuccess = this.ValidateUsernameAndPassword(username, password);
            if (this.mValidationSuccess) {
                this.mAuthCode = Sha1Generator.getSha1(username, password);
                this.mUserModel = new UserModel(username, this.mAuthCode);
            } else {
                Toast.makeText(this,
                        "Username and password must be between 5 and 30 characters", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_LONG).show();
        }

        return this.mUserModel;
    }

    private boolean ValidateUsernameAndPassword(String username, String password) {
        if (username.length() < MIN_LENGTH || MAX_LENGTH < username.length()) {
            return false;
        } else if (password.length() < MIN_LENGTH || MAX_LENGTH < password.length()) {
            return false;
        } else {
            return true;
        }
    }
}