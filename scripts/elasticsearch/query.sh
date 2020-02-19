#!/bin/bash

curl -X GET http://localhost:9200/
curl -X GET http://localhost:9200/_cat/indices

# https://stackoverflow.com/questions/14565888/how-can-i-view-the-contents-of-an-elasticsearch-index
curl -X GET http://localhost:9200/immutablejob/_search?pretty

# https://www.elastic.co/guide/en/elasticsearch/reference/current/search-search.html
curl -X GET http://localhost:9200/immutablejob/_search?q=status:ACTIVE
curl -X GET http://localhost:9200/immutablejob/_search?q=employer_id:1
