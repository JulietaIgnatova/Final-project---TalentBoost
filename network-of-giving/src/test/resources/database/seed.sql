CREATE TABLE IF NOT EXISTS Users(
    id INT IDENTITY PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    username VARCHAR(128) NOT NULL UNIQUE,
    age INT NOT NULL,
    gender VARCHAR(1),
    location VARCHAR(128),
     password VARCHAR(128),
     role VARCHAR(128)
);

CREATE TABLE IF NOT EXISTS Charities(
    id INT IDENTITY PRIMARY KEY,
    creator_id INT NOT NULL,
    title VARCHAR(128) NOT NULL UNIQUE,
    description VARCHAR(4096) NOT NULL,
    budget_required DECIMAL  NOT NULL,
    amount_collected DECIMAL NOT NULL ,
    volunteers_required INT NOT NULL,
    volunteers_signed_up INT NOT NULL,
    image BYTEA
);

CREATE TABLE IF NOT EXISTS Participants(
    user_id INT,
    charity_id INT,
    participant_date timestamp default now()
);

CREATE TABLE IF NOT EXISTS Donators(
    user_id INT,
    charity_id INT,
    donated_money DECIMAL,
    donation_date timestamp default now()
);

CREATE TABLE IF NOT EXISTS Useractions(
   user_id INT,
   description VARCHAR(4096),
   charity_title VARCHAR(128),
   action_date timestamp default now()
);

INSERT INTO Users (name,username,age,gender,location, password,role)
VALUES
('Maria','maria',21,'F','Sofia','$2a$10$qW/B129pg21XPYR6XA8B.OQg4UGsYesbRpYPdgt9rhzxWWMn11bHC','ROLE_USER'),
('Martin','martin',22,'M','Plovdiv','$2a$10$qW/B129pg21XPYR6XA8B.OQg4UGsYesbRpYPdgt9rhzxWWMn11bHC','ROLE_USER');

INSERT INTO Charities(creator_id, title, description, budget_required, amount_collected,volunteers_required, volunteers_signed_up)
VALUES
(1,'save the world','we are going to clean the world',10000,200,20,10),
(2,'better world','good people',10000,200,20,10);

INSERT INTO Participants(user_id, charity_id)
values
(1,1),
(1,2),
(2,2);

INSERT INTO Donators(user_id, charity_id, donated_money)
values
(1,1,200.0),
(1,2,300.0),
(2,1,350);
