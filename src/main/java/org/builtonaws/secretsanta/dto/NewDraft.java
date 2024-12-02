package org.builtonaws.secretsanta.dto;

import java.time.Instant;
import java.util.Set;
import org.builtonaws.secretsanta.model.DraftParticipant;
import org.builtonaws.secretsanta.util.StringPairSet;
import org.jspecify.annotations.Nullable;

public record NewDraft(
        String title,
        @Nullable String description,
        Instant exchangeAt,
        Set<DraftParticipant> participants,
        StringPairSet forbiddenPairs) {}
