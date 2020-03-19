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

    private static final int USBRequestTypeOut = UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT;
    private static final int USBRequestTypeIn = UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_IN;

    /**
     * Register Address Setting
     */

    private static final byte CMD_SET_READN = (byte) 0xA0;

    private UsbDeviceConnection mUsbDeviceConnection = null;
    private UsbInterface mUsbInterface = null;
    private UsbEndpoint mUsbEndpoint = null;


    DeviceCommunicator(Context context, UsbDevice device) throws IOException {
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        if(manager == null) {
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
            //This must be done before sending or receiving data on any UsbEndpoints belonging to the interface.
            Dlog.e("Usb ClaimInterface Test Faile");
            throw new IOException("Device USB ClaimInterface Error");
        }

        mUsbEndpoint = mUsbInterface.getEndpoint(0);

    }

    //Control Transfer is mainly used for sending commands or receiving a device descriptor.
    //Bulk Transfer is used for sending large packets of data to your target device.

    private synchronized int SendControlTransfer(int request, int value, byte[] buffer) throws IOException {
        int commandLength = 0;

        //length of data transferred (or zero) for success, or negative value for failure
        //Error code, Prevention of Data Leakage case
        commandLength = mUsbDeviceConnection.controlTransfer(USBRequestTypeOut, request, value, 0, buffer, (buffer == null) ? 0 : buffer.length, 3000);
        commandLength = mUsbDeviceConnection.controlTransfer(USBRequestTypeOut, request, value, 0, buffer, (buffer == null) ? 0 : buffer.length, 3000);
        commandLength = mUsbDeviceConnection.controlTransfer(USBRequestTypeOut, request, value, 0, buffer, (buffer == null) ? 0 : buffer.length, 3000);

        return commandLength;
    }

    private synchronized int ReceiveControlTransfer(int request, int value, byte[] CopiedBuffer) throws IOException {
        int commandLength = 0;

        byte[] SourceBuffer = new byte[CopiedBuffer.length];

        if( mUsbDeviceConnection == null) {
            Dlog.e("ReceiveControlTransfer UsbDeviceConnection not Exist");
            throw new IOException("ReceiveControlTransfer Connection Error");
        }

        commandLength = mUsbDeviceConnection.controlTransfer(USBRequestTypeIn, request, value, 0, SourceBuffer, (SourceBuffer == null) ? 0 : SourceBuffer.length,3000);

        if(commandLength > 0) {
            //System.arraycopy(source, sourceStartPosition, WrittenValue, WrittenStartPosition, CopyLength)
            System.arraycopy(SourceBuffer, 0, CopiedBuffer, 0, SourceBuffer.length);
        }

        return commandLength;
    }

    public int ReadBulkTransfer(byte[] buffer, int offset, int length)
    {
        return mUsbDeviceConnection.bulkTransfer(mUsbEndpoint, buffer, offset, length, 500);
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
