insert into your_database.portefeuille (username, solde, rank_id)
values ('Alex', 6526.349999999999, null);

insert into your_database.mouvement (mouvement_id, price, quantity, ticker, time, type, portefeuille_username)
values (1, 399.12, 6, 'MSFT', '2024-04-21 10:25:22.010472', 0, null),
       (3, 399.12, 6, 'MSFT', '2024-04-21 10:25:29.486242', 1, null),
       (4, 389.07, 6, 'MSFT', '2024-04-21 10:28:34.831280', 0, null),
       (5, 389.07, 6, 'MSFT', '2024-04-21 10:28:34.831280', 0, null),
       (6, 553.02, 1, 'NFLX', '2024-04-21 10:45:14.386241', 0, null),
       (7, 553.02, 1, 'NFLX', '2024-04-21 10:45:14.386241', 0, null),
       (8, 174.99, 3, 'AMZN', '2024-04-21 10:48:13.112181', 0, null),
       (9, 174.99, 3, 'AMZN', '2024-04-21 10:48:13.112181', 0, null);

insert into your_database.portefeuille_actions (portefeuille_username, actions_mouvement_id)
values ('Alex', 5),
       ('Alex', 7),
       ('Alex', 9);

insert into your_database.portefeuille_historique (portefeuille_username, historique_mouvement_id)
values ('Alex', 1),
       ('Alex', 3),
       ('Alex', 4),
       ('Alex', 6),
       ('Alex', 8);

insert into your_database.performance_history (id, date, percentage, username, value)
values (1, '2024-04-21 10:25:38.439767', '0.00%', 'Alex', 0),
       (2, '2024-04-20 10:25:38.439000', '-2.1%', 'Alex', -210),
       (3, '2024-04-19 10:25:38.439000', '-1.1%', 'Alex', -110),
       (4, '2024-04-18 10:25:38.439000', '1.7%', 'Alex', 170),
       (5, '2024-04-17 10:25:38.439000', '0.8%', 'Alex', 80),
       (6, '2024-04-16 10:25:38.439000', '0.2%', 'Alex', 20),
       (7, '2024-04-15 10:25:38.439000', '-0.5%', 'Alex', -50);

DELETE
FROM your_database.performance_history_seq
WHERE next_val = 1;
insert into your_database.performance_history_seq (next_val)
values (451);

insert into your_database.favoris (username, favori)
values ('Alex', 'MSFT'),
       ('Alex', 'AMZN'),
       ('Alex', 'AAPL');