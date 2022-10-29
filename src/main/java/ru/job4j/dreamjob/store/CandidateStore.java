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
    private final AtomicInteger id = new AtomicInteger(3);

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Petr Vasiliev", "Petr Description"));
        candidates.put(2, new Candidate(2, "Vasiliy Sokolov", "Vasiliy Description"));
        candidates.put(3, new Candidate(3, "Dmitriy Smirnov", "Dmitriy Description"));
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
        candidates.replace(candidate.getId(), candidate);
    }
}
