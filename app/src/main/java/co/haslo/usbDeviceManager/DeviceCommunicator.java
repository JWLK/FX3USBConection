package co.haslo.usbDeviceManager;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import java.io.IOException;

import co.haslo.util.Dlog;

public class DeviceCommunicator {

    private static final int FX3_VndRequestTypeOut = UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT;
    private static final int FX3_VndRequestTypeIn = UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_IN;

    /**
     * Register Address Setting
     */

    private static final byte CMD_SET_READN     = (byte) 0xA0;

    private UsbDeviceConnection mUsbDeviceConnection = null;
    private UsbInterface mUsbInterface = null;


    DeviceCommunicator(Context context, UsbDevice device) throws IOException {
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        if(manager == null){
            throw new IOException("Get USBManager Not Exist");
        }
        mUsb
    }


}
