package co.haslo.usbDeviceManager;

import co.haslo.util.ConvertData;
import co.haslo.util.Dlog;

public class DeviceDataTransfer {

    private static final int SEQUENCE_SIZE = 4096;
    private static final int SCANLINE_COUNT = 128;
    private static final int READ_SCANLINE_NUMBER = 4;

    private static DeviceDataTransfer mDataTransferInstance = null;
    private static final Object mSyncBlock = new Object();

    private Thread mDataTransferThread;
    private DeviceCommunicator mDeviceCommunicator;
    private final Object mDataTransferBlock = new Object();


    private class DeviceDataTransferThread extends Thread {

        public DeviceDataTransferThread() {
            super();
        }

        public void run(){
            final int defaultReadSize = SEQUENCE_SIZE * READ_SCANLINE_NUMBER;
            final byte[] readBuffer = new byte[defaultReadSize];
            int readSize;

            while (!isInterrupted()) {

                try
                {
                    readSize = mDeviceCommunicator.ReadBulkTransfer(readBuffer, 0, defaultReadSize);

                    if(isInterrupted()) {
                        Dlog.i("Thread is interrupted");
                        break;
                    }
                    for(int i = 0; i < defaultReadSize; i+=4) {
                        byte Data03 = readBuffer[i + 3];
                        byte Data02 = readBuffer[i + 2];
                        byte Data01 = readBuffer[i + 1];
                        byte Data00 = readBuffer[i + 0];
                        byte[] DataArray = {Data03,Data02,Data01,Data00};
                        Dlog.i(ConvertData.byteArrayToHexString(DataArray));
                    }

                } catch (Exception e) {

                    Dlog.e("Thread Read Exception : " + e);

                    readSize = -1;
                }

                if(readSize <= 0) {
                    continue;
                }
            }

        }

    }

}
