package org.builtonaws.secretsanta.dagger;

import dagger.Module;
import dagger.Provides;
import jakarta.inject.Named;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@Module
public class DatabaseModule {
    @Provides
    public DynamoDbEnhancedClient provideDdbClient() {
        return DynamoDbEnhancedClient.create();
    }

    @Provides
    @Named("table.name")
    public String provideTableName() {
        return System.getenv("TABLE_NAME");
    }
}
