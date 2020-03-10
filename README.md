# amazestats

## API Specification

The current API specification can be found at
[http://localhost:8080/] once the service is deployed.

The yaml can also be directly inspected in *resources/public/openapi/spec.yml*.

## Installation & Execution

### Requirements

To successfully run the service some environment variables must be provided.
They need to be provided both in the *profiles.clj* of the project root,
and also as environment variables.

#### Required Environemnt Variables

* POSTGRES_DB
* POSTGRES_USER
* POSTGRES_PASSWORD

The environment variables are most easily set in the *.env* -file in the root
of the project.

```bash
POSTGRES_USER=dbuser
POSTGRES_PASSWORD=dbpass
POSTGRES_DB=dbname
```

#### Example

```clojure
;; Example profiles.clj
{:dev {:env {:postgres-db "dbname"
             :postgres-user "dbuser"
             :postgres-password "dbpass"}}}
``` 

### Deployment

The easiest way to deploy the service is to use docker-compose.

```bash
docker-compose up -d # starts required services
docker-compose logs -f dev # shows the logs for dev service continuously
```

These options are availalbe in a Makefile for easy execution.
The same commands could be ran by:
```bash
make deploy server-logs
```
