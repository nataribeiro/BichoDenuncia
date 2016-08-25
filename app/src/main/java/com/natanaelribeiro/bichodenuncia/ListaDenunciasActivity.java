package com.natanaelribeiro.bichodenuncia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.natanaelribeiro.bichodenuncia.AppCode.CoreApplication;
import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Denuncia;
import com.natanaelribeiro.bichodenuncia.Custom.ListaDenunciasAdapter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class ListaDenunciasActivity extends BaseActivity {
    private ListView listTasks;
    public static RealmResults<Denuncia> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_denuncias);

        setFullscreenActivity();
        setupToolbar(true);

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
            case "hashtag":{
                String filtro = getIntent().getStringExtra("filtro");
                carregaDenunciasPorHashtag(filtro);
                break;
            }
        }
    }

    private RealmChangeListener callback = new RealmChangeListener() {
        public void onChange(Object element) {
            results = (RealmResults<Denuncia>) element;
            results = results.sort("id", Sort.DESCENDING);
            listTasks.setAdapter(new ListaDenunciasAdapter(ListaDenunciasActivity.this, results));
        }
    };

    private void carregaUltimasDenuncias() {
        RealmResults<Denuncia> result = ((CoreApplication)getApplication()).realm.where(Denuncia.class).findAllAsync();
        result.addChangeListener(callback);
    }

    private void carregaDenunciasPorData(String data_inicio, String data_fim){
        //TODO
    }

    private  void carregaDenunciasPorHashtag(String filtro){
        //TODO
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
//                    //No retorno popula os componentes e seta a variavel user para uso futuro
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
