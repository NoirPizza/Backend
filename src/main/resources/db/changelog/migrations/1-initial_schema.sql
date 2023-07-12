--liquibase formatted sql
--changeset w1ldy0uth:1

-- TODO: create table for authenticating

CREATE TABLE "user"
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(20)        NOT NULL,
    surname     VARCHAR(40),
    phoneNumber VARCHAR(12) UNIQUE NOT NULL, -- supports bot 8*** and +7*** formats
    email       VARCHAR(50),
    birthday    VARCHAR(10)                  -- dd.mm.yyyy
);
CREATE INDEX idx_phoneNumber ON "user" (phoneNumber);

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
    type VARCHAR(15) NOT NULL
);
INSERT INTO order_status (id, type)
VALUES (1, 'Обрабатывается'),
       (2, 'Создан'),
       (3, 'готовится'),
       (4, 'готов'),
       (5, 'доставляется'),
       (6, 'доставлен');

CREATE TABLE delivery_type
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(15) NOT NULL
);
INSERT INTO delivery_type (id, type)
VALUES (1, 'Самовывоз'),
       (2, 'Доставка');

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
VALUES (1, 'Целая'),
       (2, 'Половина'),
       (3, 'Четверть'),
       (4, 'Кусок');

CREATE TABLE size_to_price_correlation
(
    id          SERIAL PRIMARY KEY,
    type        VARCHAR(20) NOT NULL,
    coefficient NUMERIC
);
INSERT INTO size_to_price_correlation (id, type, coefficient)
VALUES (1, 'Стандарт (33 см)', 1),
       (2, 'Мини (25 см)', 0.8),
       (3, 'Большая (40 см)', 1.2);

CREATE TABLE dough
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(15) NOT NULL
);
INSERT INTO dough (id, type)
VALUES (1, 'Тонкое'),
       (2, 'Толстое');

CREATE TABLE base
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(15) NOT NULL
);
INSERT INTO base (id, type)
VALUES (1, 'Томатная'),
       (2, 'Сливочная');

CREATE TABLE ordered_pizza
(
    id      SERIAL PRIMARY KEY,
    pizzaId INTEGER NOT NULL,
    sizeId  INTEGER NOT NULL,
    doughId INTEGER NOT NULL,
    baseId  INTEGER NOT NULL,
    partId  INTEGER NOT NULL,
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
    FOREIGN KEY (orderedPizzaId) REFERENCES "ordered_pizza" ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ingredientId) REFERENCES "pizza_ingredient" ON DELETE CASCADE ON UPDATE CASCADE
);
