package edu.usb.argos.astprocessor.analyzer.infrastructure.utils;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.IShingleGenerator;

import java.util.ArrayList;
import java.util.List;

public class TokenShingleGenerator implements IShingleGenerator<String> {

    @Override
    public List<List<String>> generate(List<String> tokens, int frequency) {
        List<List<String>> shingles = new ArrayList<>();

        for (int i = 0; i <= tokens.size() - frequency; i++) {
            List<String> shingle = tokens.subList(i, i + frequency);
            shingles.add(shingle);
        }

        return shingles;
    }
}
