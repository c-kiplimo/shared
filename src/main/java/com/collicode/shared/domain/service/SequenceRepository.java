package com.collicode.shared.domain.service;


import com.collicode.shared.domain.Identity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SequenceRepository<R, T, O> {

    Mono<Identity<R, T, O>> findPreviousId(String entity);

    Mono<Void> updateId(String entity, Long currentId);

    Mono<Void> insertId(String entity, Long currentId, Long factor);

}
