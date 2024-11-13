package com.example.mymoviesapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private static final String BASE_URL = "http://10.0.2.2:8080/";

    private EditText searchEditText;
    private ListView searchResultsListView;
    private ArrayList<String> movieTitles;
    private ArrayAdapter<String> adapter;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Inicializar views
        searchEditText = view.findViewById(R.id.search_edit_text);
        searchResultsListView = view.findViewById(R.id.search_results_list_view);

        // Inicializar lista e adaptador
        movieTitles = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, movieTitles);
        searchResultsListView.setAdapter(adapter);

        // Inicializar RequestQueue
        requestQueue = Volley.newRequestQueue(getContext());

        // Configurar o listener do botão de busca
        view.findViewById(R.id.search_button).setOnClickListener(v -> searchMovies());

        // Configurar o listener de clique nos itens da lista
        searchResultsListView.setOnItemClickListener((adapterView, view1, position, id) -> {
            String selectedMovieTitle = movieTitles.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("movieTitle", selectedMovieTitle);
            MovieDetailFragment detailFragment = new MovieDetailFragment();
            detailFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void searchMovies() {
        String keyword = searchEditText.getText().toString().trim();

        // Verificar se o campo de busca não está vazio
        if (keyword.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, insira um título de filme para buscar.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = BASE_URL + "api/movies/search?keyword=" + keyword;

        Log.d(TAG, "Realizando busca com a URL: " + url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    movieTitles.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject movie = response.getJSONObject(i);
                            String title = movie.getString("title");
                            movieTitles.add(title);
                        }
                        adapter.notifyDataSetChanged();

                        if (movieTitles.isEmpty()) {
                            Toast.makeText(getContext(), "Nenhum filme encontrado para a busca.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Erro ao analisar a resposta JSON", e);
                        Toast.makeText(getContext(), "Erro ao processar os dados recebidos.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    String errorMsg = "Erro ao buscar filmes.";
                    if (error.networkResponse != null) {
                        errorMsg += " Código de resposta: " + error.networkResponse.statusCode;
                    } else {
                        errorMsg += " Verifique sua conexão.";
                    }
                    Log.e(TAG, "Erro na requisição: ", error);
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
        );

         jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                 5000,
                 DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonArrayRequest);
    }
}
