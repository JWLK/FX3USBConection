package co.haslo.usbDeviceManager;

import android.hardware.usb.UsbDevice;
import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Date;

import co.haslo.MainActivity;
import co.haslo.util.Dlog;


public class DeviceHandler extends Handler {

    private MainActivity mainActivity;
    private DeviceManager mDeviceManager;
    private DeviceCommunicator mDeviceCommunicator;
    //private DirectDataTransfer mDirectDataTransfer;
    //private PropertyManager mPropertyManager;

    private SimpleDateFormat formatPrint = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss");

    private Thread mUSBRealTimeController = new Thread(){
        @Override
        public void run() {
            while (!isInterrupted()) {

                Date currentTime = new Date();
                String printString = formatPrint.format(currentTime);
                    Dlog.i(printString);

                try
                {
                    Thread.sleep(10000);
                }
                catch (InterruptedException e)
                {
                    Dlog.e("mUSBRealTimeController Error : " + e );
                }

            }
        }
    };

    public DeviceHandler(MainActivity activity) {
        mainActivity = activity;
    }

    public void initialize() {
        //mDirectDataTransfer = DirectDataTransfer.getInstance();
        mUSBRealTimeController.start();
    }

    public void handleMessage(Message msg) {
        UsbDevice device = (UsbDevice) msg.obj;

        if(msg.what == DeviceManager.MSG_USB_CONNECTION) {
            Dlog.i("Device Manager Monitoring Service Send Message : On USB Conneted");
            try {
                mDeviceCommunicator = mDeviceManager.CreateDeviceCommunicator(mainActivity.getApplicationContext(), device);
            } catch (Exception e) {
                Dlog.e("handleMessage Error "+ e );
            }

            if(mDeviceCommunicator != null) {

            }
        } else {
            Dlog.i("Device Manager Monitoring Service Send Message : On USB DisConnected");
            handlingClear();
        }
    }

    public void handlingStart() {
        mDeviceManager = DeviceManager.getInstance();
        mDeviceManager.DeviceManagerMonitoringStart(mainActivity.getApplicationContext(), this);
    }

    public void handlingStop() {
        if(mDeviceCommunicator != null)
        {
            try
            {
                Dlog.i("try freeze");
                //DeviceSetting.sendFreeze(mDeviceCommunicator);
                Dlog.i("try freeze");
            }
            catch(Exception e)
            {
                Dlog.e("send device freeze error : " + e.getMessage());
            }
        }
        mDeviceManager.DeviceManagerMonitoringClear();
    }

    public void handlingClear() {
        //mDirectDataTransfer.deregisterDeviceCommunicator();
        mDeviceCommunicator = null;

    }


}
