package com.frodo.hexai.infrastructure.embedding;

import com.frodo.hexai.domain.model.KnowledgeItem;
import com.frodo.hexai.domain.service.KnowledgeItemEmbeddingService;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Driven outbound adapter
 */
@Service
public class AzureEmbeddingAdapter implements KnowledgeItemEmbeddingService {
    private final EmbeddingModel embeddingModel;

    public AzureEmbeddingAdapter(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Override
    public List<KnowledgeItem> embedKnowledgeItems(List<String> lines) {
        List<float[]> embeddings = embeddingModel.embed(lines);
        List<KnowledgeItem> items = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            items.add(new KnowledgeItem(lines.get(i), embeddings.get(i)));
        }
        return items;
    }
}
