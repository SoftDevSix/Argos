package edu.usb.argos.astprocessor.analyzer.codeSmells.utils;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.IPlainTextHasher;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.SHATextHasher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SHATextHasherTest {

    private static IPlainTextHasher plainTextHasher;

    @BeforeAll
    public static void setupAll() {
        plainTextHasher = new SHATextHasher();
    }

    @Test
    void hashShouldReturnValidSHA256Hash() {
        String text = "hello";
        String hash = plainTextHasher.hash(text);

        assertNotNull(hash);
        assertEquals(44, hash.length());
    }

    @Test
    void hashAsBytesShouldReturnCorrectLengthForSHA256() {
        String text = "world";
        byte[] hashBytes = plainTextHasher.hashAsBytes(text);

        assertNotNull(hashBytes);
        assertEquals(32, hashBytes.length);
    }

    @Test
    void hashShouldBeConsistentForSameInput() {
        String text = "consistent";

        String hash1 = plainTextHasher.hash(text);
        String hash2 = plainTextHasher.hash(text);

        assertEquals(hash1, hash2);
    }

    @Test
    void differentInputsShouldProduceDifferentHashes() {
        String hash1 = plainTextHasher.hash("input1");
        String hash2 = plainTextHasher.hash("input2");

        assertNotEquals(hash1, hash2);
    }
}
