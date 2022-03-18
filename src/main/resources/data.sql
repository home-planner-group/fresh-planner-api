INSERT
IGNORE INTO products (id, name)
VALUES (1, 'Apfel'),
       (2, 'Toast');

INSERT
IGNORE INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_EDITOR'),
       ('ROLE_ADMIN');

INSERT
IGNORE INTO users(name, email, password)
    VALUE ('Admin', 'steinke.felix@yahoo.de', '$2a$10$TXUz/.yyH.EAF9XjtLlJeeNjJEyjoCZ.zyNZRMPKQ7GSWHf6QuB8a');

INSERT
IGNORE INTO user_roles(user_id, role_id)
VALUES ('Admin', 'ROLE_USER'),
       ('Admin', 'ROLE_EDITOR'),
       ('Admin', 'ROLE_ADMIN');
