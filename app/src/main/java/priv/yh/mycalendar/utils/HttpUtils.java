package priv.yh.mycalendar.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Http Utils
 *
 * @author yanhan
 * @date 2019-05-05
 */
public class HttpUtils {
    private static final String TAG = "HttpUtils";

    public static final int HTTP_RESPONSE_SUCCESS = 1;
    public static final int HTTP_RESPONSE_FAIL = 0;

    /**
     * request a http, and response through a handler
     * @param url url string for request
     * @param handler a response
     */
    public static void getRequest(String url, Handler handler) {
        OkHttpClient httpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).get().build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Http Request failed: " + e.toString());
                Message msg = new Message();
                msg.what = HTTP_RESPONSE_FAIL;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //NOTICE: response.body() must be called only once, or it will throw
                // java.lang.IllegalStateException: closed
                String bodyString = response.body().string();
                Log.d(TAG, "Http request success: " + bodyString);
                try {
                    Message msg = new Message();
                    msg.what = HTTP_RESPONSE_SUCCESS;
                    msg.obj = bodyString;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    Log.e(TAG, "error occured: " + e.toString());
                }
            }
        });
    }
    
}
