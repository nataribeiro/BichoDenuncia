package com.example.natanaelribeiro.bichodenuncia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.example.natanaelribeiro.bichodenuncia.AppCode.Constantes;

import butterknife.ButterKnife;

/**
 * Created by 631610277 on 06/08/16.
 */
public class EscolheCategoriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolhe_categoria);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);
    }

    public void onClickAnimaisDomesticos(View view){
        NextActivity(Constantes.ANIMAIS_DOMESTICOS);
    }

    public void onClickAnimaisSelvagens(View view){
        NextActivity(Constantes.ANIMAIS_SELVAGENS);
    }

    private void NextActivity(String categoria){
        Intent intent = new Intent(this, EscolheAnimalActivity.class);
        intent.putExtra("categoria", categoria);
        startActivity(intent);
    }

}
