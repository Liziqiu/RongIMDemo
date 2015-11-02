package rongim.gdut.com.rongim.net;

import android.content.Context;

import com.sea_monster.network.BaseApi;
import com.sea_monster.network.NetworkManager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;


/**
 * 
 * ClassName: HttpRequestUtil
 * 
 * @Function: Http请求
 * @date: 2015年7月9日 上午10:37:14
 * 
 * @author Bloom
 * @version
 */
public class HttpRequestUtil extends BaseApi {

    private static final String HOST_HOME = "https://api.cn.ronghub.com";
    private static final String UTF8 = "UTF-8";

    private static final String APPKEY = "RC-App-Key";
    private static final String NONCE = "RC-Nonce";
    private static final String TIMESTAMP = "RC-Timestamp";
    private static final String SIGNATURE = "RC-Signature";

    public HttpRequestUtil(Context context) {
        super(NetworkManager.getInstance(), context);
    }

    // 设置body体
    public static void setBodyParameter(StringBuilder sb, HttpURLConnection conn) throws IOException {
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.writeBytes(sb.toString());
        out.flush();
        out.close();
    }

    // 添加签名header
    public static HttpURLConnection CreatePostHttpConnection(String appKey, String appSecret, String uri)
            throws MalformedURLException, IOException, ProtocolException {

        String nonce = String.valueOf(Math.random() * 1000000);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        StringBuilder toSign = new StringBuilder(appSecret).append(nonce).append(timestamp);
        String sign = CodeUtil.hexSHA1(toSign.toString());

        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        conn.setRequestProperty(APPKEY, appKey);
        conn.setRequestProperty(NONCE, nonce);
        conn.setRequestProperty(TIMESTAMP, timestamp);
        conn.setRequestProperty(SIGNATURE, sign);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        return conn;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

    public static SdkHttpResult returnResult(HttpURLConnection conn) throws Exception, IOException {
        String result;
        InputStream input = null;
        if (conn.getResponseCode() == 200) {
            input = conn.getInputStream();
        } else {
            input = conn.getErrorStream();
        }
        result = new String(readInputStream(input));
        return new SdkHttpResult(conn.getResponseCode(), result);
    }

    // 获取token
    public static SdkHttpResult getToken(String appKey, String appSecret, String userId, String userName,
            String portraitUri, FormatType format) throws Exception {

        HttpURLConnection conn =
                CreatePostHttpConnection(appKey, appSecret, HOST_HOME + "/user/getToken." + format.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
        sb.append("&name=").append(URLEncoder.encode(userName, UTF8));
        sb.append("&portraitUri=").append(URLEncoder.encode(portraitUri, UTF8));
        setBodyParameter(sb, conn);

        return returnResult(conn);
    }

}
