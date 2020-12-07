package info.androidhive.sqlite.utils;

import android.app.Application;


/**
 * Created by Abhi on 19-05-2016.
 */
public class MyApplication extends Application {

    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("https://maps.googleapis.com/maps/api/"))
                .build();
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }
}
