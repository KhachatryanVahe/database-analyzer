#!/bin/bash

set -a
source .env
set +a

QUERY="CREATE EXTENSION IF NOT EXISTS pg_stat_statements;"

CONNECTION_STR="postgresql://$DB_USER:$DB_PASSWORD@$DB_HOST:$DB_PORT/$DB"

psql $CONNECTION_STR -c "$QUERY"

docker restart postgres-thesis