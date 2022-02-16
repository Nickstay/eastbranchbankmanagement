DELETE FROM accounts."user" WHERE id > 2;

insert into accounts."user" (id, email, username, password, role, active)
VALUES (3, 'test@room.try', 'Tutor', '$2a$10$BCZDcVVuQfRwu6JRw4Z3I.wZ2F7Bx5kiqXD1ay4LbYZQwdDY5VPuy',
        'ROLE_CLIENT', true);

insert into accounts."user" (id, email, username, password, role, active)
VALUES (4, 'test2@room.try', 'Tutyer', '$2a$10$BCZDcVVuQfRwu6JRw4Z3I.wZ2F7Bx5kiqXD1ay4LbYZQwdDY5VPuy',
        'ROLE_CLIENT', true);

ALTER SEQUENCE accounts.user_id_seq RESTART WITH 5;