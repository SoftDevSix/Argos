package edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.IShingleGenerator;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TokenShingleGenerator implements IShingleGenerator<String> {

    private final int frequency;

    @Override
    public List<List<String>> generate(List<String> tokens) {
        List<List<String>> shingles = new ArrayList<>();

        for (int i = 0; i <= tokens.size() - frequency; i++) {
            List<String> shingle = tokens.subList(i, i + frequency);
            shingles.add(shingle);
        }

        return shingles;
    }

    @Override
    public List<String> flatSingles(List<List<String>> singles) {
        return singles.stream()
                .map(shingle -> String.join("", shingle))
                .toList();
    }
}
