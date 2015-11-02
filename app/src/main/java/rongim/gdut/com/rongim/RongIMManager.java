package rongim.gdut.com.rongim;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import rongim.gdut.com.rongim.net.FormatType;
import rongim.gdut.com.rongim.net.HttpRequestUtil;
import rongim.gdut.com.rongim.net.SdkHttpResult;

/**
 * Created by zhiqiang on 2015/11/2.
 */
public class RongIMManager{

    public final static int CONNECT_SUCCESS=0;
    public final static  String CONNECT_CHANGE_BROADCAST = "ir.com.connect.change";
    public final static int CONNECT_ERROR=1;

    private static RongIMManager instanse;
    private SharedPreferences mPreferences;
    private final Handler handler = new Handler(){  // 必须保证主线程上new 这个handler(RongIMManager 必须在主线程上第一次创建）
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CONNECT_SUCCESS:
                    SendConnectStatusChangeBroadcast(CONNECT_SUCCESS);
                    break;
                case CONNECT_ERROR:
                    SendConnectStatusChangeBroadcast(CONNECT_ERROR);
                    break;
            }
        }
    };
    private ConnectThread connectThread;
    private RongIMClient.ConnectCallback ConnectCallBack = new RongIMClient.ConnectCallback(){
        @Override
        public void onSuccess(String s) {
            handler.sendMessage(handler.obtainMessage(CONNECT_SUCCESS));
        }

        @Override
        public void onError(RongIMClient.ErrorCode errorCode) {
            handler.sendMessage(handler.obtainMessage(CONNECT_ERROR));
        }

        @Override
        public void onTokenIncorrect() {

        }
    };

    private void SendConnectStatusChangeBroadcast(int status) {
        Intent intent = new Intent();
        intent.setAction(CONNECT_CHANGE_BROADCAST);
        intent.putExtra("status",status);
        context.sendBroadcast(intent);
    }

    private Context context;

    private RongIMManager(){
    }

    public static RongIMManager getInstanse(){
        if(instanse == null){
            instanse = new RongIMManager();
        }
        return instanse;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void connectRongIM(String UserId,String UserName,String portraitUri){
        connectThread = new ConnectThread();
        connectThread.UserId = UserId;
        connectThread.UserName = UserName;
        connectThread.portraitUri = portraitUri;
        connectThread.start();
    }

    private void tryConnectRongIM(String UserId,String UserName,String portraitUri){
        String token = getTokenFormSharePreference(UserId);
        if(token == null || token.equals("default")){
            token = getTokenFormService(UserId,UserName,portraitUri);
            //保存回本地
            SharedPreferences.Editor edit = mPreferences.edit();
            edit.putString(UserId,token);
            edit.apply();
        }
        RongIM.connect(token,ConnectCallBack);
    }

    private String getTokenFormService(String UserId,String UserName,String portraitUri) {
        String AppKey ="lmxuhwagx8dfd";
        String AppSecret ="bklYtWFh7nV";
        SdkHttpResult result = null;
        try {
            result = HttpRequestUtil.getToken(AppKey,AppSecret,UserId,UserName,portraitUri, FormatType.json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null) {

            if (result.getHttpCode() == 200) {

                String results = result.getResult();
                System.out.print("******获取token成功,token:" + results);
                String token = "";
                String code = "";
                String userid = "";
                try {
                    JSONObject dataJson = new JSONObject(results);
                    code = dataJson.getString("code");
                    userid = dataJson.getString("userId");
                    token = dataJson.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 获取成功
               return token;

            } else {

                String code = result.getHttpCode() + "";
                System.out.print("获取token失败, ErroCode:" + code);
                return null;
            }
        }
        return null;
    }

    private String getTokenFormSharePreference(String userId) {
        if(context == null)return null;
        if(mPreferences == null) mPreferences =PreferenceManager.getDefaultSharedPreferences(context);
        String token = mPreferences.getString(userId, "default");
        return token;
    }

    private class ConnectThread extends Thread{
        public String UserId;
        public String UserName;
        public String portraitUri;

        @Override
        public void run() {
            super.run();
            tryConnectRongIM(UserId,UserName,portraitUri);
        }
    }
}
