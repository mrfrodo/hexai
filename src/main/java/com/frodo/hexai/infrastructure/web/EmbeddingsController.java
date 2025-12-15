package com.frodo.hexai.infrastructure.web;

import com.frodo.hexai.domain.model.KnowledgeItem;
import com.frodo.hexai.domain.service.KnowledgeItemEmbeddingService;
import com.frodo.hexai.domain.service.MemoryEstimator;
import com.frodo.hexai.domain.validator.KnowledgeItemValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Driving inbound adapter
 */

@RestController
public class EmbeddingsController {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingsController.class);
    private final KnowledgeItemEmbeddingService embeddingService;
    private final KnowledgeItemValidator validator = new KnowledgeItemValidator();
    // In-memory storage for demonstration purposes
    private final List<KnowledgeItem> embeddedItems = new ArrayList<>();

    public EmbeddingsController(KnowledgeItemEmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            // read raw lines from file
            List<String> rawLines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                rawLines.add(line);
            }

            // Use domain validator BEFORE calling business logic
            List<String> validLines = validator.filterValidLines(rawLines);

            if (validLines.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Uploaded file contained no valid lines.");
            }

            // Call domain service (business logic)
            long start = System.currentTimeMillis();
            List<KnowledgeItem> items = embeddingService.embedKnowledgeItems(validLines);
            long duration = System.currentTimeMillis() - start;
            log.info("________________create embeddings took {} ms. _______", duration);

            // Store in memory
            embeddedItems.addAll(items);

            // Log items
            logItems(items);

            // Log memory usage
            logMemoryUsage();

            // Log or return response
            log.info("Embedded {} knowledge items successfully.", items.size());
            return ResponseEntity.ok("Uploaded and embedded " + items.size() + " items successfully!");

        } catch (Exception e) {
            log.error("Failed to process uploaded file", e);
            return ResponseEntity.status(500)
                    .body("Failed to process file: " + e.getMessage());
        }
    }

    /**
     * New endpoint: estimates memory usage of all embedded KnowledgeItems
     */
    @GetMapping("/memory-usage")
    public ResponseEntity<String> memoryUsage() {
        long bytes = MemoryEstimator.estimateMemoryUsage(embeddedItems);
        double mb = MemoryEstimator.bytesToMB(bytes);
        log.info("Current memory footprint of KnowledgeItems: {} MB", mb);
        return ResponseEntity.ok(String.format("Estimated memory usage: %.2f MB", mb));
    }

    private void logItems(List<KnowledgeItem> items) {
        if (log.isInfoEnabled()) {
            for (KnowledgeItem item : items) {
                /**
                log.info("id={}, embeddingDimensions={}, content={}",
                        item.getId(),
                        item.getEmbedding().length,
                        item.getContent()); */
            }
        }
    }

    /**
     * Logs total memory usage.
     */
    private void logMemoryUsage() {
        long bytes = MemoryEstimator.estimateMemoryUsage(embeddedItems);
        double mb = MemoryEstimator.bytesToMB(bytes);
        log.info("Total in-memory KnowledgeItems: {} items, estimated {} MB", embeddedItems.size(), trunc(mb, 3));
    }

    private double trunc(double value, int sigFigures) {
        if (value == 0) return 0;
        final double d = Math.floor(Math.log10(Math.abs(value)));
        final double power = sigFigures - 1 - d;
        final double magnitude = Math.pow(10, power);
        // Truncate by casting to long
        return Math.floor(value * magnitude) / magnitude;
    }

}
