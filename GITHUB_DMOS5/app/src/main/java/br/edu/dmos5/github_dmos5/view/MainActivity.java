package br.edu.dmos5.github_dmos5.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.dmos5.github_dmos5.R;
import br.edu.dmos5.github_dmos5.api.RetrofitService;
import br.edu.dmos5.github_dmos5.model.Repositorio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_PERMISSION = 64;
    private static final String BASE_URL = "https://api.github.com/users/";

    private ConstraintLayout dadosConstraintLayout;
    private EditText editTextNome;
    private Button buttonPesquisar;

    private RecyclerView recyclerViewRepositorios;
    private ItemRepositorioAdapter itemRepositorioAdapter;

    private List<Repositorio> repositorioList;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dadosConstraintLayout = findViewById(R.id.constraint_dados);
        editTextNome = findViewById(R.id.edittext_nome);
        buttonPesquisar = findViewById(R.id.button_pesquisar);
        recyclerViewRepositorios = findViewById(R.id.recylerview_repositorios);
        repositorioList = new ArrayList<>();

        buttonPesquisar.setOnClickListener(this);
        buttonPesquisar.setOnClickListener(this);

        itemRepositorioAdapter = new ItemRepositorioAdapter(repositorioList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewRepositorios.setLayoutManager(layoutManager);
        recyclerViewRepositorios.setAdapter(itemRepositorioAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_pesquisar:
                if(temPermissao()){
                    buscarRepositorio();
                }else{
                    solicitaPermissao();
                }
                break;
        }
    }

    private void atualizaRecyclerView(){
        recyclerViewRepositorios.getAdapter().notifyDataSetChanged();
    }

    private boolean temPermissao(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    private void solicitaPermissao(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
            final Activity activity = this;
            new AlertDialog.Builder(this)
                    .setMessage(R.string.explicacao_permissao)
                    .setPositiveButton(R.string.botao_permitir, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface  dialogInterface, int i) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET}, REQUEST_PERMISSION);
                        }
                    })
                    .setNegativeButton(R.string.botao_nao_permitir, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.INTERNET
                    },
                    REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equalsIgnoreCase(Manifest.permission.INTERNET) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    buscarRepositorio();
                }

            }
        }
    }

    private void buscarRepositorio(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        String nome = editTextNome.getText().toString();
        if(nome.isEmpty()){
            mostarMesagem(MainActivity.this, getString(R.string.campo_obrigatorio));
        }else{
            nome += "/repos";
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<List<Repositorio>> call = retrofitService.buscaPorNome(nome);

            call.enqueue(new Callback<List<Repositorio>>() {
                @Override
                public void onResponse(Call<List<Repositorio>> call, Response<List<Repositorio>> response) {
                    if(response.isSuccessful()){
                        List<Repositorio> list = response.body();
                        if (list != null){
                            dadosConstraintLayout.setVisibility(View.VISIBLE);
                            repositorioList.clear();
                            repositorioList.addAll(list);
                            atualizaRecyclerView();
                        }else{
                            dadosConstraintLayout.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Repositorio>> call, Throwable t) {
                    mostarMesagem(MainActivity.this, getString(R.string.erro_api));
                }
            });
        }
    }

    private void mostarMesagem(Context context, String mensagem){
        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
    }

}
