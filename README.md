# Приложение bank

## приложение состоит из частей:

1. postgresql
2. nginx
3. keycloak с конфигурацией
4. notifications - сервис уведомлений
5. blocker - сервис блокировки операций
6. exchenge-generator - приложение для генерации курсов валют
7. exchange - сервис хранения курсов валют
8. cash - сервис ввода и вывода наличных
9. transfer - сервис перевода денег между счетами
10. accounts - сервис хранения информации о пользователях и счетах
11. front-ui - веб-приложение с клиентским HTML-интерфейсом

## запуск в зонтичным helm чартом (Windows 10)
- упаковать все модули мавеном (package)
- должен быть установлен docker

### выполнить из корня проекта

#### запускаем minikube и установливаем окружение

- `minikube start --driver=docker`
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

#### удаляем 
minikube delete minikube


## запуск jenkins (Windows 10)

- должен быть установлен docker
- включить в докере "Settingd -> Ggeneral -> Expose daemon on tcp://localhost:2375 without TLS"
- запускаем в консоли `minikube start --driver=docker`
- заменяем `MINIKUBE_PATH` в `jenkins/.env`
- исполняем `jenkins/docker-compose.yml`
- запускаем в консоли `docker network connect minikube jenkins`

## запускаем последовательно сборки в jenkins
- keycloak
- postgresql
- exchange-api
- exchange-generator
- 