INSERT IGNORE INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_EDITOR'),
       ('ROLE_ADMIN');

INSERT IGNORE INTO users(name, email, password)
    VALUE ('Admin', 'example@mal.de', '$2a$10$TXUz/.yyH.EAF9XjtLlJeeNjJEyjoCZ.zyNZRMPKQ7GSWHf6QuB8a');

INSERT IGNORE INTO user_roles(user_id, role_id)
VALUES ('Admin', 'ROLE_USER'),
       ('Admin', 'ROLE_EDITOR'),
       ('Admin', 'ROLE_ADMIN');

INSERT IGNORE INTO products (id, name, category, package_size)
VALUES (1, 'Penne', 'Pasta', 500),
       (2, 'Spaghetti', 'Pasta', 500),
       (3, 'Farfalle', 'Pasta', 500),
       (4, 'Paprika', 'Vegetable', 1),
       (5, 'Tomato', 'Vegetable', 1),
       (6, 'Mozzarella', 'Cheese', 1),
       (7, 'Wrap', 'Bread', 6),
       (8, 'Cereals', 'Breakfast', 300),
       (9, 'Tomato Pesto', 'Pasta', 1),
       (10, 'Yoghurt', 'Breakfast', 500);

INSERT IGNORE INTO storages (id, name)
    VALUE (1, 'Admin Storage');

INSERT IGNORE INTO user_storages (storage_id, user_id)
    VALUE (1, 'Admin');

INSERT IGNORE INTO storage_items (storage_id, product_id, count)
VALUES (1, 1, 1),
       (1, 2, 1),
       (1, 3, 1);

INSERT IGNORE INTO carts (id, name)
    VALUE (1, 'Admin Cart');

INSERT IGNORE INTO user_carts (cart_id, user_id)
    VALUE (1, 'Admin');

INSERT IGNORE INTO cart_items (cart_id, product_id, count)
VALUES (1, 1, 1),
       (1, 2, 1),
       (1, 3, 1);

INSERT IGNORE INTO recipes (id, name, category, description, duration)
VALUES (1, 'Wraps', 'Quick and Healthy', 'Take everything and roll it into the bread.', 10),
       (2, 'Spaghetti with Pesto', 'Pasta', 'Cook for 10 min and put pesto on it.', 10),
       (3, 'Penne with Pesto', 'Pasta', 'Cook for 10 min and put pesto on it.', 10),
       (4, 'Cereal with Yoghurt', 'Breakfast', 'Put everything together.', 3);

INSERT IGNORE INTO recipe_items (recipe_id, product_id, count, description)
VALUES (1, 4, 1, 'A big one.'),
       (1, 5, 2, 'Two normal ones or a pack of small pieces.'),
       (1, 6, 1, null),
       (1, 7, 4, '1 each portion.'),
       (2, 2, 125, 'One package for 4 portions.'),
       (2, 9, 1, null),
       (3, 3, 125, 'One package for 4 portions.'),
       (3, 9, 1, null),
       (4, 8, 75, null),
       (4, 10, 100, null);
