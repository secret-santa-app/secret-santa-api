package org.builtonaws.secretsanta.persistence;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.NullUnmarked;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@NullUnmarked
@DynamoDbBean
public class DraftEntity {
    private String draftId;
    private int version;
    private String ownerId;
    private Instant createdAt;
    private Instant updatedAt;
    private String title;
    private String description;
    private Instant exchangeAt;
    private Map<String, String> participants;
    private List<List<String>> forbiddenPairs;
    private Map<String, String> assignments;
    private Long assignmentSeed;
    private Instant assignmentCreatedAt;

    public DraftEntity() {}

    public DraftEntity(
            String draftId,
            int version,
            String ownerId,
            Instant createdAt,
            Instant updatedAt,
            String title,
            String description,
            Instant exchangeAt,
            Map<String, String> participants,
            List<List<String>> forbiddenPairs) {
        this.draftId = draftId;
        this.version = version;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.description = description;
        this.exchangeAt = exchangeAt;
        this.participants = participants;
        this.forbiddenPairs = forbiddenPairs;
    }

    @DynamoDbPartitionKey
    public String getDraftId() {
        return draftId;
    }

    public void setDraftId(String draftId) {
        this.draftId = draftId;
    }

    @DynamoDbSortKey
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getExchangeAt() {
        return exchangeAt;
    }

    public void setExchangeAt(Instant exchangeAt) {
        this.exchangeAt = exchangeAt;
    }

    public Map<String, String> getParticipants() {
        return participants;
    }

    public void setParticipants(Map<String, String> participants) {
        this.participants = participants;
    }

    public List<List<String>> getForbiddenPairs() {
        return forbiddenPairs;
    }

    public void setForbiddenPairs(List<List<String>> forbiddenPairs) {
        this.forbiddenPairs = forbiddenPairs;
    }

    public Map<String, String> getAssignments() {
        return assignments;
    }

    public void setAssignments(Map<String, String> assignments) {
        this.assignments = assignments;
    }

    public Long getAssignmentSeed() {
        return assignmentSeed;
    }

    public void setAssignmentSeed(Long assignmentSeed) {
        this.assignmentSeed = assignmentSeed;
    }

    public Instant getAssignmentCreatedAt() {
        return assignmentCreatedAt;
    }

    public void setAssignmentCreatedAt(Instant assignmentCreatedAt) {
        this.assignmentCreatedAt = assignmentCreatedAt;
    }
}
