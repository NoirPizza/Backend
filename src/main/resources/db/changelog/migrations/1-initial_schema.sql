--liquibase formatted sql
--changeset w1ldy0uth:1

CREATE TABLE "user"
(
    id          SERIAL PRIMARY KEY,
    login       VARCHAR(255) UNIQUE NOT NULL,
    password    VARCHAR(255)        NOT NULL, -- bcrypt
    name        VARCHAR(20)         NOT NULL,
    surname     VARCHAR(40),
    phoneNumber VARCHAR(12) UNIQUE  NOT NULL, -- supports bot 8*** and +7*** formats
    email       VARCHAR(50) UNIQUE,
    birthday    VARCHAR(10),                  -- dd.mm.yyyy
    created_at  TIMESTAMP           NOT NULL,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_phoneNumber ON "user" (phoneNumber);
CREATE INDEX idx_email ON "user" (email);

CREATE TABLE role
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);
INSERT INTO role (id, name)
VALUES (1, 'USER'),
       (2, 'ADMIN');

CREATE TABLE role_on_user
(
    id     SERIAL PRIMARY KEY,
    userId INTEGER,
    roleId INTEGER,
    FOREIGN KEY (userId) REFERENCES "user" ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (roleId) REFERENCES "role" ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE feedback
(
    id      SERIAL PRIMARY KEY,
    userId  INTEGER NOT NULL,
    grade   INTEGER,
    comment TEXT,
    FOREIGN KEY (userId) REFERENCES "user" ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE address
(
    id         SERIAL PRIMARY KEY,
    userId     INTEGER     NOT NULL,
    street     varchar(50) NOT NULL,
    building   varchar(10) NOT NULL,
    entrance   varchar(5),
    floor      INTEGER,
    flatNumber INTEGER,
    FOREIGN KEY (userId) REFERENCES "user" ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE pizza
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) UNIQUE NOT NULL,
    weight      INTEGER             NOT NULL,
    price       INTEGER             NOT NULL,
    description TEXT,
    image       TEXT                NOT NULL
);

CREATE TABLE pizza_ingredient
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(40) UNIQUE NOT NULL,
    addPrice INTEGER            NOT NULL
);

CREATE TABLE ingredient_on_pizza
(
    id           SERIAL PRIMARY KEY,
    pizzaId      INTEGER NOT NULL,
    ingredientId INTEGER NOT NULL,
    FOREIGN KEY (pizzaId) REFERENCES "pizza" ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ingredientId) REFERENCES "pizza_ingredient" ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE order_status
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(20) NOT NULL
);
INSERT INTO order_status (id, type)
VALUES (1, 'Pending'),
       (2, 'Created'),
       (3, 'Cooking'),
       (4, 'Ready to deliver'),
       (5, 'Delivering'),
       (6, 'Done');

CREATE TABLE delivery_type
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(15) NOT NULL
);
INSERT INTO delivery_type (id, type)
VALUES (1, 'Pickup'),
       (2, 'Delivery');

CREATE TABLE "order"
(
    id             SERIAL PRIMARY KEY,
    orderNumber    VARCHAR(4),
    statusId       INTEGER   NOT NULL,
    userId         INTEGER,
    userPhone      VARCHAR(12),            -- same as in user TABLE. unwanted duplication of filed comes domain logic
    deliveryTypeId INTEGER   NOT NULL,
    deliveryTime   TIMESTAMP DEFAULT NULL, -- if null, then order should be delivered ASAP
    date           TIMESTAMP NOT NULL,
    totalPrice     INTEGER,
    cutlery        INT       NOT NULL,
    address        TEXT      NOT NULL,
    note           TEXT,
    FOREIGN KEY (statusId) REFERENCES "order_status" ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (deliveryTypeId) REFERENCES "delivery_type" ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (userId) REFERENCES "user" ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE part
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(15) NOT NULL
);
INSERT INTO part (id, type)
VALUES (1, 'Full'),
       (2, 'Half'),
       (3, 'Quarter'),
       (4, 'Piece');

CREATE TABLE size_to_price_correlation
(
    id          SERIAL PRIMARY KEY,
    type        VARCHAR(20) NOT NULL,
    coefficient NUMERIC
);
INSERT INTO size_to_price_correlation (id, type, coefficient)
VALUES (1, 'Standard (33sm)', 1),
       (2, 'Mini (25sm)', 0.8),
       (3, 'Huge (40sm)', 1.2);

CREATE TABLE dough
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(15) NOT NULL
);
INSERT INTO dough (id, type)
VALUES (1, 'Thin'),
       (2, 'Thick');

CREATE TABLE base
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(15) NOT NULL
);
INSERT INTO base (id, type)
VALUES (1, 'Tomato'),
       (2, 'Creamy');

CREATE TABLE ordered_pizza
(
    id      SERIAL PRIMARY KEY,
    pizzaId INTEGER NOT NULL,
    sizeId  INTEGER NOT NULL,
    doughId INTEGER NOT NULL,
    baseId  INTEGER NOT NULL,
    partId  INTEGER NOT NULL,
    amount  INTEGER NOT NULL,
    FOREIGN KEY (pizzaId) REFERENCES "pizza" ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (sizeId) REFERENCES "size_to_price_correlation" ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (doughId) REFERENCES "dough" ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (baseId) REFERENCES "base" ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (partId) REFERENCES "part" ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE ordered_pizza_addition
(
    id             SERIAL PRIMARY KEY,
    orderedPizzaId INTEGER NOT NULL,
    ingredientId   INTEGER NOT NULL,
    amount         INTEGER NOT NULL,
    FOREIGN KEY (orderedPizzaId) REFERENCES "ordered_pizza" ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ingredientId) REFERENCES "pizza_ingredient" ON DELETE CASCADE ON UPDATE CASCADE
);
