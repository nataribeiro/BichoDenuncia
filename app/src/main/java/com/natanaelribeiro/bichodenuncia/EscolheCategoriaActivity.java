package com.natanaelribeiro.bichodenuncia;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.natanaelribeiro.bichodenuncia.AppCode.Constantes;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 631610277 on 06/08/16.
 */
public class EscolheCategoriaActivity extends BaseActivity {

    @BindView(R.id.txtCategoria) public TextView txtCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolhe_categoria);

        setFullscreenActivity();
        setupToolbar(true);

        ButterKnife.bind(this);
        //txtCategoria.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf"));
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
