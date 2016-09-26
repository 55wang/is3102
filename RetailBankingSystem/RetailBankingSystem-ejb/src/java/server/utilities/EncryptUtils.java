/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author VIN-S
 */
public class EncryptUtils {
    // Crytographic constants
    private final String DEFAULT_KEYSTORE_TYPE = "JKS";
    private final String DEFAULT_KEYSTORE_PROVIDER = "SUN";
    private final String DEFAULT_DOMAIN_CONFIG_DIRECTORY = System.getProperty("user.dir");
    private final String DEFAULT_KEYSTORE_NAME = "keystore.jks";
    private final char[] DEFAULT_KEYSTORE_PASSWORD = new char[]{'c', 'h', 'a', 'n', 'g', 'e', 'i', 't'};
    private final int DEFAULT_PRIVATEKEY_LENGTH = 633;
    private final String DEFAULT_CERTIFICATE_ALIAS = "s1as";
    private final String DEFAULT_CIPHER_ALGORITHM_NAME = "AES/CBC/PKCS5Padding";
    private final String DEFAULT_CIPHER_NAME = "AES";
    private final int DEFAULT_KEY_LENGTH = 16;
    private final int DEFAULT_IV_LENGTH = 16;
    private final String DEFAULT_CHARSET = "ISO-8859-1";

    private KeyStore keyStore = null;
    private PublicKey publicKey = null;
    private PrivateKey privateKey = null;
    private X509Certificate cert = null;

    private byte[] key = null;
    private byte[] iv = null;

    private static EncryptUtils cryptographicHelper = null;

    public static void main(String[] args) {
        String a = "apple";
        EncryptUtils eu = new EncryptUtils();
        String ciperText = eu.encrypt(a, eu.getDefaultEncryptionKey(), eu.getDefaultEncryptionIV());
        System.out.println(ciperText);
        
        String plainText = eu.encrypt(ciperText, eu.getDefaultEncryptionKey(), eu.getDefaultEncryptionIV());
        System.out.println(plainText);
        
        
    }

    public EncryptUtils()
    {
        doInitializeCryptographicHelper();
    }

    
    
    public static EncryptUtils getInstanceOf()
    {
        if (cryptographicHelper == null)
        {
            cryptographicHelper = new EncryptUtils();
        }

        return cryptographicHelper;
    }

    
    
    private void doInitializeCryptographicHelper()
    {
        try
        {
            FileInputStream keyStoreStream = new FileInputStream(DEFAULT_DOMAIN_CONFIG_DIRECTORY + System.getProperty("file.separator") + DEFAULT_KEYSTORE_NAME);
            keyStore = KeyStore.getInstance(DEFAULT_KEYSTORE_TYPE, DEFAULT_KEYSTORE_PROVIDER);
            keyStore.load(keyStoreStream, DEFAULT_KEYSTORE_PASSWORD);
            cert = (X509Certificate) keyStore.getCertificate(DEFAULT_CERTIFICATE_ALIAS);
            publicKey = cert.getPublicKey();
            privateKey = (PrivateKey) keyStore.getKey(DEFAULT_CERTIFICATE_ALIAS, DEFAULT_KEYSTORE_PASSWORD);
            byte[] certPrivateKey = privateKey.getEncoded();
            key = new byte[DEFAULT_KEY_LENGTH];
            iv = new byte[DEFAULT_IV_LENGTH];
            for (int i = 0; i < DEFAULT_KEY_LENGTH; i++)
            {
                key[i] = certPrivateKey[i];
            }
            for (int i = 0; i < DEFAULT_IV_LENGTH; i++)
            {
                iv[i] = certPrivateKey[DEFAULT_PRIVATEKEY_LENGTH - DEFAULT_IV_LENGTH - i];
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch (KeyStoreException ex)
        {
            ex.printStackTrace();
        }
        catch (NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
        }
        catch (NoSuchProviderException ex)
        {
            ex.printStackTrace();
        }
        catch (CertificateException ex)
        {
            ex.printStackTrace();
        }
        catch (UnrecoverableKeyException ex)
        {
            ex.printStackTrace();
        }
    }

    
    
    public String encrypt(String stringToEncrypt, byte[] key, byte[] iv)
    {
        String encryptedString = null;

        try
        {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM_NAME);
            
            SecretKeySpec actualKey = new SecretKeySpec(key, DEFAULT_CIPHER_NAME);
            IvParameterSpec actualIV = new IvParameterSpec(iv, 0, DEFAULT_IV_LENGTH);
            cipher.init(Cipher.ENCRYPT_MODE, actualKey, actualIV);

            byte[] encryptedBuffer = cipher.doFinal(stringToEncrypt.getBytes(DEFAULT_CHARSET));
            encryptedString = new String(encryptedBuffer, DEFAULT_CHARSET);
        }
        catch (NoSuchAlgorithmException noSuchAlgoEx)
        {
            System.err.println("********** NoSuchAlgorithmException: " + DEFAULT_CIPHER_ALGORITHM_NAME);
        }
        catch (NoSuchPaddingException noSuchPadEx)
        {
            System.err.println("********** NoSuchPaddingException: " + noSuchPadEx);
        }
        catch (Exception ex)
        {
            System.err.println("********** Exception: " + ex);
        }

        return encryptedString;
    }

    
    
    public String decrypt(String stringToDecrypt, byte[] key, byte[] iv)
    {
        String decryptedString = null;

        try
        {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM_NAME);
            SecretKeySpec actualKey = new SecretKeySpec(key, DEFAULT_CIPHER_NAME);
            IvParameterSpec actualIV = new IvParameterSpec(iv, 0, DEFAULT_IV_LENGTH);
            cipher.init(Cipher.DECRYPT_MODE, actualKey, actualIV);
            byte[] decryptedBuffer = cipher.doFinal(stringToDecrypt.getBytes(DEFAULT_CHARSET));
            decryptedString = new String(decryptedBuffer, DEFAULT_CHARSET);
        }
        catch (NoSuchAlgorithmException noSuchAlgoEx)
        {
            System.err.println("********** NoSuchAlgorithmException: " + DEFAULT_CIPHER_ALGORITHM_NAME);
        } catch (NoSuchPaddingException noSuchPadEx)
        {
            System.err.println("********** NoSuchPaddingException: " + noSuchPadEx);
        }
        catch (Exception ex)
        {

            System.err.println("********** Exception: " + ex);
        }

        return decryptedString;
    }
    
    public KeyStore getDefaultKeyStore()
    {
        return keyStore;
    }

    public X509Certificate getDefaultCertificate()
    {
        return cert;
    }

    public PublicKey getDefaultPublicKey()
    {
        return publicKey;
    }

    public PrivateKey getDefaultPrivateKey()
    {
        return privateKey;
    }

    public byte[] getDefaultEncryptionKey()
    {
        return key;
    }

    public byte[] getDefaultEncryptionIV()
    {
        return iv;
    }
}
