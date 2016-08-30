package com.natanaelribeiro.bichodenuncia.AppCode;

import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Service.Denuncia;
import com.natanaelribeiro.bichodenuncia.AppCode.Estrutura.Service.ResultadoOperacao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by natanaelribeiro on 23/08/16.
 */
public interface IRetrofit {
    @GET("Denuncias/Usuario/id")
    Call<List<Denuncia>> getDenuncias(@Path("id") String id);

    @GET("dbDenuncia/id")
    Call<Denuncia> getDenuncia(@Path("id") String id);

    @POST("dbDenuncia")
    Call<ResultadoOperacao> enviaDenuncia(@Body Denuncia dbDenuncia);
}
