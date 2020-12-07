package info.androidhive.sqlite.utils;



import javax.inject.Singleton;

import dagger.Component;
import info.androidhive.sqlite.MapsActivity;

/**
 * Created by Abhi on 19-05-2016.
 */
@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
//    void inject(MainActivity mainActivity);
//    void inject(ShopDetailActivity shopDetailActivity);
    void inject(MapsActivity mapsActivity);

}
