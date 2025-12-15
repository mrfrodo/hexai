package com.frodo.hexai.domain.service;

import com.frodo.hexai.domain.model.KnowledgeItem;

import java.util.List;

public class MemoryEstimator {

    /**
     * Estimate the memory used by a list of KnowledgeItems in bytes.
     */
    public static long estimateMemoryUsage(List<KnowledgeItem> items) {
        long totalBytes = 0;

        for (KnowledgeItem item : items) {
            // Estimate String memory: 2 bytes per character
            totalBytes += item.getId().length() * 2;
            totalBytes += item.getContent().length() * 2;

            // Estimate float array memory: 4 bytes per float
            if (item.getEmbedding() != null) {
                totalBytes += item.getEmbedding().length * 4;
            }

            // Add object overhead (~16 bytes per object, rough estimate)
            totalBytes += 16;
        }

        return totalBytes;
    }

    /**
     * Utility to convert bytes to MB for readability
     */
    public static double bytesToMB(long bytes) {
        return bytes / (1024.0 * 1024.0);
    }
}
