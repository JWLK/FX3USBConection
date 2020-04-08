package co.haslo.usbDeviceManager;

import co.haslo.util.Dlog;

import static co.haslo.util.ConvertData.hexStringArrayToInt32bit8HexArray;
import static co.haslo.util.ConvertData.hexStringToInt32bit8HexArray;
import static co.haslo.util.ConvertData.hexStringToShort16bit4HexArray;

public class DeviceRegisterSetting {

    public static void writeCommandHexData(DeviceCommunicator device, String hexString)
    {
        short[] dataShort = hexStringToShort16bit4HexArray(hexString);
        for(short shorts : dataShort){
            Dlog.d(String.format("0x%04X", shorts));
        }

        try {
            device.DataTransferSingleWrite(dataShort[0], dataShort[1]);
        } catch (Exception e) {
            e.printStackTrace();
            Dlog.e(e.toString());
        }

    }

    public static void writeBulkHexData(DeviceCommunicator device)
    {
        String[] sendStringArray =  {"980100FF", "98000003","980100FF", "98000003","980100FF", "98000003","980100FF", "98000003","980100FF", "98000003","980100FF", "98000003"};
        int[] sendIntArray = hexStringArrayToInt32bit8HexArray(sendStringArray);
        for(int ints : sendIntArray){
            Dlog.d(String.format("0x%04X", ints));
        }
        try {
            device.DataTransferBulkWrite(sendStringArray);
        } catch (Exception e) {
            e.printStackTrace();
            Dlog.e(e.toString());
        }

    }

    public static void counterSet(DeviceCommunicator device)
    {
        device.DataTransferSingleWrite((short) 0x9801, (short) 0x00FF);
    }


    public static void counterStart(DeviceCommunicator device)
    {
        device.DataTransferSingleWrite((short) 0x9800, (short) 0x0003);
    }
}
