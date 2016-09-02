package com.natanaelribeiro.bichodenuncia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.natanaelribeiro.bichodenuncia.AppCode.Constantes;
import com.natanaelribeiro.bichodenuncia.AppCode.CoreApplication;
import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Realm.dbDenuncia;
import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Realm.dbHashtag;
import com.natanaelribeiro.bichodenuncia.Custom.ListaDenunciasAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Case;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class ListaDenunciasActivity extends BaseActivity {

    @BindView(R.id.txt_tipo_busca) public TextView txt_tipo_busca;
    @BindView(R.id.txt_conteudo_busca) public TextView txt_conteudo_busca;
    @BindView(R.id.list_denuncias_busca) public ListView listTasks;
    public static RealmResults<dbDenuncia> results;
    public static RealmResults<dbHashtag> hashtagsResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_denuncias);

        setFullscreenActivity();
        setupToolbar(true);

        ButterKnife.bind(this);

        String tipo_busca = getIntent().getStringExtra("tipo_busca");
        carregaInfoBusca(tipo_busca);
    }

    private void carregaInfoBusca(String tipo_busca){
        switch (tipo_busca){
            case "ultimas": {
                txt_tipo_busca.setText("ULTIMAS DENÚNCIAS");
                txt_conteudo_busca.setText("Lista das últimas denúncias realizadas");
                carregaUltimasDenuncias();
                break;
            }
            case "data":{
                String data_inicio = getIntent().getStringExtra("data_inicio");
                String data_fim = getIntent().getStringExtra("data_fim");
                txt_tipo_busca.setText("POR DATA");
                txt_conteudo_busca.setText("de " + data_inicio + " à " + data_fim);
                carregaDenunciasPorData(data_inicio, data_fim);
                break;
            }
            case "hashtag":{
                String filtro = getIntent().getStringExtra("filtro");
                txt_tipo_busca.setText("POR HASHTAG");
                txt_conteudo_busca.setText(filtro);
                carregaDenunciasPorHashtag(filtro);
                break;
            }
        }
    }

    private RealmChangeListener callback = new RealmChangeListener() {
        public void onChange(Object element) {
            results = (RealmResults<dbDenuncia>) element;

            populaListView();
        }
    };

    private void populaListView(){
        listTasks.setAdapter(new ListaDenunciasAdapter(ListaDenunciasActivity.this, results));
        listTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), DetalhesDenunciaActivity.class);
                intent.putExtra("id_denuncia", results.get(i).id);
                startActivity(intent);
            }
        });
    }

    private void carregaUltimasDenuncias() {
        results = ((CoreApplication)getApplication()).
                realm.where(dbDenuncia.class)
                .findAllSorted("id", Sort.DESCENDING);
        //results.addChangeListener(callback);

        int iUltimos = 5;
        if(results.size() < iUltimos)
            iUltimos = results.size();

        ArrayList<dbDenuncia> lDbDenuncias = new ArrayList<>();
        for(int i = 0; i < iUltimos; i++){
            lDbDenuncias.add(results.get(i));
        }

        if(results.size() > 0) {
            populaListView();
        } else {
            showMensagemDenunciasNaoEncontradas();
        }
    }

    private void carregaDenunciasPorData(String data_inicio, String data_fim){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateInicio = new Date();
        Date dateFim = new Date();
        try {
            dateInicio = dateFormat.parse(data_inicio);
            dateFim = dateFormat.parse(data_fim);
        } catch (ParseException e) {
            Log.e("DateParse", e.getMessage());
        }

        results = ((CoreApplication)getApplication()).
                realm.where(dbDenuncia.class)
                .between("data", dateInicio, dateFim)
                .findAllSorted("id", Sort.DESCENDING);

        if(results.size() > 0) {
            populaListView();
        } else {
            showMensagemDenunciasNaoEncontradas();
        }
    }

    private  void carregaDenunciasPorHashtag(String filtro){
        hashtagsResult = ((CoreApplication)getApplication()).
                realm.where(dbHashtag.class)
                .contains("hashtag", filtro, Case.INSENSITIVE)
                .findAll();

        Integer[] ids = new Integer[hashtagsResult.size()];
        for (int i = 0; i < hashtagsResult.size(); i++) {
            ids[i] = hashtagsResult.get(i).id;
        }

        if(ids.length > 0) {
            results = ((CoreApplication) getApplication()).
                    realm.where(dbDenuncia.class)
                    .in("id", ids)
                    .findAllSorted("id", Sort.DESCENDING);

            populaListView();
        } else {
            showMensagemDenunciasNaoEncontradas();
        }
    }

    private void showMensagemDenunciasNaoEncontradas(){
        Toast.makeText(this, "Nenhuma denúncia encontrada.", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_ir_nova_denuncia)
    public void onClickNovaDenuncia() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, Constantes.REQUEST_CAMERA);
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
