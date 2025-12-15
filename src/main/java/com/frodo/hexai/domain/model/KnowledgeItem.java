package com.frodo.hexai.domain.model;

import java.util.UUID;

public class KnowledgeItem {
    private final String id;
    private final String content;
    private final float[] embedding;

    public KnowledgeItem(String content, float[] embedding) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.embedding = embedding;
    }

    public String getId() { return id; }
    public String getContent() { return content; }
    public float[] getEmbedding() { return embedding; }
}
