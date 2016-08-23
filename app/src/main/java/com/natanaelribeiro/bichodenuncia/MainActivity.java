package com.natanaelribeiro.bichodenuncia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;


import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.natanaelribeiro.bichodenuncia.AppCode.Constantes;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private String filePath;

    @BindView(R.id.nav_view) public NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFacebookSigIn();
        setContentView(R.layout.activity_base);
        setupGoogleSignIn();

        setFullscreenActivity();
        setupToolbar(false);
        getSupportActionBar().setTitle(R.string.app_name);
        setupNavigationDrawer();
    }

    public void onClickNovaDenuncia(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, Constantes.REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constantes.REQUEST_CAMERA){
            if(resultCode == RESULT_OK){
                filePath = data.getStringExtra("filePath");
                Intent intent = new Intent(this, EscolheCategoriaActivity.class);
                startActivity(intent);
            }
        } else if(requestCode == RC_SIGN_IN){
            onBaseActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            if (mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
                return;
            }
        }
    }
}
