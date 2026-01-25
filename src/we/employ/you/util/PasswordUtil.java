package we.employ.you.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import we.employ.you.model.User;


public class PasswordUtil {

    private Cipher encryptCipher;
    private Cipher decryptCipher;
    // Buffer used to transport the bytes from one stream to another
    private final byte[] buffer;
    private byte[] iv;
    private final SecretKey key;
    private boolean ivSet;
    
    private final String defaultPassphrase = "Vug_tKDU-1YveAh4A;5HGafkArUE,8XyJFSKO#Xkj#:a%;G0WoSM4a@|G.XnoXVXWlgf!G3%iZZmEqO*5KFktuA&5p8Qir;%M9D4TxCk6H,l*y7Ajjt,7zs@3ULTTYi;oLI%hgHbBg_!3SXM&twY$fr,L_;c97CRmEai.M58?1W0$iglww1xFDFKK$v.a6Qbl15Wt1UzwlL$@Ocn66w.:HIsw#PF@*%.43NPTMkD6c.h&gH!|_EC9v&Ew*1xw0;n";

    
    public PasswordUtil() throws Exception {
        key = getKey(this.defaultPassphrase);
        buffer = new byte[1024];
        ivSet = false;
    }
    
    private void initDecrypt() throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException {
        if (ivSet) {
            decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        } else {
            throw new RuntimeException("IV parameter not set before decrypting");
        }
    }

    private void initEncrypt()throws InvalidKeyException, InvalidParameterSpecException, 
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
        if (!ivSet) {
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            AlgorithmParameters params = encryptCipher.getParameters();
            setIv(params.getParameterSpec(IvParameterSpec.class).getIV());
        } else {
            encryptCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        }
    }

    private SecretKey getKey(String passPhrase) throws NoSuchAlgorithmException, 
            InvalidKeySpecException {
        byte[] salt = {(byte) 0xA9,
            (byte) 0x9B,
            (byte) 0xC8,
            (byte) 0x32,
            (byte) 0x56,
            (byte) 0x35,
            (byte) 0xE3,
            (byte) 0x03};
        
        KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), salt, 1024, 128);
        
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        
        SecretKey tempKey = factory.generateSecret(spec);
        
        SecretKey secretKey = new SecretKeySpec(tempKey.getEncoded(), "AES");
            
        return secretKey;
    }

    /**
     * Method to encryptPassword a stream
     *
     * @param input input stream to be decrypted
     * @param output decrypted version of the input stream
     * @throws Exception thrown if an unexpected error occurs
     */
    public void decrypt(InputStream input, OutputStream output) throws Exception {
        
        byte[] ivBuffer = new byte[16];
        int numberRead = input.read(ivBuffer);

        if (numberRead != 16) {
            throw new RuntimeException("Could not read IV from input");
        }
        
        setIv(ivBuffer);
        
        initDecrypt();
        // Bytes written to out will be encrypted
        try (CipherInputStream inputStream = new CipherInputStream(input, decryptCipher)) {
            // Read in the cleartext bytes and write to out to encryptPassword
            while ((numberRead = inputStream.read(buffer)) >= 0) {
                output.write(buffer, 0, numberRead);
            }
        } finally {
            input.close();
        }
    }

    /**
     * Method to encryptPassword a stream
     *
     * @param input input stream to be encrypted
     * @param output encrypted version of the input stream
     * @throws Exception thrown if any error occurs while calling this method
     */
    public void encrypt(InputStream input, OutputStream output) throws Exception {
        CipherInputStream cipherInputStream = null;
        
        try {
            initEncrypt();
            // Bytes read from in will be decrypted
            cipherInputStream = new CipherInputStream(input, encryptCipher);

            // Read in the decrypted bytes and write the cleartext to out
            int numberRead;
            
            output.write(iv);
            
            if (input instanceof PipedInputStream) {
                PipedInputStream inputStream = (PipedInputStream) input;
                boolean canContinue = true;
                int waitCount = 0;
                
                while (canContinue) {
                    if (inputStream.available() > 0) {
                        numberRead = cipherInputStream.read(buffer);
                        
                        if (numberRead >= 0) {
                            output.write(buffer, 0, numberRead);
                        }
                    } else {
                        if (waitCount < 3) {
                            waitCount++;
                            try {
                                Thread.sleep(10);
                            } finally {
                                cipherInputStream.close();
                            }
                        } else {
                            canContinue = false;
                        }
                    }
                }
                if (cipherInputStream.available() > 0) {
                    numberRead = cipherInputStream.read(buffer);
                    if (numberRead >= 0) {
                        output.write(buffer, 0, numberRead);
                    }
                }
            } else {
                while ((numberRead = cipherInputStream.read(buffer)) >= 0) {
                    output.write(buffer, 0, numberRead);
                }
            }
        } finally {
            if (cipherInputStream != null) {
                cipherInputStream.close();
            }
        }
    }

    /**
     * Raw encryption method for storage in the database as a byte array in a
     * RAW column
     *
     * @param plaintext
     * @return returnBytes the byte array composing the encrypted string
     * @throws Exception thrown if any unexpected error occurs while calling this method
     */
    private byte[] encryptRaw(String plaintext) throws Exception {
        initEncrypt();
        
        byte[] cipherBytes;
        byte[] returnBytes;
        
        cipherBytes = encryptCipher.doFinal(plaintext.getBytes("UTF-8"));
        returnBytes = new byte[iv.length + cipherBytes.length];

        System.arraycopy(iv, 0, returnBytes, 0, iv.length);

        System.arraycopy(cipherBytes, 0, returnBytes, iv.length, cipherBytes.length);

        return returnBytes;
    }

    /**
     * processor for encrypting a string to a string
     *
     * @param plainText
     * @return an encoded 
     * @throws Exception thrown if any unexpected error occurs
     */
    public String encryptPassword(String plainText) throws Exception {
        return Base64.getEncoder().encodeToString(encryptRaw(plainText));
    }

    /**
     * processor for decrypting a byte array from a database back into a string
     *
     * @param cipherBytes
     * @return a decrypted string
     * @throws Exception thrown if any unexpected error occurs while calling this method
     */
    public String decryptPassword(byte[] cipherBytes) throws Exception {
        byte[] ivBuffer = new byte[16];
        byte[] data = new byte[cipherBytes.length - ivBuffer.length];
        
        System.arraycopy(cipherBytes, 0, ivBuffer, 0, ivBuffer.length);
        
        setIv(ivBuffer);
        initDecrypt();
        
        for (int i = 0; i < data.length; i++) {
            byte val = cipherBytes[i + ivBuffer.length];
            data[i] = val;
        }
        
        return new String(decryptCipher.doFinal(data), "UTF-8");
    }

    /**
     * method for decrypting a string back into a string
     *
     * @param cipherText
     * @return a decrypted string
     * @throws Exception
     */
    public String decrypt(String cipherText) throws Exception {
        return decryptPassword(Base64.getDecoder().decode(cipherText));
    }

    private void setIv(byte[] iv) {
        if (!ivSet) {
            if (iv.length == 16) {
                this.iv = iv;
                ivSet = true;
            } else {
                throw new RuntimeException("IV parameter is not 16 bytes long");
            }
        } else {
            throw new RuntimeException("IV parameter already set");
        }
    }

    /**
     * Generates a passwords consisting of random characters.
     * @return
     */
    public static String generatePassword() {
        Random random = new Random();
        String[] specialCharacters = {
            "!", "@", "#", "$", "%", "&", "*"
        };

        //random capital letter
        char capitalLetter = (char) (random.nextInt(26) + 'A');
        String upperCaseLetter = String.valueOf(capitalLetter);

        //random lower-case letters
        char lowerLetterOne = (char) (random.nextInt(26) + 'a');
        String lowerCaseLetterOne = String.valueOf(lowerLetterOne);

        char lowerLetterTwo = (char) (random.nextInt(26) + 'a');
        String lowerCaseLetterTwo = String.valueOf(lowerLetterTwo);

        char lowerLetterThree = (char) (random.nextInt(26) + 'a');
        String lowerCaseLetterThree = String.valueOf(lowerLetterThree);
        
        char lowerLetterFour = (char) (random.nextInt(26) + 'a');
        String lowerCaseLetterFour = String.valueOf(lowerLetterFour);

        char lowerLetterFive = (char) (random.nextInt(26) + 'a');
        String lowerCaseLetterFive = String.valueOf(lowerLetterFive);

        //random digit
        int digit = random.nextInt(10);
        String requiredDigit = String.valueOf(digit);

        //random special character
        int specialCharacterIndex = random.nextInt(6);
        String specialCharacter = specialCharacters[specialCharacterIndex];

        return new StringBuilder(upperCaseLetter)
                .append(lowerCaseLetterOne).append(specialCharacter)
                .append(lowerCaseLetterTwo).append(requiredDigit)
                .append(lowerCaseLetterThree).append(lowerCaseLetterFour)
                .append(lowerCaseLetterFive).toString();
    }
    
    public static void writeCredentialsToDesktop(User user) throws Exception {
        File file = new File(System.getProperty("user.home") + "/" + user.getUserId() + "_login_info.txt");
        
        try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file))) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Your user name is ").append(user.getUserId()).append("\n");
            stringBuilder.append("Your password is ")
                .append(user.getCurrentPassword().getDecryptedPassword());

            writer.write(stringBuilder.toString().getBytes());
        }
	}
}
