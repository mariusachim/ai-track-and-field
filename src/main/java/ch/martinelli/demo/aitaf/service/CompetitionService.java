package ch.martinelli.demo.aitaf.service;

import ch.martinelli.demo.aitaf.db.tables.records.CompetitionRecord;
import ch.martinelli.demo.aitaf.repository.CompetitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompetitionService {

    private final CompetitionRepository competitionRepository;

    public CompetitionService(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    @Transactional(readOnly = true)
    public List<CompetitionRecord> findAll() {
        return competitionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CompetitionRecord> findById(Long id) {
        return competitionRepository.findById(id);
    }

    public CompetitionRecord save(CompetitionRecord competition) {
        return competitionRepository.save(competition);
    }

    public void delete(Long id) {
        competitionRepository.delete(id);
    }
}
