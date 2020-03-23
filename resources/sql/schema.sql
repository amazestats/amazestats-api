CREATE TABLE IF NOT EXISTS amaze_users(
  id SERIAL PRIMARY KEY,
  alias VARCHAR(50) UNIQUE,
  password VARCHAR(128)
);

CREATE TABLE IF NOT EXISTS competitions(
  id SERIAL PRIMARY KEY,
  key VARCHAR(50) UNIQUE,
  name VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS competition_admins(
  admin INTEGER REFERENCES amaze_users(id),
  competition INTEGER REFERENCES competitions(id),
  PRIMARY KEY (admin, competition)
);

CREATE TABLE IF NOT EXISTS divisions(
  id SERIAL PRIMARY KEY,
  key VARCHAR(50) UNIQUE,
  name VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS teams(
  id SERIAL PRIMARY KEY,
  division INTEGER REFERENCES divisions(id),
  key VARCHAR(50) UNIQUE,
  name VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS matches(
  id SERIAL PRIMARY KEY,
  division INTEGER REFERENCES divisions(id),
  home_team INTEGER REFERENCES teams(id),
  away_Team INTEGER REFERENCES teams(id)
);

CREATE TABLE IF NOT EXISTS sets(
  id SERIAL PRIMARY KEY,
  match INTEGER REFERENCES matches(id),
  home_score INTEGER,
  away_score INTEGER
);
