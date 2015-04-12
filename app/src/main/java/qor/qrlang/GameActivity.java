package qor.qrlang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Activity controlling the emulated QRLang game
 */
public class GameActivity extends FragmentActivity{

    QRLangView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        view = (QRLangView)findViewById(R.id.qrlang_view);

        //Sets the emulated code
        view.setCode(intent.getStringExtra("qrlcode"));
        //Starts the game
        view.start();
    }

    @Override
    protected void onDestroy() {
        setResult(2);
        //destroys the webview emulating the game
        view.interpreter.destroy();
        super.onDestroy();
    }

    protected void onStop(){
        setResult(2);
        super.onStop();
    }
}
