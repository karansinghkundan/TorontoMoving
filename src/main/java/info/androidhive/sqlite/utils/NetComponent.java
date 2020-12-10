package info.androidhive.sqlite.utils;



import javax.inject.Singleton;

import dagger.Component;
import info.androidhive.sqlite.MapsActivity;


@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {

    void inject(MapsActivity mapsActivity);

}
