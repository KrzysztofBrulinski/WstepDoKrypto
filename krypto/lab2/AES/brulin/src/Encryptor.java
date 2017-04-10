
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Encryptor {

    private static final int KEY = 0;
    private static final int IV = 1;


    public static String getRandomHexString(int length) {
        SecureRandom r = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        while (stringBuilder.length() < length) {
            stringBuilder.append(Integer.toHexString(r.nextInt()));
        }
        return stringBuilder.toString().substring(0, length);
    }

    public static byte[] encryptAES(byte[] key, byte[] iv, String cipherMode) {
        try {
            byte[] msg = "Elixir to funkcyjny i współbieżny język programowania, stworzony w 2012 roku przez Jose Valima. Programy napisane w Elixirze uruchamiane są na maszynie wirtualnej Erlanga, znanej m.in. z możliwości tworzenia małym kosztem bardzo wielu procesów.".getBytes();

            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(cipherMode);

            if (cipherMode.equals("AES/ECB/PKCS5Padding")) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            }

            return cipher.doFinal(msg);
        } catch (Exception e) {
            System.out.println("Exception during encryption: " + e);
        }
        return null;
    }

    public static void putKeysToFile() {
        File file = new File("src/klucze.txt");
        Path path = Paths.get("src/klucze.txt");
        Charset charset = Charset.forName("UTF-8");
        if (!file.exists()) {
            byte[] key = DatatypeConverter.parseHexBinary(getRandomHexString(64));
            byte[] iv = DatatypeConverter.parseHexBinary(getRandomHexString(32));
            List<String> lines = Arrays.asList(HexBin.encode(key), HexBin.encode(iv));
            try {
                Files.write(path, lines, charset, StandardOpenOption.WRITE);
            } catch (IOException ignored) {

            }
        }
    }

    public static void putCiphersToFile(byte[]... ciphers) {
        Path path = Paths.get("src/szyfry.txt");
        List<String> list = new ArrayList<>();
        for (byte[] b : ciphers) {
            list.add(HexBin.encode(b));
        }
        try {
            Files.write(path, list, Charset.forName("UTF-8"), StandardOpenOption.WRITE);
        } catch (IOException ignored) {

        }
    }

    public static byte[] getBytesFromFile(int keyOrIv) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/klucze.txt"));
        return DatatypeConverter.parseHexBinary(lines.get(keyOrIv));
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        putKeysToFile();
        byte[] key = getBytesFromFile(KEY);
        byte[] iv = getBytesFromFile(IV);
        byte[] ecb = encryptAES(key, iv, "AES/ECB/PKCS5Padding");
        byte[] cbc = encryptAES(key, iv, "AES/CBC/PKCS5Padding");
        byte[] ctr = encryptAES(key, iv, "AES/CTR/NoPadding");

        putCiphersToFile(ecb, cbc, ctr);
    }
}
