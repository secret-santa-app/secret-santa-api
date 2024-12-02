package org.builtonaws.secretsanta.persistence;

import jakarta.inject.Inject;
import java.util.stream.Collectors;
import org.builtonaws.secretsanta.model.Draft;
import org.builtonaws.secretsanta.model.DraftParticipant;
import org.builtonaws.secretsanta.model.DraftResult;
import org.builtonaws.secretsanta.util.StringPairSet;

public class PersistenceMapper {
    @Inject
    public PersistenceMapper() {}

    public DraftEntity map(Draft draft) {
        var entity = new DraftEntity();
        entity.setDraftId(draft.draftId());
        entity.setVersion(draft.version());
        entity.setOwnerId(draft.ownerId());
        entity.setCreatedAt(draft.createdAt());
        entity.setUpdatedAt(draft.updatedAt());
        entity.setTitle(draft.title());
        entity.setDescription(draft.description());
        entity.setExchangeAt(draft.exchangeAt());
        var participants = draft.participants().stream()
                .collect(Collectors.toUnmodifiableMap(DraftParticipant::name, DraftParticipant::email));
        entity.setParticipants(participants);
        entity.setForbiddenPairs(draft.forbiddenPairs().serialized());
        if (draft.result() != null) {
            entity.setAssignments(draft.result().assignments());
            entity.setAssignmentSeed(draft.result().seed());
            entity.setAssignmentCreatedAt(draft.result().draftedAt());
        }
        return entity;
    }

    public Draft map(DraftEntity entity) {
        DraftResult result = null;
        if (entity.getAssignments() != null) {
            result = new DraftResult(
                    entity.getAssignmentSeed(), entity.getAssignmentCreatedAt(), entity.getAssignments());
        }

        return new Draft(
                entity.getDraftId(),
                entity.getVersion(),
                entity.getOwnerId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getExchangeAt(),
                entity.getParticipants().entrySet().stream()
                        .map(e -> new DraftParticipant(e.getKey(), e.getValue()))
                        .collect(Collectors.toUnmodifiableSet()),
                StringPairSet.ofSerialized(entity.getForbiddenPairs()),
                result);
    }
}
