package com.natanaelribeiro.bichodenuncia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.View;

import com.natanaelribeiro.bichodenuncia.AppCode.Constantes;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private String filePath;
    private String fileType;

    @BindView(R.id.nav_view) public NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFacebookSigIn();
        setContentView(R.layout.activity_base);
        setupGoogleSignIn();

        setFullscreenActivity();
        setupToolbar(false);
        getSupportActionBar().setTitle(R.string.app_name);
        setupNavigationDrawer();
    }

    public void onClickNovaDenuncia(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, Constantes.REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constantes.REQUEST_CAMERA){
            if(resultCode == RESULT_OK){
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
        } else if(requestCode == RC_SIGN_IN){
            onBaseActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            if (mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
                return;
            }
        }
    }
}
