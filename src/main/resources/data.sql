INSERT INTO users (username, password, role)
VALUES ('admin', 'admin123', 'ADMIN'),
       ('john', 'john123', 'USER'),
       ('jane', 'jane123', 'USER');

INSERT INTO messages (user_id, content)
VALUES (1, 'Hello everyone! Welcome to the chat room.'),
       (2, 'Hi there! Nice to meet you all.'),
       (3, 'Good morning! How is everyone doing today?'),
       (2, 'This is a great chat application!');
