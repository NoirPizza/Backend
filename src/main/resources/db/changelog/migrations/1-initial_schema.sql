--liquibase formatted sql
--changeset w1ldy0uth:1

-- TODO: create table for authenticating

create table "user"
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(20)        NOT NULL,
    surname     VARCHAR(40),
    phoneNumber VARCHAR(12) UNIQUE NOT NULL, -- supports bot 8*** and +7*** formats
    email       VARCHAR(50),
    birthday    VARCHAR(10)                  -- dd.mm.yyyy
);
CREATE INDEX idx_phoneNumber ON "user" (phoneNumber);

create table feedback
(
    id      SERIAL PRIMARY KEY,
    userId  INTEGER NOT NULL,
    grade   INTEGER,
    comment TEXT,
    FOREIGN KEY (userId) REFERENCES "user" ON DELETE CASCADE ON UPDATE CASCADE
);

create table address
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
    image       TEXT
);

create table ingredient
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(40) UNIQUE NOT NULL,
    addPrice INTEGER            NOT NULL
);

create table ingredient_on_pizza
(
    id           SERIAL PRIMARY KEY,
    pizzaId      INTEGER NOT NULL,
    ingredientId INTEGER NOT NULL,
    FOREIGN KEY (pizzaId) REFERENCES "pizza" ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ingredientId) REFERENCES "ingredient" ON DELETE CASCADE ON UPDATE CASCADE
);

-- default обрабатывается, создан, готовится, готов, доставляется, доставлен
create table order_status
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(15) NOT NULL
);

-- default самовывоз, доставка
create table delivery_type
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(15) NOT NULL
);

create table "order"
(
    id             SERIAL PRIMARY KEY,
    orderNumber    VARCHAR(4),
    statusId       INTEGER   NOT NULL,
    userId         INTEGER,
    userPhone      VARCHAR(12),            -- same as in user table. unwanted duplication of filed comes domain logic
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

-- default целая, половина, четверть, кусок
create table part
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(15) NOT NULL
);

-- default стандарт (33), мини (25), большая (40)
create table size_to_price_correlation
(
    id          SERIAL PRIMARY KEY,
    type        VARCHAR(15) NOT NULL,
    coefficient INTEGER
);

-- default тонкое, толстое
create table dough
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(15) NOT NULL
);

-- default томатная, сливочная
create table base
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(15) NOT NULL
);

create table ordered_pizza
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

create table ordered_pizza_addition
(
    id             SERIAL PRIMARY KEY,
    orderedPizzaId INTEGER NOT NULL,
    ingredientId   INTEGER NOT NULL,
    FOREIGN KEY (orderedPizzaId) REFERENCES "ordered_pizza" ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ingredientId) REFERENCES "ingredient" ON DELETE CASCADE ON UPDATE CASCADE
);
