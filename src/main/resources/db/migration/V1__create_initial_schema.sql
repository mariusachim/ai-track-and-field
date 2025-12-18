-- Create sequences for primary keys
CREATE SEQUENCE competition_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE category_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE event_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE club_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE athlete_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE result_seq START WITH 1 INCREMENT BY 1;

-- Competition table
CREATE TABLE competition (
    id BIGINT PRIMARY KEY DEFAULT nextval('competition_seq'),
    name VARCHAR(255) NOT NULL,
    competition_date DATE NOT NULL,
    location VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Club table
CREATE TABLE club (
    id BIGINT PRIMARY KEY DEFAULT nextval('club_seq'),
    name VARCHAR(255) NOT NULL,
    abbreviation VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Category table
CREATE TABLE category (
    id BIGINT PRIMARY KEY DEFAULT nextval('category_seq'),
    competition_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('MALE', 'FEMALE', 'MIXED')),
    year_from INTEGER NOT NULL,
    year_to INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_category_competition FOREIGN KEY (competition_id) REFERENCES competition(id) ON DELETE CASCADE,
    CONSTRAINT chk_year_range CHECK (year_from <= year_to)
);

-- Event table
CREATE TABLE event (
    id BIGINT PRIMARY KEY DEFAULT nextval('event_seq'),
    category_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    unit VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_event_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);

-- Athlete table
CREATE TABLE athlete (
    id BIGINT PRIMARY KEY DEFAULT nextval('athlete_seq'),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_year INTEGER NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('MALE', 'FEMALE')),
    club_id BIGINT,
    category_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_athlete_club FOREIGN KEY (club_id) REFERENCES club(id) ON DELETE SET NULL,
    CONSTRAINT fk_athlete_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL
);

-- Result table
CREATE TABLE result (
    id BIGINT PRIMARY KEY DEFAULT nextval('result_seq'),
    athlete_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    result_value DECIMAL(10, 2) NOT NULL,
    points DECIMAL(10, 2),
    rank INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_result_athlete FOREIGN KEY (athlete_id) REFERENCES athlete(id) ON DELETE CASCADE,
    CONSTRAINT fk_result_event FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
    CONSTRAINT uq_athlete_event UNIQUE (athlete_id, event_id)
);

-- Indexes for performance
CREATE INDEX idx_category_competition ON category(competition_id);
CREATE INDEX idx_event_category ON event(category_id);
CREATE INDEX idx_athlete_club ON athlete(club_id);
CREATE INDEX idx_athlete_category ON athlete(category_id);
CREATE INDEX idx_athlete_birth_gender ON athlete(birth_year, gender);
CREATE INDEX idx_result_athlete ON result(athlete_id);
CREATE INDEX idx_result_event ON result(event_id);
