package org.builtonaws.secretsanta;

import jakarta.inject.Inject;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.builtonaws.secretsanta.model.Draft;
import org.builtonaws.secretsanta.model.DraftParticipant;
import org.builtonaws.secretsanta.model.DraftResult;

public class DraftAssigner {
    private final Random seeder;
    private final Clock clock;

    @Inject
    public DraftAssigner(Random seeder, Clock clock) {
        this.seeder = seeder;
        this.clock = clock;
    }

    public Draft assign(Draft draft) {
        var draftedAt = clock.instant();
        var seed = seeder.nextLong();
        var random = new Random(seed);

        var givers = draft.participants().stream().map(DraftParticipant::name).toList();
        var receivers = new ArrayList<>(givers);

        boolean containsForbiddenPairs;
        do {
            Collections.shuffle(receivers, random);
            containsForbiddenPairs = false;
            for (int i = 0; i < givers.size(); i++) {
                var giver = givers.get(i);
                var receiver = receivers.get(i);
                if (giver.equals(receiver) || draft.forbiddenPairs().contains(giver, receiver)) {
                    containsForbiddenPairs = true;
                    break;
                }
            }
        } while (containsForbiddenPairs);

        var assignments = IntStream.range(0, givers.size())
                .mapToObj(i -> Map.entry(givers.get(i), receivers.get(i)))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

        return new Draft(
                draft.draftId(),
                draft.version() + 1,
                draft.ownerId(),
                draft.createdAt(),
                draftedAt,
                draft.title(),
                draft.description(),
                draft.exchangeAt(),
                draft.participants(),
                draft.forbiddenPairs(),
                new DraftResult(seed, draftedAt, assignments));
    }
}
