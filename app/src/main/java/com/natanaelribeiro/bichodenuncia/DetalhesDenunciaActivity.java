package com.natanaelribeiro.bichodenuncia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.natanaelribeiro.bichodenuncia.AppCode.Constantes;
import com.natanaelribeiro.bichodenuncia.AppCode.CoreApplication;
import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Realm.dbDenuncia;
import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Realm.dbHashtag;
import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Realm.dbMidia;
import com.natanaelribeiro.bichodenuncia.Custom.RoundImage;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class DetalhesDenunciaActivity extends BaseActivity {

    @BindView(R.id.img_denuncia) public ImageView img_denuncia;
    @BindView(R.id.txt_titulo) public TextView txt_titulo;
    @BindView(R.id.txt_data) public TextView txt_data;
    @BindView(R.id.txt_descricao_denuncia) public TextView txt_descricao_denuncia;
    @BindView(R.id.txt_hashtags_denuncia) public TextView txt_hashtags_denuncia;
    @BindView(R.id.txt_orgaos_denuncia) public TextView txt_orgaos_denuncia;
    @BindView(R.id.img_status_denuncia) public ImageView img_status_denuncia;
    @BindView(R.id.txt_status_denuncia) public TextView txt_status_denuncia;
    @BindView(R.id.txt_retorno_denuncia) public TextView txt_retorno_denuncia;
    RoundImage roundedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_denuncia);

        setFullscreenActivity();
        setupToolbar(true);

        ButterKnife.bind(this);

        int id = getIntent().getIntExtra("id_denuncia", -1);
        if(id >= 0){
            carregaInfoDenuncia(id);
        }


    }

    private void carregaInfoDenuncia(int id){

        dbDenuncia denuncia = ((CoreApplication)getApplication()).
                realm.where(dbDenuncia.class)
                .equalTo("id", id)
                .findFirst();

        dbMidia midia = ((CoreApplication)getApplication()).
                realm.where(dbMidia.class)
                .equalTo("id_denuncia", id)
                .findFirst();

        RealmResults<dbHashtag> hashtags = ((CoreApplication)getApplication()).
                realm.where(dbHashtag.class)
                .equalTo("id_denuncia", id)
                .findAll();



        if(denuncia != null) {

            if(midia != null) {
                if(midia.tipo_midia == "I") {
                    byte[] decodedString = Base64.decode(midia.arquivo, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    roundedImage = new RoundImage(decodedByte);
                    img_denuncia.setImageDrawable(roundedImage);
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String strDate = sdf.format(denuncia.data);

            txt_titulo.setText(denuncia.titulo);
            txt_data.setText(strDate);
            txt_descricao_denuncia.setText(denuncia.descricao);

            if(denuncia.situacao.equals("P")) {
                img_status_denuncia.setImageResource(R.drawable.amarelo);
                txt_status_denuncia.setTextColor(getResources().getColor(R.color.yellow));
                txt_status_denuncia.setText("Pendente");
            }
            else if(denuncia.situacao.equals("R")) {
                img_status_denuncia.setImageResource(R.drawable.resolvido);
                txt_status_denuncia.setTextColor(getResources().getColor(R.color.green));
                txt_status_denuncia.setText("Resolvido");
            }
            else {
                img_status_denuncia.setImageResource(R.drawable.nao_resolvido);
                txt_status_denuncia.setTextColor(getResources().getColor(R.color.red));
                txt_status_denuncia.setText("Não resolvido");
            }

            String sHashtags = "";
            for(dbHashtag hash : hashtags){
                sHashtags += hash.hashtag + " ";
            }
            txt_hashtags_denuncia.setText(sHashtags.trim());

            txt_orgaos_denuncia.setText("EPTC e Brigada Militar");
            txt_retorno_denuncia.setText("");
        }
    }

    @OnClick(R.id.btn_ir_nova_denuncia)
    public void onClickNovaDenuncia() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, Constantes.REQUEST_CAMERA);
    }

    @OnClick(R.id.btn_ir_pesquisar_denuncia)
    public void onClickPesquisarDenuncia() {
        Intent intent = new Intent(this, PesquisarDenunciaActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constantes.REQUEST_CAMERA){
            if(resultCode == RESULT_OK){
                String fileType;
                String filePath;

                int type = data.getIntExtra("fileType", 0);
                if(type == Constantes.MEDIA_TYPE_IMAGE)
                    fileType = "I";
                else
                    fileType = "V";
                filePath = data.getStringExtra("filePath");
                Intent intent = new Intent(this, EscolheCategoriaActivity.class);
                intent.putExtra("filePath", filePath);
                intent.putExtra("fileType", fileType);
                startActivity(intent);
            }
        }
    }
}
