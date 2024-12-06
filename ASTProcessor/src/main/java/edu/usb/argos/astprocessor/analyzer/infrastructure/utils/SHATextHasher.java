package edu.usb.argos.astprocessor.analyzer.infrastructure.utils;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.IPlainTextHasher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
public class SHATextHasher implements IPlainTextHasher {

    private final String HASH_ALGORITHM = "SHA-256";
    private MessageDigest digest;

    public SHATextHasher() {
        initDigest();
    }

    @Override
    public String hash(String text) {
        byte[] hashBytes = hashAsBytes(text);

        return Base64.getEncoder().encodeToString(hashBytes);
    }

    @Override
    public byte[] hashAsBytes(String text) {
        return digest.digest(text.getBytes(StandardCharsets.UTF_8));
    }

    private void initDigest() {
        try {
            digest = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error creating hash for statement", e);
        }
    }
}
