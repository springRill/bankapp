# Приложение bank

## приложение состоит из частей:

1. postgresql
2. kafka
3. nginx
4. keycloak с конфигурацией
5. notifications - сервис уведомлений
6. blocker - сервис блокировки операций
7. exchenge-generator - приложение для генерации курсов валют
8. exchange - сервис хранения курсов валют
9. cash - сервис ввода и вывода наличных
10. transfer - сервис перевода денег между счетами
11. accounts - сервис хранения информации о пользователях и счетах
12. front-ui - веб-приложение с клиентским HTML-интерфейсом

## запуск в зонтичным helm чартом (Windows 10)
- упаковать все модули мавеном (package)
- должен быть установлен docker

### выполнить из корня проекта

#### запускаем minikube и установливаем окружение

- `minikube start --driver=docker`
- `helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx`
- `helm repo update`
- `helm upgrade --install ingress-nginx ingress-nginx/ingress-nginx   --namespace ingress-nginx --create-namespace`
- `minikube docker-env | Invoke-Expression`

#### собираем docker контейнеры

- `docker build -t exchange-api ./exchange`
- `docker build -t exchange-generator ./exchange-generator`
- `docker build -t blocker-api ./blocker`
- `docker build -t notifications-api ./notifications`
- `docker build -t accounts-api ./accounts`
- `docker build -t transfer-api ./transfer`
- `docker build -t cash-api ./cash`
- `docker build -t front-ui ./front-ui`

#### обновляем зависимости

- `helm dependency update ./bank-app`

#### разворачиваем приложение в kubernetes

- `helm install bank-app ./bank-app` иногда долго скачиваются образы
- `kubectl get pods` дождаться готовности подов

#### запускаем приложение

можно уже пробросить порт от `bank-app-front-ui` \
`kubectl port-forward svc/bank-app-front-ui 8080:8080` \
и запустить приложение в браузере http://localhost:8080/

а можно прописать в `etc/hosts` `127.0.0.1 bankapp` \
запустить в консоли `minikube tunnel` \
и запустить приложение в браузере http://bankapp/

#### останавливаем приложение
`helm uninstall bank-app`


## запуск jenkins (Windows 10)

- включить в докере "Settingd -> Ggeneral -> Expose daemon on tcp://localhost:2375 without TLS"
- заменяем `MINIKUBE_PATH` в `jenkins/.env`
- приписываем `GHCR_TOKEN` в `jenkins/.env`
- приписываем `GITHUB_USERNAME` в `jenkins/.env`
- приписываем `DOCKER_REGISTRY` в `jenkins/.env`
- исполняем `jenkins/docker-compose.yml` jenkins будет доступен по адресу http://localhost:9090/
- запускаем в консоли `docker network connect minikube jenkins`

### для запуска приложения отдельными чартами в default namespace запускаем последовательно сборки в jenkins
- 01_kafka
- 02_keycloak
- 03_postgresql
- 04_exchange-api
- 05_exchange-generator
- 06_blocker-api
- 07_notifications-api
- 08_accounts-api
- 09_transfer-api
- 10_cash-api
- 11_front-ui

### запускаем в консоли
- `minikube tunnel`
- приложение будет доступно в браузере http://bankapp/

### для запуска приложения полностью в test и, опционально drod namespace запускаем в jenkins
- 00_bank-app

### прописываем в `etc/hosts`
- `127.0.0.1 bankapp-test`
- `127.0.0.1 bankapp-prod`

### приложение будет доступно в браузере
- тестовое  http://bankapp-test/
- продуктовое  http://bankapp-prod/
