package rongim.gdut.com.rongim;

import android.app.Application;
import android.util.Log;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 2015/10/30.
 */
public class ImApplication extends Application{

    private static final String TAG="zhiqiang";
    @Override
    public void onCreate() {
        super.onCreate();
        initRongIM();
        ConnectRongIM();
    }

    private void initRongIM(){
        RongIM.init(this);
        RongIMManager.getInstanse().setContext(this);
    }

    private void ConnectRongIM(){
        String Token = "MH2i0B4ZT/hGjD41R2qi54hokzx6IbRfGD6ngdF6GI212jMZAac5std7KvAqsQ1HHJqI5kZJc55Wl4N4LYHzNZxbA3nz9DeI";
       RongIMManager.getInstanse().connectRongIM("808080","888","");
    }
}
