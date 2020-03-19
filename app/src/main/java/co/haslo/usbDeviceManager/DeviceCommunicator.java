package co.haslo.usbDeviceManager;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
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

    private static final byte CMD_SET_READN = (byte) 0xA0;

    private UsbDeviceConnection mUsbDeviceConnection = null;
    private UsbInterface mUsbInterface = null;
    private UsbEndpoint mUsbEndpoint = null;


    DeviceCommunicator(Context context, UsbDevice device) throws IOException {
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        if(manager == null){
            throw new IOException("Get USBManager Not Exist");
        }
        mUsbInterface = device.getInterface(0);
        mUsbDeviceConnection = manager.openDevice((device));

        if(mUsbDeviceConnection == null) {
            Dlog.e("UsbDeviceConnection not Exist");
            throw new IOException("Device Connection Error");
        }

        if(mUsbEndpoint == null) {
            Dlog.e("UsbEndpoint not Exist");
            throw new IOException("Device Endpoint Error");
        }

        if(!mUsbDeviceConnection.claimInterface(mUsbInterface, true)) {
            Dlog.e("Usb ClaimInterface Test Faile");
            throw new IOException("Device USB ClaimInterface Error");
        }

        mUsbEndpoint = mUsbInterface.getEndpoint(0);

    }

    public void Clear() {
        try {
            boolean release = false;
            release = mUsbDeviceConnection.releaseInterface(mUsbInterface);
            Dlog.d("Device Communicator Clear" + release);
            mUsbDeviceConnection.close();
        } catch (Exception e) {
            Dlog.e("Device Communicator Clear Exception Error");
        }
    }




}
