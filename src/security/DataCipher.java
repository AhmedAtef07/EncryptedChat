package security;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Objects;

/**
 * DataCipher encryption and decryption.
 */
public class DataCipher {

    private final EncryptionAlgorithm encryptionAlgorithm;

    /**
     * Instantiate DataCipher object.
     * @param encryptionAlgorithm
     * @throws NullPointerException If encryptionAlgorithm is null.
     */
    public DataCipher(final EncryptionAlgorithm encryptionAlgorithm) {
        this.encryptionAlgorithm = Objects.requireNonNull(encryptionAlgorithm);
    }

    /**
     * Encrypts the given data using the given key.
     * @param data
     * @param key
     * @return The encrypted data.
     * @throws IllegalArgumentException If general security exception thrown.
     */
    public byte[] encrypt(final byte[] data, final Key key) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(encryptionAlgorithm.toString());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (final GeneralSecurityException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Decrypts the given data using the given key.
     * @param data
     * @param key
     * @return The decrypted data.
     * @throws IllegalArgumentException If general security exception thrown.
     */
    public byte[] decrypt(final byte[] data, final Key key) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(encryptionAlgorithm.toString());
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (final GeneralSecurityException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
