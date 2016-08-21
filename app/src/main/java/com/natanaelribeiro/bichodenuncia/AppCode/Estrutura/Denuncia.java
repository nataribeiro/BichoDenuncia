package com.natanaelribeiro.bichodenuncia.AppCode.Estrutura;

import java.util.List;

/**
 * Created by natanaelribeiro on 21/08/16.
 */
public class Denuncia {

    public String titulo;
    public String descricao;
    public List<Hashtag> hastags;
    public String situacao;
    public String endereco;
    public double endereco_latitude;
    public double endereco_longitude;
    public String denunciante_email;
    public String denunciante_telefone;
}
