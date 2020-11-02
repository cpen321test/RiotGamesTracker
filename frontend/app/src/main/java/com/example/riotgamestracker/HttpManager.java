package com.example.riotgamestracker;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.riotgamestracker.models.MatchHistory;
import com.example.riotgamestracker.models.Summoner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpManager {
    private static HttpManager instance;
    private static Context ctx;
    private RequestQueue queue;
    private final String serverUrl = "http://52.149.183.181:8081/";
    private final DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    public HttpManager(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static synchronized HttpManager getInstance(Context context) {
        if (instance == null) {
            instance = new HttpManager(context);
        }
        return instance;
    }


    public void getSummoner(String summoner, final MutableLiveData<Summoner> data) {
        String url = serverUrl + "summoner?name=" + summoner;
        System.out.println(url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Summoner res = new Summoner();
                        try {
                            res.name = response.getJSONObject("Summoner").getString("name");
                            res.level = response.getJSONObject("Summoner").getInt("summonerLevel");

                            data.postValue(res);
                        } catch (JSONException exception) {
                            Log.d("Error", "onResponse: " + exception.getMessage());
                            res.error = true;
                            res.errorMessage = exception.getMessage();
                            data.postValue(res);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Summoner res = new Summoner();
                        res.error = true;
                        res.errorMessage = error.getMessage();
                        data.postValue(res);
                        Log.d("Error", "onErrorResponse: " + error.getMessage());
                    }
                });

        request.setRetryPolicy(retryPolicy);
        queue.add(request);
    }

    public void getMatchHistory(String summoner, final MutableLiveData<MatchHistory> data) {

        String url = serverUrl + "summoner?name=" + summoner;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MatchHistory matchHistory = new MatchHistory(response);

                        data.postValue(matchHistory);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MatchHistory res = new MatchHistory(error.getMessage());
                        data.postValue(res);
                        Log.d("Error", "onErrorResponse: " + error.getMessage());
                    }
                });

        request.setRetryPolicy(retryPolicy);
        queue.add(request);
    }

}
