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


import com.natanaelribeiro.bichodenuncia.AppCode.Constantes;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String filePath;

    @BindView(R.id.nav_view) public NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.ic_logo);

        //TODO Tirar após finalizar login com Google
        //AccessToken.setCurrentAccessToken(null);

        //nav_view.setNavigationItemSelectedListener(this);
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
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_ultimas_denuncias){

        } else if(id == R.id.nav_pesquisar_denuncia) {

        } else if(id == R.id.nav_cadastrar_entidade){

        } else if(id == R.id.nav_nova_denuncia){
            Intent intent = new Intent(this, CameraActivity.class);
            startActivityForResult(intent, 1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
