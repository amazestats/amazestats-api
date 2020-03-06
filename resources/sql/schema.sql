CREATE TABLE IF NOT EXISTS divisions(
  id SERIAL PRIMARY KEY,
  name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS teams(
  id SERIAL PRIMARY KEY,
  division INTEGER REFERENCES divisions(id),
  name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS matches(
  id SERIAL PRIMARY KEY,
  division INTEGER REFERENCES divisions(id),
  home_team INTEGER REFERENCES teams(id),
  away_Team INTEGER REFERENCES teams(id)
);
