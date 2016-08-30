package com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by natanaelribeiro on 21/08/16.
 */
public class dbDenuncia extends RealmObject {
    @PrimaryKey
    public int id;
    public int id_server;
    public String titulo;
    public String descricao;
    public String situacao;
    public String data;
    public String categoria_animal;
    public String tipo_animal;
    public String endereco;
    public double endereco_latitude;
    public double endereco_longitude;
    public String denunciante_email;
    public String denunciante_telefone;
    public String id_dispositivo;
    public RealmList<dbHashtag> dbHashtag;
    public RealmList<dbMidia> dbMidia;
    public RealmList<dbRetorno> dbRetorno;

    public dbDenuncia(){
        this.dbHashtag = new RealmList();
        this.dbMidia = new RealmList();
        this.dbRetorno = new RealmList();
    }

    @Override
    public String toString() {
        return titulo;
    }
}
