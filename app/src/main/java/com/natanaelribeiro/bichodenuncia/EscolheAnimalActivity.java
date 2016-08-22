package com.natanaelribeiro.bichodenuncia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.natanaelribeiro.bichodenuncia.AppCode.Constantes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EscolheAnimalActivity extends BaseActivity {

    private String categoria;
    private Constantes.eAnimal animal;

    @BindView(R.id.imagebutton1) public ImageButton imagebutton1;
    @BindView(R.id.imagebutton2) public ImageButton imagebutton2;
    @BindView(R.id.imagebutton3) public ImageButton imagebutton3;
    @BindView(R.id.imagebutton4) public ImageButton imagebutton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolhe_animal);

        setFullscreenActivity();
        setupToolbar(true);

        ButterKnife.bind(this);

        categoria = getIntent().getStringExtra("categoria");

        if(categoria.equals(Constantes.ANIMAIS_DOMESTICOS)){
            imagebutton1.setImageResource(R.drawable.gato);
            imagebutton1.setContentDescription(getString(R.string.texto_gato));

            imagebutton2.setImageResource(R.drawable.cachorro);
            imagebutton2.setContentDescription(getString(R.string.texto_cachorro));

            imagebutton3.setImageResource(R.drawable.cavalo);
            imagebutton3.setContentDescription(getString(R.string.texto_cavalo));

            imagebutton4.setImageResource(R.drawable.roedor);
            imagebutton4.setContentDescription(getString(R.string.texto_roedor));
        }
        else if(categoria.equals(Constantes.ANIMAIS_SELVAGENS)){
            imagebutton1.setImageResource(R.drawable.primata);
            imagebutton1.setContentDescription(getString(R.string.texto_primata));

            imagebutton2.setImageResource(R.drawable.ave);
            imagebutton2.setContentDescription(getString(R.string.texto_ave));

            imagebutton3.setImageResource(R.drawable.roedor2);
            imagebutton3.setContentDescription(getString(R.string.texto_roedor));

            imagebutton4.setImageResource(R.drawable.marinho);
            imagebutton4.setContentDescription(getString(R.string.texto_marinho));
        }
    }

    @OnClick(R.id.imagebutton1)
    public void onClickImagem1() {
        if(categoria.equals(Constantes.ANIMAIS_DOMESTICOS))
            animal = Constantes.eAnimal.gato;
        else
            animal = Constantes.eAnimal.primata;
        NextActivity(animal);
    }

    @OnClick(R.id.imagebutton2)
    public void onClickImagem2() {
        if(categoria.equals(Constantes.ANIMAIS_DOMESTICOS))
            animal = Constantes.eAnimal.cachorro;
        else
            animal = Constantes.eAnimal.ave;
        NextActivity(animal);
    }

    @OnClick(R.id.imagebutton3)
    public void onClickImagem3() {
        if(categoria.equals(Constantes.ANIMAIS_DOMESTICOS))
            animal = Constantes.eAnimal.cavalo;
        else
            animal = Constantes.eAnimal.rodedor_selvagem;
        NextActivity(animal);
    }

    @OnClick(R.id.imagebutton4)
    public void onClickImagem4() {
        if(categoria.equals(Constantes.ANIMAIS_DOMESTICOS))
            animal = Constantes.eAnimal.roedor_domestico;
        else
            animal = Constantes.eAnimal.marinho;
        NextActivity(animal);
    }

    private void NextActivity(Constantes.eAnimal animal) {
        Intent intent = new Intent(this, DetalhamentoDenunciaActivity.class);
        intent.putExtra("animal", animal.toString());
        startActivity(intent);
    }

}
