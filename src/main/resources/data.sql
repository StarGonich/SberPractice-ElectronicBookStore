INSERT INTO public.books VALUES (6, 'Книга', 'Я', 'никакой', 2002, 'гангста', 'крутая книга', '999999999', 256, NULL, true, '', 300.00, 1);
INSERT INTO public.books VALUES (4, 'Совершенный код', 'Стив Макконнелл', 'Русский', 2004, 'Учебная литература', 'Энциклопедия разработки ПО', '978-5-7502-0064-1', 896, 9.8, false, 'code.jpg', 1299.00, 0);
INSERT INTO public.books VALUES (5, 'Убийство в Восточном экспрессе', 'Агата Кристи', 'Русский', 1934, 'Детектив', 'Знаменитый роман о расследовании Эркюля Пуаро', '978-5-17-090875-4', 256, 8.7, false, 'orient.jpg', 499.00, 4);
INSERT INTO public.books VALUES (2, '1984', 'Джордж Оруэлл', 'Английский', 1949, 'Антиутопия', 'Роман о тоталитарном обществе', '978-0-452-28423-4', 328, 9.2, false, '1984.jpg', 450.00, 9);
INSERT INTO public.books VALUES (3, 'Преступление и наказание', 'Фёдор Достоевский', 'Русский', 1866, 'Классическая литература', 'Один из образов «Преступления и наказания» — большой город второй половины XIX века, жизнь в котором полна конфликтов и драм. Но то, что происходит в душах жителей этого города, оказывается гораздо масштабнее. Об убийстве Раскольниковым старухи-процентщицы слышали даже те, кто так и не открыл эту книгу. Но о том, что привело к трагедии, и особенно о том, что происходило с героем после нее, могут рассказать лишь поверхностно даже те, кто роман читал. Парадокс! Обусловленный невероятной психологической глубиной, на которую погрузился автор, исследуя проблему «сильной личности», не боящейся угрызений совести и людского суда. И огромным космосом человеческой души, который он оттуда достал.', '978-5-04-106835-4', 608, 9.0, false, 'crime.jpg', 399.00, 7);
INSERT INTO public.books VALUES (7, 'Ещё одна новая', 'Я', 'никакой', 2025, 'гангста', 'крутая книга', '999999998', 1, 10.0, NULL, '1', 199.99, 90);
INSERT INTO public.books VALUES (1, 'Мастер и Маргарита', 'Михаил Булгаков', 'Русский', 1967, 'Художественная литература', 'Философский роман о добре и зле', '978-5-699-12014-7', 384, 9.5, false, 'master.jpg', 599.00, 0);

INSERT INTO public.clients VALUES (3, 'employee', 'e@e.ru', '$2a$10$R5cf6r86v2ZKQ5HPSjNxlu7hVliVXzXDtgxCRr7X3/UM92SERO/Sm', NULL, NULL);
INSERT INTO public.clients VALUES (6, 'Nickname123', 'asdasdsad@asdasdasd.ru', '$2a$10$q9GNWmfHb6olXHVVuGZe0ON/RFlUi2BFY5cQEF2XelNZr0spw3/ay', NULL, NULL);
INSERT INTO public.clients VALUES (7, 'user', 'a@h', '$2a$10$/bNqMTpTZ/sjFLdcPwZzo.v1j6eMyNN7JDl3rjO2D6NPfrjrz7uHK', NULL, NULL);
INSERT INTO public.clients VALUES (2, 'Nickname', 'Куда-нибудь', '$2a$10$9oFLizM.X8ccL9wTIqciee.47qJ0NeGKdNeoSkPPEEbcusS1Th8IW', '+8888888888', 'ул. Куда-нибудь, 2');
INSERT INTO public.clients VALUES (8, 'Alexey', 'a@aaa.ru', '$2a$10$n77wD9xhZ.hAaTg.59kxuO3VvrqHkUNyCVnfjg3ZHnpjz/OjrQ9lC', '+7900000000', 'ул. Куда-нибудь, 3');
INSERT INTO public.clients VALUES (9, 'StarGonich', 'a@ya.ru', '$2a$10$UEquLYVsVJtWSYJ8ZUMKH.WA08rb/8DUmVzcR.HKh/jgEpoPDrfz6', '+79008888888', 'Туда');
INSERT INTO public.clients VALUES (11, 'Velichayshiy', 'vel@ya.ru', '$2a$10$RwjM/pnF9PGnQPxvRVzhx.pxMu5.43C3rWP8.8QvD2wnYByUl61Da', '+790000000000', 'Куда-нибудь');

INSERT INTO public.employees VALUES (4, 'employee3', 'e@e3', '$2a$10$b9DBwhAChOMEA31A8rh8h.FKL.JfJF9jgzIFSbeu6P6V1OrQV4Fxm');
INSERT INTO public.employees VALUES (2, 'employee1', 'e@e.ru', '$2a$10$LCVIYiKdhe7KMyfIybmvtugu4BixRPMpxUuFJpm3.NZH5TrlmHR4S');
INSERT INTO public.employees VALUES (3, 'employee2', 'employee2@e.ru', '$2a$10$mx6hvMijttUw5fjUrVZlye8VZ1gB1u4kFIQBfyHj2Nm98vMkzmWaC');
INSERT INTO public.employees VALUES (5, 'StarGonich', 'alexey8837@yandex.ru', '$2a$10$yhHa.l95qAwF4AFF.EBBKemHSusFhjF6kjkkrw.ku0VeC5HoFCUEi');

INSERT INTO public.orders VALUES (21, 2, 7, 1, 'shipped', '2025-07-03 18:41:12.798644+03');
INSERT INTO public.orders VALUES (22, 2, 7, 3, 'shipped', '2025-07-03 19:08:29.09892+03');
INSERT INTO public.orders VALUES (23, 9, 4, 7, 'shipped', '2025-07-08 14:06:05.859808+03');
INSERT INTO public.orders VALUES (24, 9, 7, 2, 'shipped', '2025-07-08 14:06:05.859808+03');
INSERT INTO public.orders VALUES (25, 11, 7, 3, 'shipped', '2025-07-08 14:17:52.701159+03');
INSERT INTO public.orders VALUES (26, 11, 1, 1, 'shipped', '2025-07-08 14:17:52.701159+03');
INSERT INTO public.orders VALUES (27, 11, 6, 1, 'in_cart', '2025-07-08 14:20:26.159259+03');