package com.natanaelribeiro.bichodenuncia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.natanaelribeiro.bichodenuncia.Custom.CircularImageView;
import com.natanaelribeiro.bichodenuncia.Custom.ProfilePictureView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;


import java.io.IOException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IdentificacaoActivity extends AppCompatActivity {

    @BindView(R.id.btn_logar_facebook) public ImageButton btn_logar_facebook;
    @BindView(R.id.btn_logar_google) public ImageButton btn_logar_google;
    @BindView(R.id.sign_in_button) public SignInButton sign_in_button;
    @BindView(R.id.edit_email) public EditText edit_email;
    @BindView(R.id.edit_telefone) public EditText edit_telefone;
    @BindView(R.id.area_logado) public LinearLayout area_logado;
    @BindView(R.id.area_deslogado) public LinearLayout area_deslogado;
    @BindView(R.id.img_usuario) public ProfilePictureView img_usuario;
    @BindView(R.id.img_usuario_google) public CircularImageView img_usuario_google;
    @BindView(R.id.text_nome_usuario) public TextView text_nome_usuario;

    public static CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private final int RC_SIGN_IN = 1;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Facebook configuration
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        handleFacebookSignIn();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(IdentificacaoActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        String sErro = "";
                        if (exception.getMessage().contains("CONNECTION_FAILURE"))
                            sErro = "Sem conexão com a internet";
                        else
                            sErro = exception.getMessage();
                        Toast.makeText(IdentificacaoActivity.this, sErro, Toast.LENGTH_LONG).show();
                    }
                });

        setContentView(R.layout.activity_identificacao);
        ButterKnife.bind(this);

        //Google SignIn configuration
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setLogo(R.drawable.ic_logo);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        checkUserLoggedIn();

    }

    private void checkUserLoggedIn(){
        if(isLoggedInFacebook()){
            handleFacebookSignIn();
        } else if(isLoggedInGoogle()) {
            onClickLogarGoogle();
        }
    }

    public boolean isLoggedInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public boolean isLoggedInGoogle(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isloggedIn = prefs.getBoolean("isloggedInGoogle", false);
        return isloggedIn;
    }

    @OnClick(R.id.btn_logar_facebook)
    public void onClickLogarFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    @OnClick(R.id.btn_logar_google)
    public void onClickLogarGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //sign_in_button.callOnClick();
    }

    @OnClick(R.id.btn_enviar_denuncia)
    public void onClickEnviarDenuncia() {

        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("ENVIANDO...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        try {
                            Thread.sleep(3000);
                            Intent intent = new Intent(IdentificacaoActivity.this, DenunciaEnviadaActivity.class);
                            startActivity(intent);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 1000);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            if (mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
                return;
            }
        }
    }

    public Bitmap getFacebookProfilePicture(Uri imageUri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

        return bitmap;
    }

    private void handleFacebookSignIn(){
        area_deslogado.setVisibility(View.GONE);
        area_logado.setVisibility(View.VISIBLE);

        Profile profile = Profile.getCurrentProfile();
        img_usuario.setVisibility(View.VISIBLE);
        img_usuario.setProfileId(profile.getId());
        try {
            img_usuario.getRoundedBitmap(getFacebookProfilePicture(profile.getProfilePictureUri(200, 200)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        text_nome_usuario.setText(profile.getFirstName() + " " + profile.getLastName());
        Log.i("FB Nome", profile.getFirstName());
        Log.i("FB ID", profile.getId());
        Log.i("FB Imagem", profile.getProfilePictureUri(200, 200).toString());
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putBoolean("isloggedInGoogle", true).commit();

            area_deslogado.setVisibility(View.GONE);
            area_logado.setVisibility(View.VISIBLE);

            GoogleSignInAccount acct = result.getSignInAccount();
            text_nome_usuario.setText(acct.getDisplayName());
            edit_email.setText(acct.getEmail());
            img_usuario_google.setVisibility(View.VISIBLE);
            if(acct.getPhotoUrl() == null)
                img_usuario_google.setImageResource(R.drawable.default_user);
            else
                img_usuario_google.setImageURI(acct.getPhotoUrl());

            Log.d("Google SignIn", "displayname: " + acct.getDisplayName());
            Log.d("Google SignIn", "displayemail: " + acct.getEmail());
            Log.d("Google SignIn", "displayimage: " + acct.getPhotoUrl());
        } else {
            Log.d("Google SignIn", "sign out");
        }
    }

}
