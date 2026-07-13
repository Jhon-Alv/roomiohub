package com.roomiohub.backend.shared.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class SseService {

    private final Map<UUID, List<SseEmitter>> emittersMap = new ConcurrentHashMap<>();

    public SseEmitter subscribe(UUID key) {
        SseEmitter emitter = new SseEmitter(3600000L);
        emittersMap.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(emitter);
        emitter.onCompletion(() -> remove(key, emitter));
        emitter.onTimeout(() -> remove(key, emitter));
        emitter.onError(e -> remove(key, emitter));
        return emitter;
    }

    public void publish(UUID key, String eventName, Object data) {
        List<SseEmitter> emitters = emittersMap.get(key);
        log.info("SSE publish: key={}, event={}, emitters={}", key, eventName,
                emitters != null ? emitters.size() : 0);
        if (emitters != null) {
            List<SseEmitter> dead = new ArrayList<>();
            emitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().name(eventName).data(data));
                } catch (IOException e) {
                    dead.add(emitter);
                }
            });
            emitters.removeAll(dead);
        }
    }

    public void schedulePublishAfterCommit(UUID key, String eventName, Object data) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publish(key, eventName, data);
            }
        });
    }

    private void remove(UUID key, SseEmitter emitter) {
        List<SseEmitter> emitters = emittersMap.get(key);
        if (emitters != null) {
            emitters.remove(emitter);
        }
    }
}
