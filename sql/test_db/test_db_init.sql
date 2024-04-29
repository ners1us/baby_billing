CREATE TABLE IF NOT EXISTS clients (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       phone_number VARCHAR(14) NOT NULL
    );

CREATE TABLE IF NOT EXISTS history (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       type VARCHAR(2) NOT NULL,
    client_id INT NOT NULL,
    caller_id INT NOT NULL,
    start_time BIGINT NOT NULL,
    end_time BIGINT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (caller_id) REFERENCES clients(id)
    );
