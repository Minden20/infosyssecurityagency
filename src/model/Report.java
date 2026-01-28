package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Report {
    private UUID id;
    private UUID authorId; 
    private String content;
    private LocalDateTime createdAt;

    public Report() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

    public Report(UUID id, UUID authorId, String content, LocalDateTime createdAt) {
        this.id = id != null ? id : UUID.randomUUID();
        this.authorId = authorId;
        this.content = content;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getAuthorId() { return authorId; }
    public void setAuthorId(UUID authorId) { this.authorId = authorId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "[" + createdAt + "] " + content;
    }
}
