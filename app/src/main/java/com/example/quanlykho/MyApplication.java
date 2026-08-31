package com.example.quanlykho;

import android.app.Application;
import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Cấu hình Cloudinary
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "dvydbcvmn");


        MediaManager.init(this, config);
    }
}