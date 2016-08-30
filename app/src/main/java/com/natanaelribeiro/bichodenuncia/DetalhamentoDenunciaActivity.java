package com.natanaelribeiro.bichodenuncia;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetalhamentoDenunciaActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.edit_descricao_denuncia)
    public EditText edit_descricao_denuncia;
    @BindView(R.id.edit_hashtags)
    public EditText edit_hashtags;
    @BindView(R.id.edit_localizacao)
    public EditText edit_localizacao;
    @BindView(R.id.btn_continuar_para_identificacao)
    public ImageButton btn_continuar_para_identificacao;
    @BindView(R.id.btnAudio)
    public ImageButton btnAudio;
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Marker markerMyLocation;

    private String filePath;
    private String fileType;
    private String categoria;
    private String animal;
    private double loc_latitude;
    private double loc_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhamento_denuncia);

        setFullscreenActivity();
        setupToolbar(true);

        ButterKnife.bind(this);

        carregaExtras();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void carregaExtras(){
        filePath = getIntent().getStringExtra("filePath");
        fileType = getIntent().getStringExtra("fileType");
        categoria = getIntent().getStringExtra("categoria");
        animal = getIntent().getStringExtra("animal");
    }

    @OnClick(R.id.btn_continuar_para_identificacao)
    public void onClickContinuar(){
        if(verificaInfoObrigatorio()) {
            Intent intent = new Intent(this, IdentificacaoActivity.class);

            intent.putExtra("filePath", filePath);
            intent.putExtra("fileType", fileType);
            intent.putExtra("categoria", categoria);
            intent.putExtra("animal", animal);
            intent.putExtra("descricao", edit_descricao_denuncia.getText().toString());
            intent.putExtra("hashtags", edit_hashtags.getText().toString());
            intent.putExtra("endereco", edit_localizacao.getText().toString());
            intent.putExtra("loc_latitude", loc_latitude);
            intent.putExtra("loc_longitude", loc_longitude);

            startActivity(intent);
        } else {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verificaInfoObrigatorio() {
        if(!edit_descricao_denuncia.getText().toString().equals("") &&
                !edit_hashtags.getText().toString().equals("") &&
                !edit_localizacao.getText().toString().equals(""))
            return true;
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        updateCamera(location);
    }


    public void showMe() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        updateCamera(mLastLocation);
    }

    public void updateCamera(Location loc) {
        LatLng eu = new LatLng(loc.getLatitude(), loc.getLongitude());
        if (markerMyLocation == null) {
            markerMyLocation = mMap.addMarker(new MarkerOptions().position(eu));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(eu, 16));
            setEnderecoFromLocation(loc);
        }

    }

    private void setEnderecoFromLocation(Location location){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            loc_latitude = location.getLatitude();
            loc_longitude = location.getLongitude();
            String endereco = addresses.get(0).getAddressLine(0);
            String cidade = addresses.get(0).getLocality();
            String estado = addresses.get(0).getAdminArea();
            String pais = addresses.get(0).getCountryName();
            String cep = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            Log.i("address", "endereco: " + endereco);
            Log.i("address", "cidade: " + cidade);
            Log.i("address", "estado: " + estado);
            Log.i("address", "pais: " + pais);
            Log.i("address", "cep: " + cep);
            Log.i("address", "knownName: " + knownName);

            edit_localizacao.setText(endereco + " - " + cidade + "/" + estado);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);//request code
            return;
        }
        showMe();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //showMe();
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnClick(R.id.btnAudio)
    public void onClickAudio(){
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if(activities.size() > 0) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale algo");
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
            startActivityForResult(intent, 0);
        } else {
            Toast.makeText(this, "Reconhecimento de voz indisponível", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            ArrayList<String> words = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(words.size() > 0)
                edit_descricao_denuncia.setText(words.get(0));
        }
    }
}
