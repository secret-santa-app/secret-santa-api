package org.builtonaws.secretsanta.persistence;

import org.builtonaws.secretsanta.exception.NotFoundException;
import org.builtonaws.secretsanta.model.Draft;

public interface DraftRepository {
    void saveDraft(Draft draft);

    Draft getDraft(String draftId) throws NotFoundException;
}
