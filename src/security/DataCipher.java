package security;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Objects;

import javax.crypto.Cipher;

/**
 * DataCipher encryption and decryption.
 */
public class DataCipher {

    private final EncryptionAlgorithm encryptionAlgorithm;

    /**
     * Instantiate DataCipher object.
     *
     * @throws NullPointerException If encryptionAlgorithm is null.
     */
    public DataCipher(final EncryptionAlgorithm encryptionAlgorithm) {
        this.encryptionAlgorithm = Objects.requireNonNull(encryptionAlgorithm);
    }

    /**
     * Encrypts the given data using the given key.
     *
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
     *
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
