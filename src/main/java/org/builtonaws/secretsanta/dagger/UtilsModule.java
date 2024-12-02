package org.builtonaws.secretsanta.dagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dagger.Module;
import dagger.Provides;
import java.security.SecureRandom;
import java.time.Clock;
import java.util.Random;

@Module
public class UtilsModule {
    @Provides
    public Clock provideClock() {
        return Clock.systemUTC();
    }

    @Provides
    public ObjectMapper provideObjectMapper() {
        return new ObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Provides
    public Random provideRandom() {
        return new SecureRandom();
    }
}
