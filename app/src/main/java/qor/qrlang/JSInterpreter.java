package qor.qrlang;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.UnsupportedEncodingException;

/**
 * Custom Javascript interpreter.
 * This takes the built in webview and uses it as a JS interpreter
 */
public class JSInterpreter {

    private WebView webView;

    JSInterpreter(Context context, JSInterface jsInterface, String jsIntName){
        webView = new WebView(context);
        webView.setWillNotDraw(true);
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webView.addJavascriptInterface(jsInterface, jsIntName);
    }

    public void destroy(){
        webView.destroy();
    }

    //Automatically interprets and runs given javascript
    public void interpret(String javascript){
        byte[] data;
        try {
            javascript = "<script>" + javascript + "</script>";
            data = javascript.getBytes("UTF-8");
            final String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            webView.loadUrl("data:text/html;charset=utf-8;base64," + base64);

        } catch (final UnsupportedEncodingException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }
}
