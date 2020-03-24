package co.haslo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import co.haslo.usbDeviceManager.DeviceCommunicator;
import co.haslo.usbDeviceManager.DeviceHandler;
import co.haslo.usbDeviceManager.DeviceManager;
import co.haslo.util.Dlog;

public class MainActivity extends Activity {

    public static boolean DEBUG = false;

    private DeviceHandler mDeviceHandler = new DeviceHandler(this);

    /*System Element*/
    Process logcat;
    StringBuilder log = null;

    /*XML Element*/
    TextView logBoxText;

    Button menuBoxClearButton;
    Button menuBoxLoadButton;

    EditText sendBoxEdit;
    Button sendBoxButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.DEBUG = isDebuggable(this);
        Dlog.i("Start");

        /**
         * Handler Start
         */

        mDeviceHandler.initialize();

        /**
         * Log Box Setting
         */
        logBoxText = (TextView)findViewById(R.id.log_box_text);
        logBoxText.setMovementMethod(new ScrollingMovementMethod());

        /**
         * Menu Box Setting
         */
        //Clear
        menuBoxClearButton = (Button)findViewById(R.id.menu_box_clear_button);
        menuBoxClearButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dlog.i("menuBoxClearButton");
                logBoxText.setText(null);
                clearLogcatLog();
            }
        });

        //Load
        menuBoxLoadButton = (Button)findViewById(R.id.menu_box_load_button);
        menuBoxLoadButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dlog.i("menuBoxLoadButton");
                logBoxText.setText(getLogcat());
            }
        });

        /**
         * Send Box Setting
         */
        sendBoxEdit = (EditText)findViewById(R.id.send_box_edit);
        sendBoxButton = (Button)findViewById(R.id.send_box_button);

        sendBoxButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dlog.i("sendBoxButton");
                logBoxText.append(sendBoxEdit.getText() + System.lineSeparator());
                sendBoxEdit.getText().clear();
            }
        });
    }

    private String getLogcat() {
        try {
            log = new StringBuilder();
            logcat = Runtime.getRuntime().exec("logcat -d -v time");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(logcat.getInputStream()),4*1024);
            String lineString;
            String separator = System.lineSeparator();
            while ((lineString = bufferedReader.readLine()) != null) {
                if(lineString.contains("JWLK")){
                    log.append(lineString);
                    log.append(separator);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return log.toString();
    }

    private void clearLogcatLog() {
        try {
            @SuppressWarnings("unused")
            Process process = Runtime.getRuntime().exec("logcat -b all -c");
            log = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onStart() {
        super.onStart();
        Dlog.d("onStart");
        mDeviceHandler.handlingStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Dlog.d("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Dlog.d("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Dlog.d("onStop");
        mDeviceHandler.handlingStop();
        Dlog.i("Device Handler Stop And Reset Complete");
        mDeviceHandler.handlingClear();
        Dlog.i("Device Handler Clear Complete");

        Dlog.i("onStop Completed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Dlog.d("onDestroy");
    }

    @Override
    public void onBackPressed() {
        Dlog.d("Application Finsih");
        finish();
    }



    /**
     * get Debug Mode
     *
     * @param context
     * @return
     */
    private boolean isDebuggable(Context context) {
        boolean debuggable = false;

        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(context.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
            /* debuggable variable will remain false */
        }

        return debuggable;
    }
}
