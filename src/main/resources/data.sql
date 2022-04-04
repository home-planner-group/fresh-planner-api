INSERT
IGNORE INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_EDITOR'),
       ('ROLE_ADMIN');

INSERT
IGNORE INTO users(name, email, password)
    VALUE ('Admin', 'example@mal.de', '$2a$10$TXUz/.yyH.EAF9XjtLlJeeNjJEyjoCZ.zyNZRMPKQ7GSWHf6QuB8a');

INSERT
IGNORE INTO user_roles(user_id, role_id)
VALUES ('Admin', 'ROLE_USER'),
       ('Admin', 'ROLE_EDITOR'),
       ('Admin', 'ROLE_ADMIN');

INSERT
IGNORE INTO products (id, name, category)
VALUES  (1, 'Penne', 'Pasta'),
        (2, 'Fusilli', 'Pasta'),
        (3, 'Farfalle', 'Pasta'),
        (4, 'Paprika', 'Vegetable'),
        (5, 'Tomato', 'Vegetable'),
        (6, 'Mozzarella', 'Cheese'),
        (7, 'Wrap', 'Bread');

INSERT
IGNORE INTO recipes (id, name, category, description, duration)
VALUES (1, 'Wraps', 'Quick and Healthy', 'Take everything and roll it into the bread.', 10);

INSERT
IGNORE INTO recipe_items (recipe_id, product_id, count, description)
VALUES  (1, 4, 1, 'A big one.'),
        (1,5,2, 'Two normal ones or a pack of small pieces.'),
        (1,6,1, null),
        (1,7,4, '1 each portion.');
