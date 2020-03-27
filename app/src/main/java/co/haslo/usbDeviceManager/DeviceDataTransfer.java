package co.haslo.usbDeviceManager;

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
            final int dataReadSize = SEQUENCE_SIZE * READ_SCANLINE_NUMBER;
            final byte[] readBuffer = new byte[dataReadSize];

        }

    }

}
