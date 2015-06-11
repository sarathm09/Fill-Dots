package t90.filldots;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class HashGeneratorUtils {

    public static String generateMD5(File file){
        return hashString(file, "MD5");
    }

    private static String hashString(File file, String algorithm){

        try {
            FileInputStream inputStream = new FileInputStream(file);
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            byte[] bytesBuffer = new byte[1024];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
                digest.update(bytesBuffer, 0, bytesRead);
            }

            byte[] hashedBytes = digest.digest();

            return convertByteArrayToHexString(hashedBytes);
        }catch (Exception ex) {

        }
        return "";
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}