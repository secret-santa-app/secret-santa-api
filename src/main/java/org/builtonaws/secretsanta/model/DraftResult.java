package org.builtonaws.secretsanta.model;

import java.time.Instant;
import java.util.Map;

public record DraftResult(long seed, Instant draftedAt, Map<String, String> assignments) {}
