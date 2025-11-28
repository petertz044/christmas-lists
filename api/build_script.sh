#!/bin/bash

sudo docker compose down
mvn clean install 
sudo docker build -t zullo/christmas-be .
sudo docker compose up -d