package com.example.mymoviesapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailFragment extends Fragment {

    private TextView titleTextView, yearTextView, genreTextView, extractTextView;
    private ImageView posterImageView;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        titleTextView = view.findViewById(R.id.title_text_view);
        yearTextView = view.findViewById(R.id.year_text_view);
        genreTextView = view.findViewById(R.id.genre_text_view);
        extractTextView = view.findViewById(R.id.extract_text_view);
        posterImageView = view.findViewById(R.id.poster_image_view);
        requestQueue = Volley.newRequestQueue(requireContext());

        String movieTitle = getArguments() != null ? getArguments().getString("movieTitle") : "Unknown";
        loadMovieDetails(movieTitle);

        return view;
    }

    private void loadMovieDetails(String paramTitle) {
        // Atualize o URL conforme o ambiente (emulador ou dispositivo físico)
        String url = "http://10.0.2.2:8080/api/movies/search?keyword=" + paramTitle;
        // Para dispositivo físico, use:
        // String url = "http://192.168.5.29:8080/api/movies/search?keyword=" + paramTitle;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    if (response.length() > 0) {
                        try {
                            JSONObject movie = response.getJSONObject(0);
                            String title = movie.getString("title");
                            int year = movie.getInt("year");

                            // Parsing aprimorado para gêneros
                            JSONArray genresArray = movie.getJSONArray("genres");
                            StringBuilder genresBuilder = new StringBuilder();
                            for (int i = 0; i < genresArray.length(); i++) {
                                genresBuilder.append(genresArray.getString(i));
                                if (i < genresArray.length() - 1) {
                                    genresBuilder.append(", ");
                                }
                            }
                            String genre = genresBuilder.toString();

                            String extract = movie.getString("extract");
                            String thumbnail = movie.getString("thumbnail");

                            titleTextView.setText(title);
                            yearTextView.setText(String.valueOf(year));
                            genreTextView.setText(genre);
                            extractTextView.setText(extract);

                            Picasso.get()
                                    .load(thumbnail)
                                    .into(posterImageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Erro ao analisar detalhes do filme", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Nenhum detalhe encontrado para o filme", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    String errorMessage = error.getMessage();
                    String errorDetail = error.networkResponse != null ?
                            new String(error.networkResponse.data) : "Sem dados adicionais";

                    Log.e("MovieDetailFragment", "Erro ao carregar filme: " + errorMessage +
                            "\nDetalhes: " + errorDetail);

                    Toast.makeText(getContext(), "Erro ao carregar filme. Verifique o log para mais informações.", Toast.LENGTH_LONG).show();
                });

        requestQueue.add(jsonArrayRequest);
    }

}
