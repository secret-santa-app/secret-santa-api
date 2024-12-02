package org.builtonaws.secretsanta.dagger;

import dagger.Binds;
import dagger.Module;
import org.builtonaws.secretsanta.persistence.DdbDraftRepository;
import org.builtonaws.secretsanta.persistence.DraftRepository;

@Module
public abstract class PersistenceModule {
    @Binds
    public abstract DraftRepository bindRepo(DdbDraftRepository wtv);
}
