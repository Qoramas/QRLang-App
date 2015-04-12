package qor.qrlang;

import android.webkit.JavascriptInterface;

/**
 * Interface to pass to the webview to ensure that all required functions are callable
 */
public interface JSInterface {

    @JavascriptInterface
    public void log(String value);

    @JavascriptInterface
    public void drawbox(float x, float y, float w, float h);

    @JavascriptInterface
    public void drawbox(float x, float y, float w, float h, int colour);

    @JavascriptInterface
    public void write(String text, float x, float y);

    @JavascriptInterface
    public void write(String text, float x, float y, int colour);

    @JavascriptInterface
    public void lock();

    @JavascriptInterface
    public void unlock();

    @JavascriptInterface
    public boolean key(String k);

    @JavascriptInterface
    public void update();
//
//    @JavascriptInterface
//    public void test(String str, int x);
}
