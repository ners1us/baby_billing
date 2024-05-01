CREATE TABLE clients (
                         client_id VARCHAR(14) PRIMARY KEY,
                         tariff_id INT,
                         balance DECIMAL(19, 2)
);

CREATE TABLE history (
                         id BIGINT serial PRIMARY KEY,
                         type VARCHAR(2),
                         client_id VARCHAR(14),
                         caller_id VARCHAR(14),
                         start_time DATETIME,
                         end_time DATETIME,
                         tariff_id INT,
                         internal BOOLEAN,
                         cost DECIMAL(19, 2)
);

CREATE TABLE tariff_payments_history (
                        id BIGINT serial PRIMARY KEY,
                        client_id VARCHAR(14),
                        tariff_id INT,
                        cost DECIMAL(19, 2),
                        time DATETIME
);
