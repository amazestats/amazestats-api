INSERT INTO amaze_users (alias) VALUES ('emini');
INSERT INTO amaze_users (alias) VALUES ('jockeboi');

INSERT INTO competitions (name, key)
VALUES ('Korpen Volleyboll', 'korpen-volleyboll');

INSERT INTO competition_admins (admin, competition) VALUES ('1', '1');

INSERT INTO divisions (name, key) VALUES ('Allsvenskan', 'allsvenskan');
INSERT INTO divisions (name, key) VALUES ('Superettan', 'superettan');

INSERT INTO teams (name, key, division)
VALUES ('Pineapple Pizza', 'pineapple-pizza', '1');
INSERT INTO teams (name, key, division)
VALUES ('SuperSmash', 'supersmash', '1');
INSERT INTO teams (name, key, division)
VALUES ('Arsenal', 'arsenal', '1');
INSERT INTO teams (name, key, division)
VALUES ('Man U', 'man-u', '1');
INSERT INTO teams (name, key, division)
VALUES ('Team Cambio', 'team-cambio', '1');
INSERT INTO teams (name, key, division)
VALUES ('Dyrgriparna', 'dyrgriparna', '1');

INSERT INTO teams (name, key, division)
VALUES ('Man City', 'man-city', '2');
INSERT INTO teams (name, key, division)
VALUES ('Peking', 'peking', '2');

INSERT INTO matches (division, home_team, away_team)
VALUES ('1', '1', '2');
INSERT INTO matches (division, home_team, away_team)
VALUES ('1', '3', '4');
INSERT INTO matches (division, home_team, away_team)
VALUES ('1', '5', '6');
INSERT INTO matches (division, home_team, away_team)
VALUES ('1', '1', '3');
INSERT INTO matches (division, home_team, away_team)
VALUES ('1', '2', '5');
INSERT INTO matches (division, home_team, away_team)
VALUES ('1', '4', '6');


INSERT INTO matches (division, home_team, away_team)
VALUES ('2', '7', '8');
INSERT INTO matches (division, home_team, away_team)
VALUES ('2', '8', '7');

INSERT INTO sets (match, home_score, away_score)
VALUES ('1', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('1', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('1', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('2', '23', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('2', '25', '22');
INSERT INTO sets (match, home_score, away_score)
VALUES ('2', '25', '19');
INSERT INTO sets (match, home_score, away_score)
VALUES ('2', '25', '13');

INSERT INTO sets (match, home_score, away_score)
VALUES ('3', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('3', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('3', '25', '20');
