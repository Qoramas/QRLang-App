package qor.qrlang;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Main activity for the app. Contains the buttons for traversing the app.
 */
public class MainActivity extends FragmentActivity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);

        //Scan button activates the scanner when pressed
        Button scanBtn = (Button)findViewById(R.id.scan_button);
        scanBtn.setOnClickListener(this);

        //About button activates an about screen when pressed describing the app
        Button aboutBtn = (Button)findViewById(R.id.about_button);
        aboutBtn.setOnClickListener(this);

        //Exit button returns to the home screen
        Button exitBtn = (Button)findViewById(R.id.exit_button);
        exitBtn.setOnClickListener(this);
    }

    //Handles click events
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.scan_button:
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
            break;
            case R.id.about_button:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("The QRLang app allows you " +
                        "to scan any QRLang code and " +
                        "play the game encoded on it " +
                        "immediately without a data " +
                        "connection.\n\n Try it out!")
                        .setCancelable(false)
                        .setPositiveButton("OK", null);
                AlertDialog alert = builder.create();
                alert.show();
            break;
            case R.id.exit_button:
                Log.d("QRLang", "exitbutton");
                finish();
            break;
        }
    }

    //Handles scan results
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        //Verifies that something was scanned
        if (scanningResult != null && scanningResult.getContents() != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 $:%*+-=./";

            //Verifies the scan is of a QR Code
            if(!scanFormat.equals("QR_CODE")){
                Toast toast = Toast.makeText(getApplicationContext(), "Not a QRCODE!", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            //Verifies the QRCode contains only QRLang characters
            for(int i = 0; i < scanContent.length(); i++){
                if(!characters.contains(scanContent.substring(i,i+1))){
                    Toast toast = Toast.makeText(getApplicationContext(), "Not a valid QRLang code!", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
            }

            //Starts the game
            Intent in = new Intent(this, GameActivity.class );
            in.putExtra("qrlcode", scanContent);
            startActivity(in);

        } else{
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
