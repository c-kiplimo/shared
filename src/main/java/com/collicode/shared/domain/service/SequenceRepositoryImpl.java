package com.collicode.shared.domain.service;

import com.collicode.shared.domain.Identity;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Repository
public class SequenceRepositoryImpl<T> implements SequenceRepository<T, Long, Long> {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public SequenceRepositoryImpl(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }


    @Override
    public Mono<Identity<T, Long, Long>> findPreviousId(String entity) {
        return r2dbcEntityTemplate.getDatabaseClient()
                .sql("SELECT value,factor FROM shared_sequence WHERE key = :entity")
                .bind("entity", entity)
                .map((row, rowMetadata) -> new Identity<T, Long, Long>(row.get("value",
                        Long.class), row.get("factor",
                        Long.class))).one();
    }

    @Override
    public Mono<Void> updateId(String entity, Long currentId) {

        return r2dbcEntityTemplate
                .getDatabaseClient()
                .sql("UPDATE shared_sequence set value=:value WHERE key =:key")
                .bind("value", currentId)
                .bind("key", entity).fetch()
                .rowsUpdated().then();
    }

    @Override
    public Mono<Void> insertId(String entity, Long currentId, Long factor) {

        return r2dbcEntityTemplate
                .getDatabaseClient()
                .sql(
                        "INSERT INTO shared_sequence (key,value,factor,created_at,updated_at) values (:key,:value,"
                                + ":factor,"
                                + ":created_at,:updated_at)")
                .bind("value", currentId)
                .bind("key", entity)
                .bind("factor", factor)
                .bind("created_at", LocalDateTime.now())
                .bind("updated_at", LocalDateTime.now())
                .fetch()
                .rowsUpdated().then();
    }

}
