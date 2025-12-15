package com.frodo.hexai.infrastructure.web;

import com.frodo.hexai.domain.model.KnowledgeItem;
import com.frodo.hexai.domain.service.KnowledgeItemEmbeddingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Driving inbound adapter
 */

@RestController
public class EmbeddingsController {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingsController.class);
    private final KnowledgeItemEmbeddingService embeddingService;

    public EmbeddingsController(KnowledgeItemEmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isBlank()) lines.add(line);
            }

            if (lines.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Uploaded file contained no valid lines.");
            }

            // Call domain service → outbound adapter handles embedding
            long start = System.currentTimeMillis();
            List<KnowledgeItem> items = embeddingService.embedItems(lines);
            log.info("Embedding {} items completed in {} ms", items.size(),
                    System.currentTimeMillis() - start);

            logItems(items);

            return ResponseEntity.ok(
                    "Uploaded and embedded " + items.size() + " items successfully!");

        } catch (Exception e) {
            log.error("Failed to process uploaded file", e);
            return ResponseEntity.status(500)
                    .body("Failed to process file: " + e.getMessage());
        }
    }

    private void logItems(List<KnowledgeItem> items) {
        if (log.isInfoEnabled()) {
            for (KnowledgeItem item : items) {
                log.info("id={}, embeddingDimensions={}, content={}",
                        item.getId(),
                        item.getEmbedding().length,
                        item.getContent());
            }
        }
    }

}
