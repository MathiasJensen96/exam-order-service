CREATE TABLE camunda_order_task
(
    system_order_id     INT NOT NULL,
    process_id          VARCHAR(255) NULL,
    task_definition_key VARCHAR(255) NULL,
    task_id             VARCHAR(255) NULL,
    worker_id           VARCHAR(255) NULL,
    CONSTRAINT pk_camundaordertask PRIMARY KEY (system_order_id)
);

CREATE TABLE `order`
(
    id            INT NOT NULL,
    customer_id   INT NULL,
    restaurant_id INT NULL,
    created_at    datetime NULL,
    status        VARCHAR(255) NULL,
    order_price DOUBLE NULL,
    delivery_price DOUBLE NULL,
    with_delivery BIT(1) NULL,
    courier_id    INT NULL,
    process_id    VARCHAR(255) NULL,
    CONSTRAINT pk_order PRIMARY KEY (id)
);

CREATE TABLE order_item
(
    id           INT NOT NULL,
    menu_item_id INT NULL,
    name         VARCHAR(255) NULL,
    price DOUBLE NULL,
    amount       INT NULL,
    order_id     INT NULL,
    CONSTRAINT pk_order_item PRIMARY KEY (id)
);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_ORDER FOREIGN KEY (order_id) REFERENCES `order` (id);