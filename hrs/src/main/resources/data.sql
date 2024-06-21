INSERT INTO tariffs (tariff_id, tariff_rules)
VALUES (
           11,
           '{
             "name": "Классика",
             "description": "Входящие - бесплатно. Исходящие внутри ромашки - 1,5 у.е/мин, для остальных - 2,5 у.е./мин",
             "currency": "RUB",
             "overlimit": {
               "internalIncoming": 0.00,
               "internalOutcoming": 1.50,
               "externalIncoming": 0.00,
               "externalOutcoming": 2.50
             }
           }'::jsonb
       ),
       (
           12,
           '{
             "name": "Помесячный",
             "description": "Лимит 50 минут на все звонки, сверх лимита - по тарифу Классика",
             "currency": "RUB",
             "prepaid": {
               "tariffCost": 100,
               "limits": {
                 "totalMinutes": 50
               }
             },
             "overlimit": {
               "referenceTariffId": 11
             }
           }'::jsonb
       );
