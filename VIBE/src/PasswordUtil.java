
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

    public static String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String salt) {
        PBEKeySpec spec = null;

        try {
            byte[] saltBytes = Base64.getDecoder().decode(salt);

            spec = new PBEKeySpec(
                password.toCharArray(),
                saltBytes,
                120000,
                256
            );

            SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new IllegalStateException(
                "Unable to process password", e
            );
        } finally {
            if (spec != null) {
                spec.clearPassword();
            }
        }
    }

    public static boolean verifyPassword(
            String enteredPassword,
            String storedHash,
            String storedSalt) {

        String calculatedHash =
            hashPassword(enteredPassword, storedSalt);

        return java.security.MessageDigest.isEqual(
            Base64.getDecoder().decode(calculatedHash),
            Base64.getDecoder().decode(storedHash)
        );
    }
}

