package com.frodo.hexai.domain.validator;

import java.util.List;
import java.util.stream.Collectors;

public class KnowledgeItemValidator {

    /**
     * Remove blank or whitespace-only lines before creating KnowledgeItems.
     */
    public List<String> filterValidLines(List<String> lines) {
        return lines.stream()
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .collect(Collectors.toList());
    }
}
