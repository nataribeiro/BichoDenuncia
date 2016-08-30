package com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by natanaelribeiro on 21/08/16.
 */
public class dbHashtag extends RealmObject {
    @PrimaryKey
    public int id;
    public int id_denuncia;
    public String hashtag;
}
