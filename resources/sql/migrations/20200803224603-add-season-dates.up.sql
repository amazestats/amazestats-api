-- Update ALL rows for seasons and matches with the UNIX Epoch Time

ALTER TABLE seasons
ADD COLUMN start_date TIMESTAMP,
ADD COLUMN end_date TIMESTAMP;

--;;

ALTER TABLE matches
ADD COLUMN match_date TIMESTAMP;

--;;

UPDATE seasons
SET start_date = to_timestamp(0), end_date = to_timestamp(0)
WHERE true;

--;;

UPDATE matches
SET match_date = to_timestamp(0)
WHERE true;
