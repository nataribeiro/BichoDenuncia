package com.natanaelribeiro.bichodenuncia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DetalhesDenunciaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_denuncia);

        setFullscreenActivity();
        setupToolbar(true);
    }
}
