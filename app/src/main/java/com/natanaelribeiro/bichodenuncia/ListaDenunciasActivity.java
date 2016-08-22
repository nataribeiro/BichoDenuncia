package com.natanaelribeiro.bichodenuncia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListaDenunciasActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_denuncias);

        setFullscreenActivity();
        setupToolbar(true);
    }
}
