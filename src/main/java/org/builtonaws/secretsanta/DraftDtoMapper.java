package org.builtonaws.secretsanta;

import jakarta.inject.Inject;
import java.time.Clock;
import java.util.UUID;
import java.util.stream.Collectors;
import org.builtonaws.secretsanta.dto.NewDraft;
import org.builtonaws.secretsanta.exception.BadRequestException;
import org.builtonaws.secretsanta.model.Draft;
import org.builtonaws.secretsanta.model.DraftParticipant;

public class DraftDtoMapper {
    private final Clock clock;

    @Inject
    public DraftDtoMapper(Clock clock) {
        this.clock = clock;
    }

    public Draft mapNewDraft(NewDraft dto, String ownerId) throws BadRequestException {
        var uniqueParticipantNames =
                dto.participants().stream().map(DraftParticipant::name).collect(Collectors.toUnmodifiableSet());
        if (uniqueParticipantNames.size() != dto.participants().size()) {
            throw new BadRequestException("Participants must have unique names");
        }
        for (var e : dto.forbiddenPairs().entrySet()) {
            if (!uniqueParticipantNames.contains(e.getKey()) || !uniqueParticipantNames.contains(e.getValue())) {
                throw new BadRequestException("Forbidden pair contains unknown participant: " + e);
            }
        }
        var now = clock.instant();
        return new Draft(
                UUID.randomUUID().toString(),
                1,
                ownerId,
                now,
                now,
                dto.title(),
                dto.description(),
                dto.exchangeAt(),
                dto.participants(),
                dto.forbiddenPairs(),
                null);
    }

    public Draft mapUpdatedDraft(Draft draft, NewDraft dto) throws BadRequestException {
        var uniqueParticipantNames =
                dto.participants().stream().map(DraftParticipant::name).collect(Collectors.toUnmodifiableSet());
        if (uniqueParticipantNames.size() != dto.participants().size()) {
            throw new BadRequestException("Participants must have unique names");
        }
        for (var e : dto.forbiddenPairs().entrySet()) {
            if (!uniqueParticipantNames.contains(e.getKey()) || !uniqueParticipantNames.contains(e.getValue())) {
                throw new BadRequestException("Forbidden pair contains unknown participant: " + e);
            }
        }
        return new Draft(
                draft.draftId(),
                draft.version() + 1,
                draft.ownerId(),
                draft.createdAt(),
                clock.instant(),
                dto.title(),
                dto.description(),
                dto.exchangeAt(),
                dto.participants(),
                dto.forbiddenPairs(),
                null);
    }
}
