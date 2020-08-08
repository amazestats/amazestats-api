# amazestats

## API Specification

The current API specification can be found at
[http://localhost:8080/](http://localhost:8080/)
once the service is deployed.

The yaml can also be directly inspected in *resources/public/openapi/spec.yml*.

### API Linting

The API is linted when pushed to Circle CI.
To lint locally use:
`npx @redocly/openapi-cli lint resources/public/openapi/spec.yml`.

Configuration for the linter can be found in: `.redocly.yaml'.

## Installation & Execution

### Requirements

To successfully run the service some environment variables must be provided.
They need to be provided both in the *profiles.clj* of the project root,
and also as environment variables.

#### Required Environment Variables

* POSTGRES_DB
* POSTGRES_USER
* POSTGRES_PASSWORD
* SECRET_KEY
* TOKEN_EXPIRATION_PERIOD

The environment variables are most easily set in the *.env* -file in the root
of the project.

```bash
POSTGRES_USER=dbuser
POSTGRES_PASSWORD=dbpass
POSTGRES_DB=dbname
SECRET_KEY=somethingsecret
TOKEN_EXPIRATION_PERIOD=60
```

#### Example

```clojure
;; Example profiles.clj
{:dev {:env {:postgres-db "dbname"
             :postgres-user "dbuser"
             :postgres-password "dbpass"
             :secret-key "somethingsecret"
             :token-expiration-period 60}}}
```

### Deployment

The easiest way to deploy the service is to use docker-compose.

```bash
docker-compose up -d # starts required services
docker-compose logs -f web # shows the logs for dev service continuously
```

These options are availalbe in a Makefile for easy execution.
The same commands could be ran by:
```bash
make deploy server-logs
```
