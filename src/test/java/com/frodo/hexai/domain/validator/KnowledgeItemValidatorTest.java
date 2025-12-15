package com.frodo.hexai.domain.validator;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KnowledgeItemValidatorTest {

    private final KnowledgeItemValidator validator = new KnowledgeItemValidator();

    @Test
    void filterValidLines_removesBlankLines() {
        List<String> rawLines = List.of(
                "  First line  ",
                "   ",
                "Second line",
                "",
                "\tThird line"
        );

        List<String> validLines = validator.filterValidLines(rawLines);

        assertEquals(3, validLines.size());
        assertEquals("First line", validLines.get(0));
        assertEquals("Second line", validLines.get(1));
        assertEquals("Third line", validLines.get(2));
    }
}
