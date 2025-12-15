package com.frodo.hexai.infrastructure.web;

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

@RestController
public class EmbeddingsController {

    private static final Logger log =
            LoggerFactory.getLogger(EmbeddingsController.class);
    private final List<Map<String, Object>> embeddedTips = new ArrayList<>();
    private final EmbeddingModel embeddingModel;

    public EmbeddingsController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadTips(@RequestParam("file") MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            List<String> lines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isBlank()) {
                    lines.add(line);
                }
            }

            // 🚀 ONE Azure API call instead of N
            long start = System.currentTimeMillis();
            List<float[]> embeddings = embeddingModel.embed(lines);
            log.info("\n\n_____Embedding {} tips completed in {} ms\n\n", lines.size(), System.currentTimeMillis() - start);

            for (int i = 0; i < lines.size(); i++) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("id", "tip-" + i);
                entry.put("content", lines.get(i));
                entry.put("embedding", embeddings.get(i));

                embeddedTips.add(entry);
            }

            logEmbeddedTips(embeddedTips);

            return ResponseEntity.ok(
                    "\n\nUploaded and embedded " + lines.size() + " tips successfully!\n");

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Failed to process file: " + e.getMessage());
        }
    }

    private void logEmbeddedTips(List<Map<String, Object>> embeddedTips) {
        if (log.isInfoEnabled()) {
            log.info("\n\n");
            for (Map<String, Object> tip : embeddedTips) {
                float[] embedding = (float[]) tip.get("embedding");
                log.info("id={}, embeddingDimensions={}, content={}",
                        tip.get("id"),
                        embedding.length,
                        tip.get("content"));
            }
        }
    }

}
