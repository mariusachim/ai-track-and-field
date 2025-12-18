-- AI Track and Field - Initial Schema
-- Creates all core entities for managing track and field competitions

-- ============================================================================
-- Sequences for Primary Keys
-- ============================================================================

CREATE SEQUENCE competition_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE category_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE event_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE category_event_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE club_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE athlete_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE result_seq START WITH 1 INCREMENT BY 1;

-- ============================================================================
-- Competition Table
-- ============================================================================

CREATE TABLE competition (
    id BIGINT PRIMARY KEY DEFAULT nextval('competition_seq'),
    name VARCHAR(255) NOT NULL,
    competition_date DATE NOT NULL,
    location VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_competition_date ON competition(competition_date);

-- ============================================================================
-- Category Table
-- ============================================================================

CREATE TABLE category (
    id BIGINT PRIMARY KEY DEFAULT nextval('category_seq'),
    competition_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('MALE', 'FEMALE', 'MIXED')),
    year_from INT NOT NULL,
    year_to INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_category_competition FOREIGN KEY (competition_id) REFERENCES competition(id) ON DELETE CASCADE,
    CONSTRAINT chk_year_range CHECK (year_to >= year_from)
);

CREATE INDEX idx_category_competition ON category(competition_id);
CREATE INDEX idx_category_gender_year ON category(gender, year_from, year_to);

-- ============================================================================
-- Event Table
-- ============================================================================

CREATE TABLE event (
    id BIGINT PRIMARY KEY DEFAULT nextval('event_seq'),
    name VARCHAR(100) NOT NULL,
    event_type VARCHAR(50) NOT NULL CHECK (event_type IN ('TRACK', 'FIELD', 'COMBINED')),
    unit VARCHAR(20) NOT NULL CHECK (unit IN ('SECONDS', 'METERS', 'CENTIMETERS', 'POINTS')),
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_event_name UNIQUE (name)
);

CREATE INDEX idx_event_type ON event(event_type);

-- ============================================================================
-- CategoryEvent Table (Junction Table)
-- ============================================================================

CREATE TABLE category_event (
    id BIGINT PRIMARY KEY DEFAULT nextval('category_event_seq'),
    category_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_category_event_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
    CONSTRAINT fk_category_event_event FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
    CONSTRAINT uq_category_event UNIQUE (category_id, event_id)
);

CREATE INDEX idx_category_event_category ON category_event(category_id);
CREATE INDEX idx_category_event_event ON category_event(event_id);

-- ============================================================================
-- Club Table
-- ============================================================================

CREATE TABLE club (
    id BIGINT PRIMARY KEY DEFAULT nextval('club_seq'),
    name VARCHAR(255) NOT NULL,
    abbreviation VARCHAR(20),
    city VARCHAR(100),
    country VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_club_name UNIQUE (name)
);

CREATE INDEX idx_club_name ON club(name);

-- ============================================================================
-- Athlete Table
-- ============================================================================

CREATE TABLE athlete (
    id BIGINT PRIMARY KEY DEFAULT nextval('athlete_seq'),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birth_year INT NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('MALE', 'FEMALE')),
    club_id BIGINT,
    category_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_athlete_club FOREIGN KEY (club_id) REFERENCES club(id) ON DELETE SET NULL,
    CONSTRAINT fk_athlete_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL,
    CONSTRAINT chk_birth_year CHECK (birth_year >= 1900 AND birth_year <= EXTRACT(YEAR FROM CURRENT_DATE))
);

CREATE INDEX idx_athlete_name ON athlete(last_name, first_name);
CREATE INDEX idx_athlete_club ON athlete(club_id);
CREATE INDEX idx_athlete_category ON athlete(category_id);
CREATE INDEX idx_athlete_birth_year_gender ON athlete(birth_year, gender);

-- ============================================================================
-- Result Table
-- ============================================================================

CREATE TABLE result (
    id BIGINT PRIMARY KEY DEFAULT nextval('result_seq'),
    athlete_id BIGINT NOT NULL,
    category_event_id BIGINT NOT NULL,
    performance_value DECIMAL(10, 3) NOT NULL,
    points INT DEFAULT 0,
    rank INT,
    notes TEXT,
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_result_athlete FOREIGN KEY (athlete_id) REFERENCES athlete(id) ON DELETE CASCADE,
    CONSTRAINT fk_result_category_event FOREIGN KEY (category_event_id) REFERENCES category_event(id) ON DELETE CASCADE,
    CONSTRAINT uq_athlete_category_event UNIQUE (athlete_id, category_event_id),
    CONSTRAINT chk_performance_value CHECK (performance_value >= 0),
    CONSTRAINT chk_points CHECK (points >= 0)
);

CREATE INDEX idx_result_athlete ON result(athlete_id);
CREATE INDEX idx_result_category_event ON result(category_event_id);
CREATE INDEX idx_result_points ON result(points DESC);
CREATE INDEX idx_result_rank ON result(rank);

-- ============================================================================
-- Comments for Documentation
-- ============================================================================

COMMENT ON TABLE competition IS 'Track and field competition events';
COMMENT ON TABLE category IS 'Athlete groupings based on gender and birth year ranges';
COMMENT ON TABLE event IS 'Track or field events (e.g., 100m sprint, high jump)';
COMMENT ON TABLE category_event IS 'Junction table linking categories to events';
COMMENT ON TABLE club IS 'Athletic clubs or organizations';
COMMENT ON TABLE athlete IS 'Individual athletes participating in competitions';
COMMENT ON TABLE result IS 'Athlete performance records with calculated points';

COMMENT ON COLUMN category.gender IS 'Gender category: MALE, FEMALE, or MIXED';
COMMENT ON COLUMN category.year_from IS 'Starting birth year for category (inclusive)';
COMMENT ON COLUMN category.year_to IS 'Ending birth year for category (inclusive)';
COMMENT ON COLUMN event.event_type IS 'Type of event: TRACK, FIELD, or COMBINED';
COMMENT ON COLUMN event.unit IS 'Performance measurement unit: SECONDS, METERS, CENTIMETERS, or POINTS';
COMMENT ON COLUMN result.performance_value IS 'Raw performance value in the event unit';
COMMENT ON COLUMN result.points IS 'Calculated points based on IAAF ranking formulas';
COMMENT ON COLUMN result.rank IS 'Athlete rank within their category for this event';
