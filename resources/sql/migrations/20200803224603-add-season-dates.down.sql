ALTER TABLE seasons
DROP COLUMN start_date CASCADE,
DROP COLUMN end_date CASCADE;

--;;

ALTER TABLE matches
DROP COLUMN match_date CASCADE;
