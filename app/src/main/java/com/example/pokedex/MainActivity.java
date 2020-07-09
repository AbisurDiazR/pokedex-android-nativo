package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.CellIdentity;
import android.util.Log;

import com.example.pokedex.models.Pokemon;
import com.example.pokedex.models.PokemonRespuesta;
import com.example.pokedex.pokeapi.PokeApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    //creamos la instancia global de retrofit
    private Retrofit retrofit;

    //variable local para el log
    private String TAG = "Pokedex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializamos la url base
        retrofit = new Retrofit.Builder()
                .baseUrl("http://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //obtenerDatos() es una interface de java, retrofit trabaja bien con ellas
        obtenerDatos();
    }

    private void obtenerDatos(){
        PokeApiService service = retrofit.create(PokeApiService.class);
        Call<PokemonRespuesta> pokemonRespuestaCall = service.obtenerListaPokemon();

        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {
            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
                if (response.isSuccessful()){
                    PokemonRespuesta pokemonRespuesta = response.body();
                    //hacemos un arraylist para guardar los pokemon de pokemonRespuesta
                    ArrayList<Pokemon> listaPokemon = pokemonRespuesta.getResults();

                    for(int i=0; i < listaPokemon.size(); i++){
                        Pokemon p = listaPokemon.get(i);
                        Log.i(TAG," Pokemon: "+p.getName());
                    }

                }else{
                    Log.e(TAG," onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                Log.e(TAG," onFailure "+t.getMessage());
            }
        });
    }
}
