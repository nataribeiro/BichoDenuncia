package com.natanaelribeiro.bichodenuncia;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.natanaelribeiro.bichodenuncia.AppCode.CoreApplication;
import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Realm.dbDenuncia;
import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Service.Denuncia;
import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Service.Hashtag;
import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Service.Midia;
import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Service.ResultadoOperacao;
import com.natanaelribeiro.bichodenuncia.AppCode.IRetrofit;
import com.natanaelribeiro.bichodenuncia.AppCode.ServiceGenerator;
import com.natanaelribeiro.bichodenuncia.Custom.ProfilePictureView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.natanaelribeiro.bichodenuncia.Custom.RoundImage;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdentificacaoActivity extends BaseActivity {

    @BindView(R.id.btn_logar_facebook) public ImageButton btn_logar_facebook;
    @BindView(R.id.btn_logar_google) public ImageButton btn_logar_google;
    @BindView(R.id.edit_email) public EditText edit_email;
    @BindView(R.id.edit_telefone) public EditText edit_telefone;
    @BindView(R.id.area_logado) public LinearLayout area_logado;
    @BindView(R.id.area_deslogado) public LinearLayout area_deslogado;
    @BindView(R.id.img_usuario) public ProfilePictureView img_usuario;
    @BindView(R.id.img_usuario_google) public ImageView img_usuario_google;
    @BindView(R.id.text_nome_usuario) public TextView text_nome_usuario;

    private Denuncia denuncia;
    private dbDenuncia db_denuncia;

    public IRetrofit service;
    public static CallbackManager mCallbackManager;
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

        setupGoogleSignIn();
        setFullscreenActivity();
        setupToolbar(true);

        ButterKnife.bind(this);

        checkUserLoggedIn();
        carregaExtras();
    }

    private void carregaExtras(){
        String sFilePath = getIntent().getStringExtra("filePath");

        Bitmap imagem = BitmapFactory.decodeFile(sFilePath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] imgByteArray = stream.toByteArray();
        String sImagemArray = Base64.encodeToString(imgByteArray, Base64.DEFAULT);

        denuncia = new Denuncia();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = sdf.format(new Date());

        denuncia.data = strDate;
        Midia midia = new Midia();
        midia.arquivo = sImagemArray;
        midia.tipo_midia = getIntent().getStringExtra("fileType");
        midia.sequencia = 1;
        denuncia.categoria_animal = getIntent().getStringExtra("categoria");
        denuncia.tipo_animal = getIntent().getStringExtra("animal");
        denuncia.descricao = getIntent().getStringExtra("descricao");
        denuncia.situacao = "pendente";
        denuncia.hashtag = getHashtags(getIntent().getStringExtra("hashtags"));
        denuncia.endereco = getIntent().getStringExtra("endereco");
        denuncia.endereco_latitude = getIntent().getDoubleExtra("loc_latitude", 0);
        denuncia.endereco_longitude = getIntent().getDoubleExtra("loc_longitude", 0);
        denuncia.midia.add(midia);

        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        denuncia.id_dispositivo = tm.getDeviceId();
    }

    private List<Hashtag> getHashtags(String hashtags){
        List<Hashtag> lHashtags = new ArrayList<Hashtag>();

        String[] hashs = hashtags.split(" ");
        for (String h: hashs) {
            Hashtag hash = new Hashtag();
            hash.hashtag = "#" + h.replace("#", "");
            lHashtags.add(hash);
        }

        return lHashtags;
    }

    private void checkUserLoggedIn(){
        if(isLoggedInFacebook()){
            handleFacebookSignIn();
        } else if(isLoggedInGoogle()) {
            onClickLogarGoogle();
        }
    }

    @OnClick(R.id.btn_logar_facebook)
    public void onClickLogarFacebook() {
        logarFacebook();
    }

    @OnClick(R.id.btn_logar_google)
    public void onClickLogarGoogle() {
        logarGoogle();
    }

    @OnClick(R.id.btn_enviar_denuncia)
    public void onClickEnviarDenuncia() {

        if(!edit_email.getText().toString().equals("") && !edit_telefone.getText().toString().equals("")) {

            denuncia.denunciante_email = edit_email.getText().toString();
            denuncia.denunciante_telefone = edit_telefone.getText().toString();

            final ProgressDialog progressDialog = new ProgressDialog(this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("ENVIANDO...");
            progressDialog.show();


            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            try {


                                db_denuncia = new dbDenuncia();
                                db_denuncia.categoria_animal = denuncia.categoria_animal;
                                db_denuncia.data = denuncia.data;
                                db_denuncia.denunciante_email = denuncia.denunciante_email;
                                db_denuncia.denunciante_telefone = denuncia.denunciante_telefone;
                                db_denuncia.descricao = denuncia.descricao;
                                db_denuncia.endereco = denuncia.endereco;
                                db_denuncia.endereco_latitude = denuncia.endereco_latitude;
                                db_denuncia.endereco_longitude = denuncia.endereco_longitude;
                                db_denuncia.id_dispositivo = denuncia.id_dispositivo;
                                db_denuncia.situacao = denuncia.situacao;
                                db_denuncia.tipo_animal = denuncia.tipo_animal;
                                db_denuncia.titulo = denuncia.titulo;
                                //db_denuncia.id_server = response.body().iCodigo;

                                ((CoreApplication)getApplication()).realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm bgRealm) {
                                        bgRealm.copyToRealmOrUpdate(db_denuncia);
                                    }
                                }, new Realm.Transaction.OnSuccess() {
                                    public void onSuccess() {
                                        finish();
                                        NextActivity();
                                    }
                                }, new Realm.Transaction.OnError() {
                                    public void onError(Throwable error) {
                                        Log.e("SalvarDenunciaRealm", error.getMessage());
                                    }
                                });

                                //EnviaDenuncia(denuncia);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 1000);
        } else {
            Toast.makeText(this, "Informe e-mail e telefone para contato", Toast.LENGTH_LONG).show();
        }

    }

    private void EnviaDenuncia(final Denuncia denuncia){

        service = ServiceGenerator.createService(IRetrofit.class);

        Call<ResultadoOperacao> call = service.enviaDenuncia(denuncia);
        call.enqueue(new Callback<ResultadoOperacao>() {
            @Override
            public void onResponse(Call<ResultadoOperacao> call, Response<ResultadoOperacao> response) {
                if(response.body() != null) {
                    if(response.body().bSucesso) {

//                        db_denuncia = new dbDenuncia();
//                        db_denuncia.categoria_animal = denuncia.categoria_animal;
//                        db_denuncia.data = denuncia.data;
//                        db_denuncia.denunciante_email = denuncia.denunciante_email;
//                        db_denuncia.denunciante_telefone = denuncia.denunciante_telefone;
//                        db_denuncia.descricao = denuncia.descricao;
//                        db_denuncia.endereco = denuncia.endereco;
//                        db_denuncia.endereco_latitude = denuncia.endereco_latitude;
//                        db_denuncia.endereco_longitude = denuncia.endereco_longitude;
//                        db_denuncia.id_dispositivo = denuncia.id_dispositivo;
//                        db_denuncia.situacao = denuncia.situacao;
//                        db_denuncia.tipo_animal = denuncia.tipo_animal;
//                        db_denuncia.titulo = denuncia.titulo;
//                        db_denuncia.id_server = response.body().iCodigo;
//
//                        ((CoreApplication)getApplication()).realm.executeTransactionAsync(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm bgRealm) {
//                                bgRealm.copyToRealmOrUpdate(db_denuncia);
//                            }
//                        }, new Realm.Transaction.OnSuccess() {
//                            public void onSuccess() {
//                                finish();
//                                NextActivity();
//                            }
//                        }, new Realm.Transaction.OnError() {
//                            public void onError(Throwable error) {
//                                Log.e("SalvarDenunciaRealm", error.getMessage());
//                            }
//                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultadoOperacao> call, Throwable t) {
                Log.e("EnviarDenuncia", t.getMessage());
            }
        });
    }

    private void NextActivity(){
        Intent intent = new Intent(IdentificacaoActivity.this, DenunciaEnviadaActivity.class);
        startActivity(intent);
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
//            img_usuario_google.setVisibility(View.VISIBLE);
//            if(acct.getPhotoUrl() == null)
//                img_usuario_google.setImageResource(R.drawable.default_user);
//            else
//                img_usuario_google.setImageURI(acct.getPhotoUrl());

            Bitmap bm;
            if (acct.getPhotoUrl() == null)
                bm = BitmapFactory.decodeResource(getResources(),R.drawable.default_user);
            else
                try {
                    bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(),acct.getPhotoUrl());
                } catch (IOException e) {
                    bm = BitmapFactory.decodeResource(getResources(),R.drawable.default_user);
                }

            roundedImage = new RoundImage(bm);
            img_usuario_google.setImageDrawable(roundedImage);
            img_usuario_google.setVisibility(View.VISIBLE);

            Log.d("Google SignIn", "displayname: " + acct.getDisplayName());
            Log.d("Google SignIn", "displayemail: " + acct.getEmail());
            Log.d("Google SignIn", "displayimage: " + acct.getPhotoUrl());
        } else {
            Log.d("Google SignIn", "sign out");
        }
    }

}
