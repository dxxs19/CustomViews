package com.wei.customviews.view.activity;

import com.wei.customviews.model.bean.IpModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * author: WEI
 * date: 2017/7/28
 */

public interface IpService {
    /**
     * Retrofit提供的请求方式注解有@GET和@POST等，分别代表GET请求和POST请求，我们在这里访问的界面是
     * “getIpInfo.php”。参数注解有@PATH和@Query等，@Query就是我们的请求的键值对的设置，在这里
     * @Query(“ip”)代表键，“String ip”则代表值。
     *
     * @param ip
     * @return
     */
    @GET("getIpInfo.php")
    Call<IpModel> getIpMsg(@Query("ip")String ip);
}
