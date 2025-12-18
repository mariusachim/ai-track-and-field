package ch.martinelli.demo.aitaf.repository;

import ch.martinelli.demo.aitaf.db.tables.records.CompetitionRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ch.martinelli.demo.aitaf.db.Tables.COMPETITION;

@Repository
@Transactional
public class CompetitionRepository {

    private final DSLContext dslContext;

    public CompetitionRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public CompetitionRecord create(String name, LocalDate competitionDate,
                                    String location, String description) {
        return dslContext
                .insertInto(COMPETITION)
                .columns(
                        COMPETITION.NAME,
                        COMPETITION.COMPETITION_DATE,
                        COMPETITION.LOCATION,
                        COMPETITION.DESCRIPTION
                )
                .values(name, competitionDate, location, description)
                .returning()
                .fetchOne();
    }

    public Optional<CompetitionRecord> findById(Integer id) {
        return dslContext
                .selectFrom(COMPETITION)
                .where(COMPETITION.ID.eq(id))
                .fetchOptional();
    }

    public List<CompetitionRecord> findAll() {
        return dslContext
                .selectFrom(COMPETITION)
                .orderBy(COMPETITION.COMPETITION_DATE.desc())
                .fetch();
    }

    public CompetitionRecord update(Integer id, String name, LocalDate competitionDate,
                                    String location, String description) {
        dslContext
                .update(COMPETITION)
                .set(COMPETITION.NAME, name)
                .set(COMPETITION.COMPETITION_DATE, competitionDate)
                .set(COMPETITION.LOCATION, location)
                .set(COMPETITION.DESCRIPTION, description)
                .set(COMPETITION.UPDATED_AT, LocalDateTime.now())
                .where(COMPETITION.ID.eq(id))
                .execute();

        return findById(id).orElse(null);
    }

    public int deleteById(Integer id) {
        return dslContext
                .deleteFrom(COMPETITION)
                .where(COMPETITION.ID.eq(id))
                .execute();
    }
}
