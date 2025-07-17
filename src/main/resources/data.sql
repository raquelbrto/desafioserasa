CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       login VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

INSERT INTO users (login, password, role) VALUES
    ('admin', '$2a$10$IZhhwNjfTSe8qdc/ofDOkO9r0SoMU9I0EEwlwdGzEAj8vxADu63a2', 'ADMIN');
