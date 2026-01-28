package ch.martinelli.demo.aitaf.competition.service;

import ch.martinelli.demo.aitaf.db.tables.records.CompetitionRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.martinelli.demo.aitaf.db.Tables.COMPETITION;

@Service
@Transactional
public class CompetitionService {

    private final DSLContext dslContext;

    public CompetitionService(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public List<CompetitionRecord> findAll() {
        return dslContext.selectFrom(COMPETITION)
                .orderBy(COMPETITION.COMPETITION_DATE.desc())
                .fetchInto(CompetitionRecord.class);
    }

    public CompetitionRecord findById(Long id) {
        return dslContext.selectFrom(COMPETITION)
                .where(COMPETITION.ID.eq(id))
                .fetchOneInto(CompetitionRecord.class);
    }

    public CompetitionRecord save(CompetitionRecord competition) {
        if (competition.getId() == null) {
            var record = dslContext.newRecord(COMPETITION, competition);
            record.store();
            return record;
        } else {
            var existing = dslContext.selectFrom(COMPETITION)
                    .where(COMPETITION.ID.eq(competition.getId()))
                    .fetchOne();
            if (existing != null) {
                existing.setName(competition.getName());
                existing.setCompetitionDate(competition.getCompetitionDate());
                existing.setLocation(competition.getLocation());
                existing.store();
                return existing;
            }
            return null;
        }
    }

    public void delete(Long id) {
        dslContext.deleteFrom(COMPETITION)
                .where(COMPETITION.ID.eq(id))
                .execute();
    }

    public boolean hasAthletes(Long competitionId) {
        int count = dslContext.selectCount()
                .from(ch.martinelli.demo.aitaf.db.Tables.ATHLETE_COMPETITION)
                .where(ch.martinelli.demo.aitaf.db.Tables.ATHLETE_COMPETITION.COMPETITION_ID.eq(competitionId))
                .fetchOne(0, int.class);
        return count > 0;
    }
}
