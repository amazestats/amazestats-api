-- DIVISIONS

INSERT INTO divisions (competition, name, key)
VALUES ('1', 'Allsvenskan', 'allsvenskan');
INSERT INTO divisions (competition, name, key)
VALUES ('1', 'Superettan', 'superettan');

-- TEAMS DIVISION 1

INSERT INTO teams (name, key, competition, division, activated)
VALUES ('Arsenal', 'arsenal', '1', '1', TRUE);
INSERT INTO teams (name, key, competition, division, activated)
VALUES ('Everton', 'everton', '1', '1', TRUE);
INSERT INTO teams (name, key, competition, division, activated)
VALUES ('Sunderland', 'sunderland', '1', '1', TRUE);
INSERT INTO teams (name, key, competition, division, activated)
VALUES ('Aston Villa', 'aston-villa', '1', '1', TRUE);

-- TEAMS DIVISION 2

INSERT INTO teams (name, key, competition, division, activated)
VALUES ('Notts County', 'notts-county', '1', '2', TRUE);
INSERT INTO teams (name, key, competition, division, activated)
VALUES ('Northampton Town', 'northampton-town', '1', '2', TRUE);
INSERT INTO teams (name, key, competition, division, activated)
VALUES ('Bury', 'bury', '1', '2', TRUE);
INSERT INTO teams (name, key, competition, division, activated)
VALUES ('Macclesfield', 'macclesfield', '1', '2', TRUE);

-- SEASONS DIVISION 1

INSERT INTO seasons (name, key, division)
VALUES ('2019', '2019', 1);
INSERT INTO seasons (name, key, division)
VALUES ('2020', '2020', 1);

-- SEASONS DIVISION 2

INSERT INTO seasons (name, key, division)
VALUES ('2019', '2019', 2);
INSERT INTO seasons (name, key, division)
VALUES ('2020', '2020', 2);

-- MATCHES SEASON ID 1

INSERT INTO matches (season, home_team, away_team)
VALUES ('1', '1', '2');
INSERT INTO matches (season, home_team, away_team)
VALUES ('1', '3', '4');
INSERT INTO matches (season, home_team, away_team)
VALUES ('1', '1', '3');
INSERT INTO matches (season, home_team, away_team)
VALUES ('1', '2', '4');
INSERT INTO matches (season, home_team, away_team)
VALUES ('1', '1', '4');
INSERT INTO matches (season, home_team, away_team)
VALUES ('1', '3', '2');

-- MATCHES SEASON ID 2

INSERT INTO matches (season, home_team, away_team)
VALUES ('2', '1', '2');
INSERT INTO matches (season, home_team, away_team)
VALUES ('2', '3', '4');
INSERT INTO matches (season, home_team, away_team)
VALUES ('2', '1', '3');
INSERT INTO matches (season, home_team, away_team)
VALUES ('2', '2', '4');
INSERT INTO matches (season, home_team, away_team)
VALUES ('2', '1', '4');
INSERT INTO matches (season, home_team, away_team)
VALUES ('2', '3', '2');

-- MATCHES SEASON ID 3 // DIVISION 2

INSERT INTO matches (season, home_team, away_team)
VALUES ('3', '5', '6');
INSERT INTO matches (season, home_team, away_team)
VALUES ('3', '7', '8');
INSERT INTO matches (season, home_team, away_team)
VALUES ('3', '5', '7');
INSERT INTO matches (season, home_team, away_team)
VALUES ('3', '6', '8');
INSERT INTO matches (season, home_team, away_team)
VALUES ('3', '5', '8');
INSERT INTO matches (season, home_team, away_team)
VALUES ('3', '7', '6');

-- MATCHES SEASON ID 4 // DIVISION 2

INSERT INTO matches (season, home_team, away_team)
VALUES ('4', '5', '6');
INSERT INTO matches (season, home_team, away_team)
VALUES ('4', '7', '8');
INSERT INTO matches (season, home_team, away_team)
VALUES ('4', '5', '7');
INSERT INTO matches (season, home_team, away_team)
VALUES ('4', '6', '8');
INSERT INTO matches (season, home_team, away_team)
VALUES ('4', '5', '8');
INSERT INTO matches (season, home_team, away_team)
VALUES ('4', '7', '6');

-- SETS (oh my)

-- SEASON 1

INSERT INTO sets (match, home_score, away_score)
VALUES ('1', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('1', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('1', '25', '20');
 
INSERT INTO sets (match, home_score, away_score)
VALUES ('2', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('2', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('2', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('3', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('3', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('3', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('4', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('4', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('4', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('5', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('5', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('5', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('6', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('6', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('6', '25', '20');


-- SEASON 2

INSERT INTO sets (match, home_score, away_score)
VALUES ('7', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('7', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('7', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('8', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('8', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('8', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('9', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('9', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('9', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('10', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('10', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('10', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('11', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('11', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('11', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('12', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('12', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('12', '25', '20');

-- SEASON 3

INSERT INTO sets (match, home_score, away_score)
VALUES ('13', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('13', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('13', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('14', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('14', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('14', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('15', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('15', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('15', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('16', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('16', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('16', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('17', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('17', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('17', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('18', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('18', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('18', '25', '20');

-- SEASON 4

INSERT INTO sets (match, home_score, away_score)
VALUES ('19', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('19', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('19', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('20', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('20', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('20', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('21', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('21', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('21', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('22', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('22', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('22', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('23', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('23', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('23', '25', '20');

INSERT INTO sets (match, home_score, away_score)
VALUES ('24', '25', '13');
INSERT INTO sets (match, home_score, away_score)
VALUES ('24', '27', '25');
INSERT INTO sets (match, home_score, away_score)
VALUES ('24', '25', '20');
