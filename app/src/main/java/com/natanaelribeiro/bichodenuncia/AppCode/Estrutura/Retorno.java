package com.natanaelribeiro.bichodenuncia.AppCode.Estrutura;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by natanaelribeiro on 23/08/16.
 */
public class Retorno extends RealmObject {
    @PrimaryKey
    public int id;
    public String entidade;
    public String descricao;
    public String data;
}
