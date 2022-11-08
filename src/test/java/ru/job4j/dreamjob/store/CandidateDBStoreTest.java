package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CandidateDBStoreTest {
    @Test
    public void whenCreateCandidate() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Candidate name", "Description", new City(1, "Moscow"));
        store.addCandidate(candidate);
        Candidate candidateInDB = store.findById(candidate.getId());
        assertThat(candidateInDB.getName(), is(candidate.getName()));
        assertThat(candidateInDB.getDescription(), is(candidate.getDescription()));
        assertThat(candidateInDB.getCity().getId(), is(candidate.getCity().getId()));
    }

    @Test
    public void whenUpdateCandidate() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Candidate name", "Description", new City(1, "Moscow"));
        store.addCandidate(candidate);
        candidate.setName("New Name");
        store.update(candidate);
        Candidate candidateInDB = store.findById(candidate.getId());
        assertThat(candidateInDB.getName(), is(candidate.getName()));
    }

    @Test
    public void whenFindAllCandidate() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate1 = new Candidate(0, "Candidate name1", "Description1", new City(1, "Moscow"));
        Candidate candidate2 = new Candidate(0, "Candidate name2", "Description2", new City(1, "Moscow"));
        Candidate candidate3 = new Candidate(0, "Candidate name3", "Description3", new City(1, "Moscow"));
        List<Candidate> listCandidatesBefore = store.findAll();
        store.addCandidate(candidate1);
        store.addCandidate(candidate2);
        store.addCandidate(candidate3);
        List<Candidate> listCandidatesAfter = store.findAll();
        assertThat(listCandidatesAfter.size() - listCandidatesBefore.size(), is(3));
    }
}