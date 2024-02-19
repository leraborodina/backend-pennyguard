-- CREATE TABLE statement for the "user" table

CREATE TABLE "user" (
    -- Primary key column: id
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    
    -- User's first name column: firstname
    firstname VARCHAR(100) NOT NULL,
    
    -- User's last name column: lastname
    lastname VARCHAR(100) NOT NULL,
    
    -- User's email column: email
    email VARCHAR(100) UNIQUE NOT NULL,
    
    -- User's password column: password
    password VARCHAR(255) NOT NULL,
    
    -- User's public key column: public_key
    -- The VARCHAR(MAX) is used to store longer strings, assuming it's for the public key
    public_key VARCHAR(MAX) NULL
);

CREATE TABLE "category" (
    -- Primary key column: id
    id BIGINT PRIMARY KEY IDENTITY(1,1),

    -- User's category name column: value
    category VARCHAR(100) NOT NULL
);

CREATE TABLE "transaction_type" (
    -- Primary key column: id
    id BIGINT PRIMARY KEY IDENTITY(1,1),

    -- User's transaction type column: type
    type VARCHAR(100) NOT NULL
);

CREATE TABLE "transaction" (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    category_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    transaction_type_id BIGINT NOT NULL,
    date DATETIME NOT NULL,
    amount DECIMAL NOT NULL,
    purpose VARCHAR(255),
    regular BIT NOT NULL,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user"(id),
    CONSTRAINT fk_transaction_type FOREIGN KEY (transaction_type_id) REFERENCES transaction_type(id)
);
