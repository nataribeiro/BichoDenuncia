package com.natanaelribeiro.bichodenuncia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.natanaelribeiro.bichodenuncia.Custom.RoundImage;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetalhesDenunciaActivity extends BaseActivity {

    @BindView(R.id.img_denuncia) public ImageView img_denuncia;
    RoundImage roundedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_denuncia);

        setFullscreenActivity();
        setupToolbar(true);

        ButterKnife.bind(this);

        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.default_user);
        roundedImage = new RoundImage(bm);
        img_denuncia.setImageDrawable(roundedImage);
    }
}
