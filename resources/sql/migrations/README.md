# Migrations

In this directory the initialization script (*init.sql*) and all migrations
are stored.
Creating, marking and running these migrations are done via
`src/amazestats/database/migration.clj`.

## Application Usage

The application will run the `init.sql` script the first time the application
is started (or if the database is not initialized).
During this first time all migrations will also be marked as complete,
as the init-script should be complete.

The next server starts will check for new migrations and run them if available.

## Creating Migration

Run `amazestats/database/migration/create`.
This will result in newly generated, time-stamped migration scripts
(up and down).
Give the script a proper description after the time stamp.

**NOTE:** Scripts that should run multiple statements should have the
statements separated by `--;;`.

## Migration and Rollback

To run the migrations or to roll them back use the functions:
`amazestats/database/migration/migrate` and
`amazestats/database/migration/rollback`.

## Marking as Completed

The migrations could be marked as completed without being ran with:
`amazestats/database/migration/mark-all-migrations-complete` or
`amazestats/database/migration/mark-complete` (for single migration).

For most users this should not be relevant.
