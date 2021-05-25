#!/bin/bash
path=$PWD

for i in {1..10000}
do
  echo "Making $i request"
  curl -X POST -H "Content-Type: application/json" -d '{
  "owner":"12312",
  "bleHardware":"skljglfk",
  "vehicleName":"bel veicolo"
}' http://localhost/proxy/api/v1/tracks/vehicles
done