package com.natanaelribeiro.bichodenuncia.Custom;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Denuncia;
import com.natanaelribeiro.bichodenuncia.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by natanaelribeiro on 21/08/16.
 */
public class ListaDenunciasAdapter extends BaseAdapter {
    List<Denuncia> denuncias;
    Context contexto;

    public ListaDenunciasAdapter(Context contexto, List<Denuncia> denuncias){
        this.contexto = contexto;
        this.denuncias = denuncias;
    }

    @Override
    public int getCount() {
        return denuncias.size();
    }

    @Override
    public Object getItem(int position) {
        return denuncias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)contexto.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_denuncia, null);
            ImageView img = (ImageView)convertView.findViewById(R.id.listDenuncia_imgStatus);

            TextView txtStatus = (TextView) convertView.findViewById(R.id.listDenuncia_txtStatus);
            txtStatus.setText(denuncias.get(position).titulo);

            if(denuncias.get(position).situacao.equals("pendente")) {
                img.setImageResource(R.drawable.amarelo);
                txtStatus.setTextColor(contexto.getResources().getColor(R.color.yellow));
            } else if(denuncias.get(position).situacao.equals("resolvido")) {
                img.setImageResource(R.drawable.resolvido);
                txtStatus.setTextColor(contexto.getResources().getColor(R.color.green));
            } else if(denuncias.get(position).situacao.equals(("nao_resolvido"))) {
                img.setImageResource(R.drawable.nao_resolvido);
                txtStatus.setTextColor(contexto.getResources().getColor(R.color.red));
            }
        }

        return convertView;
    }
}
