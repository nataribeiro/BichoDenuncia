package com.natanaelribeiro.bichodenuncia.Custom;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Realm.dbDenuncia;
import com.natanaelribeiro.bichodenuncia.R;

import io.realm.RealmResults;

/**
 * Created by natanaelribeiro on 21/08/16.
 */
public class ListaDenunciasAdapter extends BaseAdapter {
    RealmResults<dbDenuncia> dbDenuncias;
    Context contexto;

    public ListaDenunciasAdapter(Context contexto, RealmResults<dbDenuncia> dbDenuncias){
        this.contexto = contexto;
        this.dbDenuncias = dbDenuncias;
    }

    @Override
    public int getCount() {
        return dbDenuncias.size();
    }

    @Override
    public Object getItem(int position) {
        return dbDenuncias.get(position);
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
            txtStatus.setText(dbDenuncias.get(position).titulo);

            if(dbDenuncias.get(position).situacao.equals("pendente")) {
                img.setImageResource(R.drawable.amarelo);
                txtStatus.setTextColor(contexto.getResources().getColor(R.color.yellow));
            } else if(dbDenuncias.get(position).situacao.equals("resolvido")) {
                img.setImageResource(R.drawable.resolvido);
                txtStatus.setTextColor(contexto.getResources().getColor(R.color.green));
            } else if(dbDenuncias.get(position).situacao.equals(("nao_resolvido"))) {
                img.setImageResource(R.drawable.nao_resolvido);
                txtStatus.setTextColor(contexto.getResources().getColor(R.color.red));
            }
        }

        return convertView;
    }
}
