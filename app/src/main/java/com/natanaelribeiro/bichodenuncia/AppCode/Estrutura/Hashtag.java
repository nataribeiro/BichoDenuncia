package com.natanaelribeiro.bichodenuncia.AppCode.Estrutura;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by natanaelribeiro on 21/08/16.
 */
public class Hashtag extends RealmObject {
    @PrimaryKey
    public int id;
    public String hashtag;
}
