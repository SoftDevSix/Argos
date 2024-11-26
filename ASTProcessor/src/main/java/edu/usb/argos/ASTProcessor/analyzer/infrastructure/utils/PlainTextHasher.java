package edu.usb.argos.ASTProcessor.analyzer.infrastructure.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
public class PlainTextHasher {

    private final String HASH_ALGORITHM = "SHA-256";
    private MessageDigest digest;

    public PlainTextHasher() {
        initDigest();
    }

    public String hash(String text) {
        byte[] hashBytes = digest.digest(text.getBytes());

        return Base64.getEncoder().encodeToString(hashBytes);
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
