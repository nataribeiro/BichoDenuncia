package com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by natanaelribeiro on 23/08/16.
 */
public class dbMidia extends RealmObject {
    @PrimaryKey
    public int id;
    public int id_denuncia;
    public int sequencia;
    public String tipo_midia;
    public String arquivo;
}
