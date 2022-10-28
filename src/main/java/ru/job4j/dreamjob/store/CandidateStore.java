package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CandidateStore {
    private static final CandidateStore INST = new CandidateStore();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    private CandidateStore() {
        candidates.put(1, new Candidate(id.incrementAndGet(), "Petr Vasiliev", "Petr Description"));
        candidates.put(2, new Candidate(id.incrementAndGet(), "Vasiliy Sokolov", "Vasiliy Description"));
        candidates.put(3, new Candidate(id.incrementAndGet(), "Dmitriy Smirnov", "Dmitriy Description"));
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public void addCandidate(Candidate candidate) {
        int candidateId = id.incrementAndGet();
        candidate.setId(candidateId);
        candidate.setCreated(LocalDateTime.now());
        candidates.putIfAbsent(candidateId, candidate);
    }

    public Candidate findById(int id) {
       return candidates.get(id);
    }

    public void update(Candidate candidate) {
        candidate.setCreated(LocalDateTime.now());
        candidates.replace(candidate.getId(), candidate);
    }
}
