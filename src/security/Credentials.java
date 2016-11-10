package security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Objects;

/**
 * Host credentials.
 */
public class Credentials {

    private final KeyPair keyPair;

    /**
     * Generate public and private keys using the specified algorithm.
     *
     * @throws NullPointerException     If encryptionAlgorithm is null.
     * @throws IllegalArgumentException When failed to create the keys.
     */
    public Credentials(final EncryptionAlgorithm encryptionAlgorithm) {
        Objects.requireNonNull(encryptionAlgorithm);
        try {
            keyPair = KeyPairGenerator.getInstance(encryptionAlgorithm.toString()).generateKeyPair();
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @return The public key generated.
     */
    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    /**
     * @return The private key generated.
     */
    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }
}
