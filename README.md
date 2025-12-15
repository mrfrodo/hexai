# Hexagonal architecture service
### Powered by
* spring boot ai and rag
* postgresql with a vectordatabase (ragdb with pgvector)
  * docker pull pgvector/pgvector:pg16
  * docker run -d --name pgvector -e POSTGRES_PASSWORD=postgres -p 5432:5432 pgvector/pgvector:pg16
  * docker exec -it pgvector psql -U postgres
    * while inside run
      * CREATE DATABASE ragdb;
      * \c ragdb;
      * CREATE EXTENSION vector;
* The database is now ready for the spring boot app and it can create tables with VECTOR columns and store embeddings



.

📌 Use Case: Knowledge Asset Embedding Service

The app ingests textual knowledge assets (e.g., documents, manuals, policies), transforms them into embeddings.  
Then stores them so they can later be used for semantic search, RAG, or recommendations.

Project is structured in domain and infrastructure root folders. 
All core business is in domain package. Everything else in infrastructure.

There is an anti corruption layer between the domain the infrastructure.  
  ➜ Zero leakage of external concepts into the core domain

Inbound ports: generateEmbeddings interface  
Outbound ports: createEmbeddings  interface


