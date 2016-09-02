package com.natanaelribeiro.bichodenuncia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.natanaelribeiro.bichodenuncia.AppCode.Constantes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EscolheAnimalActivity extends BaseActivity {

    private String filePath;
    private String fileType;
    private String categoria;
    private String animal;

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

        filePath = getIntent().getStringExtra("filePath");
        fileType = getIntent().getStringExtra("fileType");
        categoria = getIntent().getStringExtra("categoria");

        if(categoria.equals(Constantes.ANIMAIS_DOMESTICOS)){
            imagebutton1.setImageResource(R.drawable.gato);
            imagebutton1.setContentDescription(getString(R.string.texto_gato));

            imagebutton2.setImageResource(R.drawable.cachorro);
            imagebutton2.setContentDescription(getString(R.string.texto_cachorro));

            imagebutton3.setImageResource(R.drawable.cavalo);
            imagebutton3.setContentDescription(getString(R.string.texto_cavalo));

            imagebutton4.setImageResource(R.drawable.roedor_domestico);
            imagebutton4.setContentDescription(getString(R.string.texto_roedor));
        }
        else if(categoria.equals(Constantes.ANIMAIS_SELVAGENS)){
            imagebutton1.setImageResource(R.drawable.primata);
            imagebutton1.setContentDescription(getString(R.string.texto_primata));

            imagebutton2.setImageResource(R.drawable.ave);
            imagebutton2.setContentDescription(getString(R.string.texto_ave));

            imagebutton3.setImageResource(R.drawable.roedor_selvagem);
            imagebutton3.setContentDescription(getString(R.string.texto_roedor));

            imagebutton4.setImageResource(R.drawable.marinho);
            imagebutton4.setContentDescription(getString(R.string.texto_marinho));
        }
    }

    @OnClick(R.id.imagebutton1)
    public void onClickImagem1() {
        if(categoria.equals(Constantes.ANIMAIS_DOMESTICOS))
            animal = Constantes.eAnimal.gato.toString();
        else
            animal = Constantes.eAnimal.primata.toString();
        NextActivity(animal);
    }

    @OnClick(R.id.imagebutton2)
    public void onClickImagem2() {
        if(categoria.equals(Constantes.ANIMAIS_DOMESTICOS))
            animal = Constantes.eAnimal.cachorro.toString();
        else
            animal = Constantes.eAnimal.ave.toString();
        NextActivity(animal);
    }

    @OnClick(R.id.imagebutton3)
    public void onClickImagem3() {
        if(categoria.equals(Constantes.ANIMAIS_DOMESTICOS))
            animal = Constantes.eAnimal.cavalo.toString();
        else
            animal = Constantes.eAnimal.rodedor_selvagem.toString();
        NextActivity(animal);
    }

    @OnClick(R.id.imagebutton4)
    public void onClickImagem4() {
        if(categoria.equals(Constantes.ANIMAIS_DOMESTICOS))
            animal = Constantes.eAnimal.roedor_domestico.toString();
        else
            animal = Constantes.eAnimal.marinho.toString();
        NextActivity(animal);
    }

    @OnClick(R.id.btn_outro_animal)
    public void onClickOutroAnimal(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_logo);
        builder.setTitle("Outro Animal");
        builder.setMessage("Informe o tipo de animal que está sendo maltratado");

        final EditText input = new EditText(this);
        input.setHighlightColor(getResources().getColor(R.color.colorPrimary));
        input.setBackgroundColor(getResources().getColor(R.color.gray));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                animal = input.getText().toString();
                NextActivity(animal);
                return;
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void NextActivity(String animal) {
        Intent intent = new Intent(this, DetalhamentoDenunciaActivity.class);
        intent.putExtra("filePath", filePath);
        intent.putExtra("fileType", fileType);
        intent.putExtra("categoria", categoria);
        intent.putExtra("animal", animal);
        startActivity(intent);
    }

}
