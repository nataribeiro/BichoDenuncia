package com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by natanaelribeiro on 29/08/16.
 */
public class Denuncia {
    public Integer id;
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
    public List<Hashtag> hashtag;
    public List<Midia> midia;
    public List<Retorno> retorno;

    public Denuncia(){
        this.hashtag = new ArrayList();
        this.midia = new ArrayList();
        this.retorno = new ArrayList();
    }
}
