package info.androidhive.sqlite.utils;

import info.androidhive.sqlite.Model.RoutesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("place/nearbysearch/json")
    Call<NearByShopsResponse> getNearByShops(@Query("location") String location,
                                             @Query("radius") int radius,
                                             @Query("type") String type,
                                             @Query("key") String ApiKey);

    @GET("place/details/json")
    Call<ShopDetailsResponse> getShopDetails(@Query("placeid") String placeid,
                                             @Query("key") String ApiKey);

    @GET("directions/json")
    Call<RoutesResponse> getDirections(@Query("origin") String origin,
                                       @Query("destination") String destination,
                                       @Query("key") String ApiKey);

}
