package com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Realm;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by natanaelribeiro on 23/08/16.
 */
public class dbRetorno extends RealmObject {
    @PrimaryKey
    public int id;
    public int id_denuncia;
    public String entidade;
    public String descricao;
    public Date data;
}
