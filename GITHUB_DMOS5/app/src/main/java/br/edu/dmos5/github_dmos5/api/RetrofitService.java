package br.edu.dmos5.github_dmos5.api;

import java.util.List;

import br.edu.dmos5.github_dmos5.model.Repositorio;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {

    @GET("{endereco}")
    Call<List<Repositorio>> buscaPorNome(@Path(value="endereco", encoded = true) String nome);
}
