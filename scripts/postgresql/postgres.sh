#!/bin/bash

# https://stackoverflow.com/questions/7975556/how-to-start-postgresql-server-on-mac-os-x
# https://www.codementor.io/@engineerapart/getting-started-with-postgresql-on-mac-osx-are8jcopb

# brew services start postgresql
pg_ctl -D /usr/local/var/postgres start

# egrep 'listen|port' /usr/local/var/postgres/postgresql.conf
# -> localhost:5432

# psql postgres
# \du
# \password jiayee
# # OR
# CREATE ROLE jiayee WITH LOGIN PASSWORD '123456';
# ALTER ROLE jiayee blah blah blah
