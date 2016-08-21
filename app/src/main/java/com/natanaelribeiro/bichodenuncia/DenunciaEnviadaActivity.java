package com.natanaelribeiro.bichodenuncia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.natanaelribeiro.bichodenuncia.AppCode.Constantes;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DenunciaEnviadaActivity extends AppCompatActivity {

    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_enviada);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setLogo(R.drawable.ic_logo);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_ir_pesquisa)
    public void onClickPesquisarDenuncia(){
        Intent intent = new Intent(this, PesquisarDenunciaActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_ir_nova_denuncia)
    public void onClickNovaDenuncia(){
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
}
