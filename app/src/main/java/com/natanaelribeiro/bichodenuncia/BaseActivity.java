package com.natanaelribeiro.bichodenuncia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.natanaelribeiro.bichodenuncia.Custom.CircularImageView;
import com.natanaelribeiro.bichodenuncia.Custom.ProfilePictureView;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by natanaelribeiro on 21/08/16.
 */
public class BaseActivity extends AppCompatActivity {
    protected DrawerLayout drawerLayout;
    protected GoogleApiClient mGoogleApiClient;
    public static CallbackManager mCallbackManager;
    NavigationView navigationView;
    protected final int RC_SIGN_IN = 1;

    protected void setupToolbar(boolean bCarregaBotaoVoltar) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setLogo(R.drawable.ic_logo);

        if(bCarregaBotaoVoltar) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//          getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);

            if (toolbar != null) {
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }

    protected void setupFacebookSigIn() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        setNavViewValues(navigationView);
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getBaseContext(), "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        String sErro = "";
                        if (exception.getMessage().contains("CONNECTION_FAILURE"))
                            sErro = "Sem conexão com a internet";
                        else
                            sErro = exception.getMessage();
                        Toast.makeText(getBaseContext(), sErro, Toast.LENGTH_LONG).show();
                    }
                });
    }

    protected void setupGoogleSignIn() {
        //Google SignIn configuration
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    protected void setFullscreenActivity() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void setupNavigationDrawer(){
        //Drawer Layout
        final ActionBar actionBar = getSupportActionBar();
        //Ícone do menu do NavDrawer
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView != null && drawerLayout != null) {
            //Atualiza header
            setNavViewValues(navigationView);
            //Trata o evento de clique no menu
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {

                    item.setChecked(true);

                    drawerLayout.closeDrawers();

                    onNavDrawerItemSelected(item);
                    return true;
                }
            });
        }
    }

    private void onNavDrawerItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_ultimas_denuncias: {
                Intent intent = new Intent(getBaseContext(), ListaDenunciasActivity.class);
                intent.putExtra("tipo_busca", "ultimas");
                startActivity(intent);
                break;
            }
            case R.id.nav_pesquisar_denuncia: {
                Intent intent = new Intent(getBaseContext(), PesquisarDenunciaActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_nova_denuncia: {
                Intent intent = new Intent(getBaseContext(), CameraActivity.class);
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.nav_logoff: {
                if(isLoggedInFacebook()){
                    logoutFacebook();
                    setNavViewValues(navigationView);
                } else if(isLoggedInGoogle()) {
                    logoutGoogle();
                    setNavViewValues(navigationView);
                }
                break;
            }
        }
    }

    void setNavViewValues(NavigationView navView) {
        View headerView = navView.getHeaderView(0);
        MenuItem nav_logoff = navView.getMenu().findItem(R.id.nav_logoff);
        LinearLayout area_logado = (LinearLayout) headerView.findViewById(R.id.area_logado);
        LinearLayout area_deslogado = (LinearLayout) headerView.findViewById(R.id.area_deslogado);

        if(isLoggedInFacebook()) {
            nav_logoff.setVisible(true);
            area_logado.setVisibility(View.VISIBLE);
            area_deslogado.setVisibility(View.GONE);

            Profile profile = Profile.getCurrentProfile();
            ProfilePictureView img_usuario = (ProfilePictureView) headerView.findViewById(R.id.img_usuario_facebook);
            img_usuario.setVisibility(View.VISIBLE);
            img_usuario.setProfileId(profile.getId());
            try {
                img_usuario.getRoundedBitmap(getFacebookProfilePicture(profile.getProfilePictureUri(200, 200)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            TextView txt_nome_usuario = (TextView) headerView.findViewById(R.id.txt_nome_usuario);
            txt_nome_usuario.setText(profile.getFirstName() + " " + profile.getLastName());
        } else if(isLoggedInGoogle()) {
            nav_logoff.setVisible(true);
            logarGoogle();
        } else {
            nav_logoff.setVisible(false);
            area_logado.setVisibility(View.GONE);
            area_deslogado.setVisibility(View.VISIBLE);
            //Login Facebook
            ImageButton btnFacebook = (ImageButton) headerView.findViewById(R.id.btn_logar_facebook);
            if (btnFacebook != null) {
                btnFacebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logarFacebook();
                    }
                });
            }

            //Login Google
            ImageButton btnGoogle = (ImageButton) headerView.findViewById(R.id.btn_logar_google);
            if (btnGoogle != null) {
                btnGoogle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logarGoogle();
                    }
                });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(drawerLayout != null){
                    openDrawer();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    protected void openDrawer() {
        if(drawerLayout != null) { drawerLayout.openDrawer(GravityCompat.START); }
    }

    protected void closeDrawer() {
        if(drawerLayout != null) { drawerLayout.closeDrawer(GravityCompat.START); }
    }

    public Bitmap getFacebookProfilePicture(Uri imageUri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

        return bitmap;
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

    public void logarFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    public void logarGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void logoutGoogle() {
        try {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            prefs.edit().putBoolean("isloggedInGoogle", false).commit();
                        }
                    });
        }
        catch(Exception e) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            prefs.edit().putBoolean("isloggedInGoogle", false).commit();
        }
    }

    public void logoutFacebook() {
        LoginManager.getInstance().logOut();
    }

    protected void onBaseActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if(navigationView != null) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if(result.isSuccess()) {
                    View headerView = navigationView.getHeaderView(0);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    prefs.edit().putBoolean("isloggedInGoogle", true).commit();

                    LinearLayout area_logado = (LinearLayout) headerView.findViewById(R.id.area_logado);
                    LinearLayout area_deslogado = (LinearLayout) headerView.findViewById(R.id.area_deslogado);
                    area_logado.setVisibility(View.VISIBLE);
                    area_deslogado.setVisibility(View.GONE);

                    GoogleSignInAccount acct = result.getSignInAccount();
                    TextView txt_nome_usuario = (TextView) headerView.findViewById(R.id.txt_nome_usuario);
                    txt_nome_usuario.setText(acct.getDisplayName());
                    CircularImageView img_usuario_google = (CircularImageView) headerView.findViewById(R.id.img_usuario_google);
                    img_usuario_google.setVisibility(View.VISIBLE);
                    if (acct.getPhotoUrl() == null)
                        img_usuario_google.setImageResource(R.drawable.default_user);
                    else
                        img_usuario_google.setImageURI(acct.getPhotoUrl());
                }
                else {
                    logoutGoogle();
                }
            }
        }
    }


}
