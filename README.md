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
- Liquibase
- JUnit 5
- Mockito
- Testcontainers

## Глоссарий

- CDR – Call Data Record – формат файла, содержащего в себе информацию о действиях, совершенных абонентом за тарифицируемый период.
- BRT – Billing Real Time.
- HRS – High performance Rating Server.

## Постановки

- [Симулятор Коммутатора](https://docs.google.com/document/d/1uD2oaUhXccn-I2PdqZ1q3_mYTdI2XhHQ/edit?usp=sharing&ouid=113918469695711497887&rtpof=true&sd=true)
- [BRT-HRS](https://docs.google.com/document/d/1GosTWBp7OSpktRpfLRm14eGcjLiYv3jZ/edit?usp=sharing&ouid=113918469695711497887&rtpof=true&sd=true)
- [HRS Сервис](https://docs.google.com/document/d/1HjNd-IDC5nQDPpJ3f3gAjznPFq5SsIfD/edit?usp=sharing&ouid=113918469695711497887&rtpof=true&sd=true)
- [Use-Case](https://docs.google.com/document/d/19Jym4V2EAc4hVurmnbo5_9UYn61sK6K0/edit?usp=sharing&ouid=113918469695711497887&rtpof=true&sd=true)
- [ER-диаграммы](https://drive.google.com/file/d/1IyLWccGDe9aAdz40KbILt1M-0gVmsE-h/view?usp=sharing)
- [Общая Схема Проекта](https://drive.google.com/file/d/1UJjf8MSSPOa2BXljRD3xHSOZudCxqQH4/view?usp=sharing)

## Запуск приложения

```bash
docker compose up -d --build
```

## Очистка баз данных

```bash
docker volume rm baby_billing_cdr_data baby_billing_brt_data baby_billing_hrs_data
```

## Остановка приложения

```bash
docker compose down
```
## Просмотр логов Симулятора Коммутатора

```bash
docker logs baby_billing-cdr-app-1
```

## Просмотр логов сервиса BRT

```bash
docker logs baby_billing-brt-app-1
```

## Просмотр логов сервиса HRS

```bash
docker logs baby_billing-hrs-app-1
```

## Очистка кэша

```bash
docker system prune -f
```

## Данные для авторизации в БД

- ### Для базы данных Симулятора Коммутатора (PostgreSQL, порт - 5432):
    - username: sa
    - password: password

- ### Для базы данных сервиса BRT (PostgreSQL, порт - 5434):
    - username: user1
    - password: password1

- ### Для базы данных сервиса HRS (PostgreSQL, порт - 5435):
    - username: user2
    - password: password2

## Примечания
- Для просмотра спецификации Swagger к Симулятору Коммутатора следует убедиться, что он запущен, а затем в браузере перейти по адресу ```http://localhost:8081/swagger-ui/index.html```.<br>
  Для сервиса BRT - ```http://localhost:8082/swagger-ui/index.html```.
- Для вызова endpoint'ов ```admin-controller``` или ```client-controller``` нужно предварительно авторизоваться по адресу ```http://localhost:8082/login```.<br>
  По адресу ```http://localhost:8082/logout``` можно выйти из аккаунта.
- Данные для использования endpoint'ов ```admin-controller``` (также будет возможность воспользоваться ```client-controller```):
    - username: admin
    - password: admin
- Для использования endpoint'ов ```client-controller``` в качестве ```username``` и ```password``` следует передать номер телефона клиента, существующий в базе данных сервиса BRT, в таблице ```clients```.
- Каждый раз, когда вызывается любой из endpoint-триггеров (см. Use-Case), рекомендуется проверять логи сервисов в docker.
