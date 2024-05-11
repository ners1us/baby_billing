### Инструменты

- OpenJDK 17
- Maven
- Spring
- RabbitMQ
- Docker
- Swagger
- PostgreSQL

### Глоссарий

- CDR – Call Data Record – формат файла, содержащего в себе информацию о действиях, совершенных абонентом за тарифицируемый период.
- BRT – Billing Real Time.
- HRS – High performance rating server.
- msisdn - Mobile Subscriber Integrated Services Digital Number - номер мобильного абонента цифровой сети.

### Постановки

- [Симулятор Генератора](https://docs.google.com/document/d/1uD2oaUhXccn-I2PdqZ1q3_mYTdI2XhHQ/edit?usp=sharing&ouid=113918469695711497887&rtpof=true&sd=true)
- [BRT-HRS](https://docs.google.com/document/d/1GosTWBp7OSpktRpfLRm14eGcjLiYv3jZ/edit?usp=sharing&ouid=113918469695711497887&rtpof=true&sd=true)
- [HRS Сервис](https://docs.google.com/document/d/1HjNd-IDC5nQDPpJ3f3gAjznPFq5SsIfD/edit?usp=sharing&ouid=113918469695711497887&rtpof=true&sd=true)
- [Use-Case](https://docs.google.com/document/d/19Jym4V2EAc4hVurmnbo5_9UYn61sK6K0/edit?usp=sharing&ouid=113918469695711497887&rtpof=true&sd=true)
- [Общая Схема Проекта](https://drive.google.com/file/d/1dnqJGq3WitTHMvmK762f00M85IKKpGZW/view?usp=sharing)
- [ERD-Диаграммы](https://drive.google.com/file/d/1J2bDM4hUKx2S_W-lY_oEdO-1e16yMpF5/view?usp=sharing)

### Запуск приложения

```bash
docker compose up -d --build
```


### Остановка приложенмя

```bash
docker compose down
```
### Просмотр логов приложения

```bash
docker logs baby_billing-app-1
```

### Очистка кэша

```bash
docker system prune -f
```

### Данные для авторизации в БД

- Для базы данных сервиса Симулятора Коммутатора:
    - username: sa
    - password: password

- Для базы данных сервиса BRT:
    - username: user1
    - password: password1

- Для базы данных сервиса HRS:
    - username: user2
    - password: password2

### Примечание
Спецификация Swagger находится в файле ```openapi.yaml```. Для проверки работоспособности GET-запросов рекомендуется вручную вводить пути в браузере или использовать инструмент командной строки ```cURL```.