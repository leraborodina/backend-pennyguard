-- CREATE TABLE statement for the "user" table

CREATE TABLE "user" (
    -- Primary key column: id
    id INT PRIMARY KEY IDENTITY(1,1),
    
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
