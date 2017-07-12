package com.wei.utillibrary.net;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * author: WEI
 * date: 2017/3/1
 */

public class HttpRequest
{
    public interface GitHubService
    {
        @GET("users/{user}/repos")
        Call<List<String>> listUsers();
    }
}
