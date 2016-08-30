package com.natanaelribeiro.bichodenuncia;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class ListaDenunciasActivity extends BaseActivity {

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
                carregaUltimasDenuncias();
                break;
            }
            case "data":{
                String data_inicio = getIntent().getStringExtra("data_inicio");
                String data_fim = getIntent().getStringExtra("data_fim");
                carregaDenunciasPorData(data_inicio, data_fim);
                break;
            }
            case "dbHashtag":{
                String filtro = getIntent().getStringExtra("filtro");
                carregaDenunciasPorHashtag(filtro);
                break;
            }
        }
    }

    private RealmChangeListener callback = new RealmChangeListener() {
        public void onChange(Object element) {
            results = (RealmResults<dbDenuncia>) element;

            listTasks.setAdapter(new ListaDenunciasAdapter(ListaDenunciasActivity.this, results));
        }
    };

    private void carregaUltimasDenuncias() {
        results = ((CoreApplication)getApplication()).
                realm.where(dbDenuncia.class)
                .findAllSortedAsync("id", Sort.DESCENDING);
        results.addChangeListener(callback);

        int iUltimos = 5;
        if(results.size() < iUltimos)
            iUltimos = results.size();

        ArrayList<dbDenuncia> lDbDenuncias = new ArrayList<>();
        for(int i = 0; i < iUltimos; i++){
            lDbDenuncias.add(results.get(i));
        }

        listTasks.setAdapter(new ListaDenunciasAdapter(ListaDenunciasActivity.this, results));
    }

    private void carregaDenunciasPorData(String data_inicio, String data_fim){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateInicio = new Date();
        Date dateFim = new Date();
        try {
            dateInicio = dateFormat.parse(data_inicio);
            dateInicio = dateFormat.parse(data_fim);
        } catch (ParseException e) {
            Log.e("DateParse", e.getMessage());
        }

        results = ((CoreApplication)getApplication()).
                realm.where(dbDenuncia.class)
                .between("data", dateInicio, dateFim)
                .findAllSortedAsync("id", Sort.DESCENDING);
        results.addChangeListener(callback);

        listTasks.setAdapter(new ListaDenunciasAdapter(ListaDenunciasActivity.this, results));
    }

    private  void carregaDenunciasPorHashtag(String filtro){
        hashtagsResult = ((CoreApplication)getApplication()).
                realm.where(dbHashtag.class)
                .contains("dbHashtag", filtro)
                .findAllSortedAsync("id", Sort.DESCENDING)
                .distinct("id");


        RealmQuery<dbDenuncia> query = ((CoreApplication)getApplication()).
                realm.where(dbDenuncia.class)
                .beginGroup();

        int i = 0;
        for (dbHashtag h:hashtagsResult) {
            if(i != 0)
                query = query.or();
            query = query.equalTo("id", h.id_denuncia);
        }
        query = query.endGroup();

        results = query.findAllSortedAsync("id", Sort.DESCENDING);
    }

//    private void GetContatoFromService(String id){
//        //Utiliza retrofit para buscar as informações
//        Gson gson = new GsonBuilder().create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://dev.4all.com:3003")
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        service = retrofit.create(IRetrofit.class);
//        Call<Contato> call = service.getContato(id);
//        call.enqueue(new Callback<Contato>() {
//            @Override
//            public void onResponse(Call<Contato> call, Response<Contato> response) {
//                if(response.body() != null) {
//                    //No dbRetorno popula os componentes e seta a variavel user para uso futuro
//                    user = response.body();
//
//                    getSupportActionBar().setIcon(R.drawable.ic_location_on_white_24dp);
//                    getSupportActionBar().setTitle(user.cidade + " - " + user.bairro);
//                    ImageView imgFoto = (ImageView)findViewById(R.id.imgFoto);
//                    new DownloadImageTask(imgFoto).execute(user.urlFoto);
//                    TextView txt_texto = (TextView)findViewById(R.id.txt_texto);
//                    txt_texto.setText(user.texto);
//                    TextView txt_titulo = (TextView)findViewById(R.id.txt_titulo);
//                    txt_titulo.setText(user.titulo);
//                    TextView txt_endereco = (TextView)findViewById(R.id.txt_endereco);
//                    txt_endereco.setText(user.endereco);
//                    FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
//                    new DownloadImageTask(fab).execute(user.urlLogo);
//
//                    //Centraliza o mapa utilizando a latitude e longitude
//                    LatLng location = new LatLng(user.latitude, user.longitude);
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
//                    //Desabilita a navegação no mapa
//                    mMap.getUiSettings().setAllGesturesEnabled(false);
//
//                    ListView listView_comentarios = (ListView)findViewById(R.id.listView_comentarios);
//                    ListaComentariosAdapter adapter = new ListaComentariosAdapter(getBaseContext(), user.comentarios);
//                    listView_comentarios.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Contato> call, Throwable t) {
//
//            }
//        });
//    }
}
