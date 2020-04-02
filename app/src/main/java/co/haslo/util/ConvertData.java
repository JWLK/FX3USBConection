package co.haslo.util;

public class ConvertData {

    /**
     * String 타입의 변수 hexString에 16진수 문자열 01020304FF11을 대입합니다.
     *
     * String hexString = "01020304FF11";
     *
     *
     *
     * hexStringToByteArray 메소드를 사용하면 16진수 문자열을 바이트 배열 ByteArray로 변환합니다.
     *
     * byte[] ByteArray = hexStringToByteArray(hexString);
     *
     *
     *
     * 다음과 같이 바이트 배열에 들어가게 됩니다.
     *
     * { 0x01, 0x02, 0x03, 0x04, 0xFF, 0x11}
     *
     *
     *
     * byteArrayToHexString 메소드를 사용하면 다시 16진수 문자열로 변환할 수 있습니다.
     *
     * String str = byteArrayToHexString(ByteArray);
     *
     */



    public static String byteArrayToHexString(byte[] bytes){

        StringBuilder sb = new StringBuilder();

        for(byte b : bytes){

            sb.append(String.format("%02X", b&0xff));
        }

        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


    public static short[] hexStringToShortArray(String s) {
        int len = s.length();
        byte[] dataByte;
        short[] dataShort = new short[len / 4];

        dataByte = hexStringToByteArray(s);

        for (int i = 0, counter = 0 ; i < dataByte.length; i += 2, counter++) {
            dataShort[counter] = (short) ( (dataByte[i] & 0xFF) << 8 | (dataByte[i+1] & 0xFF) ) ;
        }
        return dataShort;
    }

}
