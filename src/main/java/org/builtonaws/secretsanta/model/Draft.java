package org.builtonaws.secretsanta.model;

import java.time.Instant;
import java.util.Set;
import org.builtonaws.secretsanta.util.StringPairSet;
import org.jspecify.annotations.Nullable;

public record Draft(
        String draftId,
        int version,
        String ownerId,
        Instant createdAt,
        Instant updatedAt,
        String title,
        @Nullable String description,
        Instant exchangeAt,
        Set<DraftParticipant> participants,
        StringPairSet forbiddenPairs,
        @Nullable DraftResult result) {}
