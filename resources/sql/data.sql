INSERT INTO divisions (name, key) VALUES ('Allsvenskan', 'allsvenskan');
INSERT INTO divisions (name, key) VALUES ('Superettan', 'superettan');

INSERT INTO teams (name, division) VALUES ('Pineapple Pizza', '1');
INSERT INTO teams (name, division) VALUES ('SuperSmash', '1');
INSERT INTO teams (name, division) VALUES ('Arsenal', '1');
INSERT INTO teams (name, division) VALUES ('Man City', '2');
INSERT INTO teams (name, division) VALUES ('Peking', '2');

INSERT INTO matches (division, home_team, away_team)
VALUES ('1', '1', '2');
INSERT INTO matches (division, home_team, away_team)
VALUES ('1', '3', '1');
INSERT INTO matches (division, home_team, away_team)
VALUES ('2', '4', '5');
INSERT INTO matches (division, home_team, away_team)
VALUES ('2', '5', '4');
INSERT INTO matches (division, home_team, away_team)
VALUES ('1', '3', '2');
INSERT INTO matches (division, home_team, away_team)
VALUES ('1', '2', '1');
