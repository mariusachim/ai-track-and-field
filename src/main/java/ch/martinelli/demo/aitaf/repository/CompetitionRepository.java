package ch.martinelli.demo.aitaf.repository;

import ch.martinelli.demo.aitaf.db.tables.records.CompetitionRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static ch.martinelli.demo.aitaf.db.Tables.COMPETITION;

@Repository
public class CompetitionRepository {

    private final DSLContext dsl;

    public CompetitionRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<CompetitionRecord> findAll() {
        return dsl.selectFrom(COMPETITION)
                .orderBy(COMPETITION.COMPETITION_DATE.desc(), COMPETITION.NAME)
                .fetch();
    }

    public Optional<CompetitionRecord> findById(Long id) {
        return dsl.selectFrom(COMPETITION)
                .where(COMPETITION.ID.eq(id))
                .fetchOptional();
    }

    public CompetitionRecord save(CompetitionRecord record) {
        if (record.getId() == null) {
            return insert(record);
        } else {
            return update(record);
        }
    }

    private CompetitionRecord insert(CompetitionRecord record) {
        return dsl.insertInto(COMPETITION)
                .set(COMPETITION.NAME, record.getName())
                .set(COMPETITION.COMPETITION_DATE, record.getCompetitionDate())
                .set(COMPETITION.LOCATION, record.getLocation())
                .returning()
                .fetchOne();
    }

    private CompetitionRecord update(CompetitionRecord record) {
        return dsl.update(COMPETITION)
                .set(COMPETITION.NAME, record.getName())
                .set(COMPETITION.COMPETITION_DATE, record.getCompetitionDate())
                .set(COMPETITION.LOCATION, record.getLocation())
                .where(COMPETITION.ID.eq(record.getId()))
                .returning()
                .fetchOne();
    }

    public void delete(Long id) {
        dsl.deleteFrom(COMPETITION)
                .where(COMPETITION.ID.eq(id))
                .execute();
    }
}
