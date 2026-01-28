-- Create Competition table
CREATE TABLE competition (
    id BIGINT PRIMARY KEY DEFAULT nextval('competition_seq'),
    name VARCHAR(255) NOT NULL,
    competition_date DATE NOT NULL,
    location VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create Club table
CREATE TABLE club (
    id BIGINT PRIMARY KEY DEFAULT nextval('club_seq'),
    name VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(10),
    country VARCHAR(2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create Category table
CREATE TABLE category (
    id BIGINT PRIMARY KEY DEFAULT nextval('category_seq'),
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('MALE', 'FEMALE')),
    year_from INTEGER NOT NULL,
    year_to INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create Event table
CREATE TABLE event (
    id BIGINT PRIMARY KEY DEFAULT nextval('event_seq'),
    name VARCHAR(100) NOT NULL,
    event_type VARCHAR(10) NOT NULL CHECK (event_type IN ('TRACK', 'FIELD')),
    unit VARCHAR(20) NOT NULL,
    higher_is_better BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create CategoryEvent junction table
CREATE TABLE category_event (
    id BIGINT PRIMARY KEY DEFAULT nextval('category_event_seq'),
    category_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    CONSTRAINT fk_category_event_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
    CONSTRAINT fk_category_event_event FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
    CONSTRAINT uq_category_event UNIQUE (category_id, event_id)
);

-- Create Athlete table
CREATE TABLE athlete (
    id BIGINT PRIMARY KEY DEFAULT nextval('athlete_seq'),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('MALE', 'FEMALE')),
    club_id BIGINT,
    category_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_athlete_club FOREIGN KEY (club_id) REFERENCES club(id) ON DELETE SET NULL,
    CONSTRAINT fk_athlete_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL
);

-- Create AthleteCompetition table
CREATE TABLE athlete_competition (
    id BIGINT PRIMARY KEY DEFAULT nextval('athlete_competition_seq'),
    athlete_id BIGINT NOT NULL,
    competition_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ac_athlete FOREIGN KEY (athlete_id) REFERENCES athlete(id) ON DELETE CASCADE,
    CONSTRAINT fk_ac_competition FOREIGN KEY (competition_id) REFERENCES competition(id) ON DELETE CASCADE,
    CONSTRAINT fk_ac_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
    CONSTRAINT uq_athlete_competition UNIQUE (athlete_id, competition_id)
);

-- Create Result table
CREATE TABLE result (
    id BIGINT PRIMARY KEY DEFAULT nextval('result_seq'),
    athlete_competition_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    value DECIMAL(10, 4) NOT NULL,
    points INTEGER NOT NULL,
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_result_athlete_competition FOREIGN KEY (athlete_competition_id) REFERENCES athlete_competition(id) ON DELETE CASCADE,
    CONSTRAINT fk_result_event FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
    CONSTRAINT uq_result UNIQUE (athlete_competition_id, event_id)
);

-- Create indexes for performance
CREATE INDEX idx_athlete_name ON athlete(last_name, first_name);
CREATE INDEX idx_athlete_birth_date ON athlete(birth_date);
CREATE INDEX idx_athlete_category ON athlete(category_id);
CREATE INDEX idx_athlete_club ON athlete(club_id);
CREATE INDEX idx_category_gender_range ON category(gender, year_from, year_to);
CREATE INDEX idx_result_athlete_competition ON result(athlete_competition_id);
CREATE INDEX idx_result_event ON result(event_id);
CREATE INDEX idx_athlete_competition_competition ON athlete_competition(competition_id);
CREATE INDEX idx_athlete_competition_category ON athlete_competition(category_id);
