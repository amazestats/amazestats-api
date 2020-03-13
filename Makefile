include .env

all: install uberjar deploy

install:
	lein deps

uberjar:
	lein ring uberjar

deploy: clean uberjar
	docker-compose up -d

clean:
	docker-compose down
	rm -rf target/

server-logs:
	docker-compose logs -f web

db-logs:
	docker-compose logs -f postgres

db:
	psql -d ${POSTGRES_DB} -U ${POSTGRES_USER} -h localhost

db-init:
	psql -d ${POSTGRES_DB} -U ${POSTGRES_USER} -h localhost -f resources/sql/schema.sql

db-drop: clean
	rm -rf /var/opt/amazestats-data
