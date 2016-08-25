package com.natanaelribeiro.bichodenuncia.AppCode.Estrutura;

import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by natanaelribeiro on 21/08/16.
 */
public class Denuncia extends RealmObject {
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
    public RealmList<Hashtag> hashtag;
    public RealmList<Midia> midia;
    public RealmList<Retorno> retorno;

    public Denuncia(){
        this.hashtag = new RealmList();
        this.midia = new RealmList();
        this.retorno = new RealmList();
    }

    @Override
    public String toString() {
        return titulo;
    }
}
