package com.frodo.hexai.domain.service;

import com.frodo.hexai.domain.model.KnowledgeItem;

import java.util.List;

public interface KnowledgeItemEmbeddingService {
    List<KnowledgeItem> embedKnowledgeItems(List<String> lines);
}