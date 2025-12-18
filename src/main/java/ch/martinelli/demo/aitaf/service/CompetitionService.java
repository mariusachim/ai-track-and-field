package ch.martinelli.demo.aitaf.service;

import ch.martinelli.demo.aitaf.db.tables.records.CompetitionRecord;
import ch.martinelli.demo.aitaf.repository.CompetitionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompetitionService {

    private final CompetitionRepository repository;

    public CompetitionService(CompetitionRepository repository) {
        this.repository = repository;
    }

    public List<CompetitionRecord> findAll() {
        return repository.findAll();
    }

    public Optional<CompetitionRecord> findById(Integer id) {
        return repository.findById(id);
    }

    public CompetitionRecord create(CompetitionRecord competition) {
        return repository.create(
                competition.getName(),
                competition.getCompetitionDate(),
                competition.getLocation(),
                competition.getDescription()
        );
    }

    public CompetitionRecord update(CompetitionRecord competition) {
        return repository.update(
                competition.getId(),
                competition.getName(),
                competition.getCompetitionDate(),
                competition.getLocation(),
                competition.getDescription()
        );
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
