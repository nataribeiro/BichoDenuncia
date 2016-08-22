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

public class DenunciaEnviadaActivity extends BaseActivity {

    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_enviada);

        setFullscreenActivity();
        setupToolbar(true);

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
