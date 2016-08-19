package com.example.natanaelribeiro.bichodenuncia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.example.natanaelribeiro.bichodenuncia.Custom.ProfilePictureView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;


import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IdentificacaoActivity extends AppCompatActivity {

    @BindView(R.id.btn_logar_facebook) public ImageButton btn_logar_facebook;
    @BindView(R.id.btn_logar_google) public ImageButton btn_logar_google;
    @BindView(R.id.edit_email) public EditText edit_email;
    @BindView(R.id.edit_telefone) public EditText edit_telefone;
    @BindView(R.id.area_logado) public LinearLayout area_logado;
    @BindView(R.id.area_deslogado) public LinearLayout area_deslogado;
    @BindView(R.id.img_usuario) public ProfilePictureView img_usuario;
    @BindView(R.id.text_nome_usuario) public TextView text_nome_usuario;

    public static CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        CarregaInfoCliente();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(IdentificacaoActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(IdentificacaoActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        setContentView(R.layout.activity_identificacao);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);

        ButterKnife.bind(this);

        if(isLoggedIn()){
            CarregaInfoCliente();
        } else{

        }
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public Bitmap getFacebookProfilePicture(Uri imageUri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

        return bitmap;
    }

    private void CarregaInfoCliente(){
        area_deslogado.setVisibility(View.GONE);
        area_logado.setVisibility(View.VISIBLE);

        Profile profile = Profile.getCurrentProfile();
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

    @OnClick(R.id.btn_logar_facebook)
    public void onClickLogarFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }
}
