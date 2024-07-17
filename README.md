# baby_billing
Приложение, работающее с биллинговой системой.

## Инструменты

- OpenJDK 17
- Maven
- Spring
- RabbitMQ
- Docker
- Swagger
- PostgreSQL
- JUnit 5
- Mockito
- Testcontainers

## Глоссарий

- CDR – Call Data Record – формат файла, содержащего в себе информацию о действиях, совершенных абонентом за тарифицируемый период.
- BRT – Billing Real Time.
- HRS – High performance Rating Server.

## Постановки

- [Симулятор Генератора](https://docs.google.com/document/d/1uD2oaUhXccn-I2PdqZ1q3_mYTdI2XhHQ/edit?usp=sharing&ouid=113918469695711497887&rtpof=true&sd=true)
- [BRT-HRS](https://docs.google.com/document/d/1GosTWBp7OSpktRpfLRm14eGcjLiYv3jZ/edit?usp=sharing&ouid=113918469695711497887&rtpof=true&sd=true)
- [HRS Сервис](https://docs.google.com/document/d/1HjNd-IDC5nQDPpJ3f3gAjznPFq5SsIfD/edit?usp=sharing&ouid=113918469695711497887&rtpof=true&sd=true)
- [Use-Case](https://docs.google.com/document/d/19Jym4V2EAc4hVurmnbo5_9UYn61sK6K0/edit?usp=sharing&ouid=113918469695711497887&rtpof=true&sd=true)
- [Общая Схема Проекта](https://drive.google.com/file/d/1YzHcSYZaLkd1YmqKzIlxhe2zc-HjVMI8/view?usp=sharing)

## Запуск приложения

```bash
docker compose up -d --build
```
## Запуск приложения co свежими базами данных

```bash
docker volume rm baby_billing_cdr_data baby_billing_brt_data baby_billing_hrs_data && docker compose up -d --build
```

## Остановка приложения

```bash
docker compose down
```
## Просмотр логов микросервиса cdr_generator

```bash
docker logs baby_billing-cdr-app-1
```

## Просмотр логов микросервиса brt

```bash
docker logs baby_billing-brt-app-1
```

## Просмотр логов микросервиса hrs

```bash
docker logs baby_billing-hrs-app-1
```

## Просмотр логов тестов микросервиса cdr_generator

```bash
docker logs baby_billing-cdr-tests-1
```

## Просмотр логов тестов микросервиса brt

```bash
docker logs baby_billing-brt-tests-1
```

## Просмотр логов тестов микросервиса hrs

```bash
docker logs baby_billing-hrs-tests-1
```

## Очистка кэша

```bash
docker system prune -f
```

## Данные для авторизации в БД

- ### Для базы данных сервиса Симулятора Коммутатора (PostgreSQL, порт - 5432):
    - username: sa
    - password: password

- ### Для базы данных сервиса BRT (PostgreSQL, порт - 5434):
    - username: user1
    - password: password1

- ### Для базы данных сервиса HRS (PostgreSQL, порт - 5435):
    - username: user2
    - password: password2

## Примечания
- Спецификация Swagger приведена в файле ```openapi.yaml```.
- Для проверки работоспособности GET-запросов следует вручную вводить пути в браузере или использовать инструмент командной строки ```cURL```.
- Если нет возможности открыть Swagger в IDE, можно использовать [онлайн-инструмент](https://editor.swagger.io/) и вставить туда код из ```openapi.yaml```.
- Каждый раз, когда вызывается любой из endpoint-триггеров (см. Use-Case), рекомендуется проверять логи микросервисов в docker.
