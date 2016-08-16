package moe.haruue.goodhabits;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.avos.avoscloud.AVOSCloud;
import com.jude.utils.JActivityManager;
import com.jude.utils.JUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import im.fir.sdk.FIR;
import moe.haruue.goodhabits.util.ResourcesLoader;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        // JUtils
        JUtils.initialize(this);
        JUtils.setDebug(BuildConfig.DEBUG, BuildConfig.APPLICATION_ID);
        registerActivityLifecycleCallbacks(JActivityManager.getActivityLifecycleCallbacks());
        // ResourceLoader
        ResourcesLoader.initialize(this);
        // BugHD
        FIR.init(this);
        // LeanCloud
        AVOSCloud.initialize(this, "OLR6WcgAdRG09PKf2dnkN4ab-gzGzoHsz", "IxmvxbCw28dGnQNpbLFQsjHt");
        // Universal-Image-Loader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static Context getContext() {
        return context;
    }

    public static SharedPreferences getCommonSharedPreferences() {
        return context.getSharedPreferences("common", MODE_PRIVATE);
    }

}