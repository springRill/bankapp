#!/bin/sh

# Завершаем работу, если какая-либо команда не выполнится
set -e

# Ждем, пока Consul будет готов
echo 'Waiting for Consul to be ready...'
sleep 2

until consul members -http-addr=consul:8500 | grep -q 'server'; do
  echo 'Consul is not ready yet, retrying...'
  sleep 1
done
echo 'Consul is ready. Importing configuration from YAML...'

# Читаем содержимое YAML-файла
yaml_content=$(cat /config/config.yaml)

# Помещаем весь YAML в Consul под одним ключом
consul kv put -http-addr=consul:8500 'config/application/data' "$yaml_content"

echo 'Импорт завершен.'
