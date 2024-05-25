IF NOT EXISTS (SELECT 1 FROM sys.databases WHERE name = 'pennyguard')
BEGIN
    CREATE DATABASE pennyguard;
END;

USE pennyguard;

IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'user')
BEGIN
    CREATE TABLE [user] (
        id BIGINT IDENTITY PRIMARY KEY,
        firstname VARCHAR(255) NOT NULL,
        lastname VARCHAR(255) NOT NULL,
        email VARCHAR(255) UNIQUE NOT NULL,
        password VARCHAR(255) NOT NULL,
        public_key TEXT
    );
END;

-- Create the transaction_type table first
IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'transaction_type')
BEGIN
    CREATE TABLE transaction_type (
        id BIGINT IDENTITY PRIMARY KEY,
        type NVARCHAR(100) NOT NULL
    );

    INSERT INTO transaction_type (type) VALUES (N'доходы'), (N'расходы');
END;

IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'category')
BEGIN
    CREATE TABLE category (
        id BIGINT IDENTITY PRIMARY KEY,
        name NVARCHAR(100) NOT NULL,
        is_default BIT NOT NULL,
        user_id BIGINT,
        CONSTRAINT name_user_id_unique UNIQUE (name, user_id),
        FOREIGN KEY (user_id) REFERENCES [user](id)
    );

    -- Inserting some categories with names in Russian
    INSERT INTO category (name, is_default, user_id) VALUES
    (N'Еда', 1, null),
    (N'Путешествия', 1, null),
    (N'Здоровье', 1, null),
    (N'Красота', 1, null),
    (N'Транспорт', 1, null),
    (N'Развлечения', 1, null),
    (N'Покупки', 1, null),
    (N'Образование', 1, null),
    (N'Банк', 1, null),
    (N'Персональное', 1, null);
END;

IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'category_limit')
BEGIN
    CREATE TABLE category_limit (
        id BIGINT IDENTITY PRIMARY KEY,
        user_id BIGINT NOT NULL,
        category_id BIGINT,
        amount FLOAT NOT NULL,
        salary_day INT NOT NULL,
        FOREIGN KEY (user_id) REFERENCES [user](id),
        FOREIGN KEY (category_id) REFERENCES category(id)
    );
END;

-- Create the transaction table after transaction_type table
IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'transaction')
BEGIN
    CREATE TABLE [transaction] (
        id BIGINT IDENTITY PRIMARY KEY,
        user_id BIGINT NOT NULL,
        category_id BIGINT NOT NULL,
        type_id BIGINT NOT NULL,
        created_at DATETIME NOT NULL,
        FOREIGN KEY (user_id) REFERENCES [user](id),
        FOREIGN KEY (category_id) REFERENCES category(id),
        FOREIGN KEY (type_id) REFERENCES transaction_type(id) -- Reference transaction_type table
    );
END;

IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'notification')
BEGIN
    CREATE TABLE notification (
        id BIGINT IDENTITY PRIMARY KEY,
        user_id BIGINT NOT NULL,
        message TEXT NOT NULL,
        FOREIGN KEY (user_id) REFERENCES [user](id)
    );
END;

IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'financial_goal')
BEGIN
    CREATE TABLE financial_goal (
        id BIGINT IDENTITY PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        sum DECIMAL(18, 2) NOT NULL,
        start_date DATETIME NOT NULL,
        end_date DATETIME NOT NULL,
        user_id BIGINT NOT NULL,
        FOREIGN KEY (user_id) REFERENCES [user](id)
    );
END;
