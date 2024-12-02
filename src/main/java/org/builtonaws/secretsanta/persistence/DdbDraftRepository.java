package org.builtonaws.secretsanta.persistence;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.builtonaws.secretsanta.exception.NotFoundException;
import org.builtonaws.secretsanta.model.Draft;
import org.jspecify.annotations.NullUnmarked;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

@NullUnmarked
public class DdbDraftRepository implements DraftRepository {
    private static final TableSchema<DraftEntity> ENTITY_SCHEMA = TableSchema.fromBean(DraftEntity.class);

    @Inject
    public DdbDraftRepository(
            DynamoDbEnhancedClient client, @Named("table.name") String tableName, PersistenceMapper persistenceMapper) {
        this.table = client.table(tableName, ENTITY_SCHEMA);
        this.persistenceMapper = persistenceMapper;
    }

    private final DynamoDbTable<DraftEntity> table;
    private final PersistenceMapper persistenceMapper;

    @Override
    public void saveDraft(Draft draft) {
        var entity = persistenceMapper.map(draft);
        table.putItem(put -> put.item(entity)
                .conditionExpression(Expression.builder()
                        .expression("attribute_not_exists(draftId) OR attribute_not_exists(version)")
                        .build()));
    }

    @Override
    public Draft getDraft(String draftId) throws NotFoundException {
        var entities = table.query(req -> req.limit(1)
                .scanIndexForward(false)
                .queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(draftId))));
        return entities.items().stream()
                .limit(1)
                .map(persistenceMapper::map)
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
