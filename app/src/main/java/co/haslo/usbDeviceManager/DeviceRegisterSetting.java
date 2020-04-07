package co.haslo.usbDeviceManager;

import co.haslo.util.Dlog;

import static co.haslo.util.ConvertData.hexStringToShortArray;

public class DeviceRegisterSetting {

    public static void writeCommandHexData(DeviceCommunicator device, String hexString)
    {
        short[] dataShort = hexStringToShortArray(hexString);
        for(short shorts : dataShort){
            Dlog.d(String.format("0x%04X", shorts));
        }

        try {
            device.DataTransferSingleWrite(dataShort[0], dataShort[1]);
        } catch (Exception e) {

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
