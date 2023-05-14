#!/bin/bash
lsof -i :8090 | awk '{print $2}' | tail -n 1 | xargs kill
rm -rf /home/backend
mkdir backend
chmod 777 backend