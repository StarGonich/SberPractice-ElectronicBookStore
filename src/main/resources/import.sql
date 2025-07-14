--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5
-- Dumped by pg_dump version 17.5

-- Started on 2025-07-14 12:15:38

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 862 (class 1247 OID 16426)
-- Name: order_status; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.order_status AS ENUM (
    'in_cart',
    'pending',
    'processing',
    'shipped',
    'delivered',
    'cancelled'
);


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 218 (class 1259 OID 16393)
-- Name: books; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.books (
    id integer NOT NULL,
    title character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    language character varying(63) NOT NULL,
    publication_year integer NOT NULL,
    genre character varying(100),
    description text,
    isbn character varying(20),
    page_count integer,
    rating numeric(3,1),
    is_new boolean DEFAULT false,
    image_path character varying(255),
    price numeric(10,2) NOT NULL,
    stock_quantity integer NOT NULL,
    CONSTRAINT books_rating_check CHECK (((rating >= (0)::numeric) AND (rating <= (10)::numeric)))
);


--
-- TOC entry 217 (class 1259 OID 16392)
-- Name: books_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.books_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4940 (class 0 OID 0)
-- Dependencies: 217
-- Name: books_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.books_id_seq OWNED BY public.books.id;


--
-- TOC entry 220 (class 1259 OID 16406)
-- Name: clients; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.clients (
    id integer NOT NULL,
    nickname character varying(50) NOT NULL,
    email character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    phone character varying(20),
    address text
);


--
-- TOC entry 219 (class 1259 OID 16405)
-- Name: clients_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.clients_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4941 (class 0 OID 0)
-- Dependencies: 219
-- Name: clients_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.clients_id_seq OWNED BY public.clients.id;


--
-- TOC entry 222 (class 1259 OID 16417)
-- Name: employees; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.employees (
    id integer NOT NULL,
    nickname character varying(50) NOT NULL,
    email character varying(50) NOT NULL,
    password character varying(255) NOT NULL
);


--
-- TOC entry 221 (class 1259 OID 16416)
-- Name: employees_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.employees_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4942 (class 0 OID 0)
-- Dependencies: 221
-- Name: employees_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.employees_id_seq OWNED BY public.employees.id;


--
-- TOC entry 224 (class 1259 OID 16440)
-- Name: orders; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.orders (
    id integer NOT NULL,
    client_id integer NOT NULL,
    book_id integer NOT NULL,
    count integer DEFAULT 1 NOT NULL,
    status character varying(25) DEFAULT 'in_cart'::public.order_status NOT NULL,
    date timestamp with time zone
);


--
-- TOC entry 223 (class 1259 OID 16439)
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.orders_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4943 (class 0 OID 0)
-- Dependencies: 223
-- Name: orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;


--
-- TOC entry 4760 (class 2604 OID 16396)
-- Name: books id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.books ALTER COLUMN id SET DEFAULT nextval('public.books_id_seq'::regclass);


--
-- TOC entry 4762 (class 2604 OID 16409)
-- Name: clients id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.clients ALTER COLUMN id SET DEFAULT nextval('public.clients_id_seq'::regclass);


--
-- TOC entry 4763 (class 2604 OID 16420)
-- Name: employees id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employees ALTER COLUMN id SET DEFAULT nextval('public.employees_id_seq'::regclass);


--
-- TOC entry 4764 (class 2604 OID 16443)
-- Name: orders id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);


--
-- TOC entry 4928 (class 0 OID 16393)
-- Dependencies: 218
-- Data for Name: books; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.books VALUES (6, 'Книга', 'Я', 'никакой', 2002, 'гангста', 'крутая книга', '999999999', 256, NULL, true, '', 300.00, 1);
INSERT INTO public.books VALUES (4, 'Совершенный код', 'Стив Макконнелл', 'Русский', 2004, 'Учебная литература', 'Энциклопедия разработки ПО', '978-5-7502-0064-1', 896, 9.8, false, 'code.jpg', 1299.00, 0);
INSERT INTO public.books VALUES (5, 'Убийство в Восточном экспрессе', 'Агата Кристи', 'Русский', 1934, 'Детектив', 'Знаменитый роман о расследовании Эркюля Пуаро', '978-5-17-090875-4', 256, 8.7, false, 'orient.jpg', 499.00, 4);
INSERT INTO public.books VALUES (2, '1984', 'Джордж Оруэлл', 'Английский', 1949, 'Антиутопия', 'Роман о тоталитарном обществе', '978-0-452-28423-4', 328, 9.2, false, '1984.jpg', 450.00, 9);
INSERT INTO public.books VALUES (3, 'Преступление и наказание', 'Фёдор Достоевский', 'Русский', 1866, 'Классическая литература', 'Один из образов «Преступления и наказания» — большой город второй половины XIX века, жизнь в котором полна конфликтов и драм. Но то, что происходит в душах жителей этого города, оказывается гораздо масштабнее. Об убийстве Раскольниковым старухи-процентщицы слышали даже те, кто так и не открыл эту книгу. Но о том, что привело к трагедии, и особенно о том, что происходило с героем после нее, могут рассказать лишь поверхностно даже те, кто роман читал. Парадокс! Обусловленный невероятной психологической глубиной, на которую погрузился автор, исследуя проблему «сильной личности», не боящейся угрызений совести и людского суда. И огромным космосом человеческой души, который он оттуда достал.', '978-5-04-106835-4', 608, 9.0, false, 'crime.jpg', 399.00, 7);
INSERT INTO public.books VALUES (7, 'Ещё одна новая', 'Я', 'никакой', 2025, 'гангста', 'крутая книга', '999999998', 1, 10.0, NULL, '1', 199.99, 90);
INSERT INTO public.books VALUES (1, 'Мастер и Маргарита', 'Михаил Булгаков', 'Русский', 1967, 'Художественная литература', 'Философский роман о добре и зле', '978-5-699-12014-7', 384, 9.5, false, 'master.jpg', 599.00, 0);


--
-- TOC entry 4930 (class 0 OID 16406)
-- Dependencies: 220
-- Data for Name: clients; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.clients VALUES (3, 'employee', 'e@e.ru', '$2a$10$R5cf6r86v2ZKQ5HPSjNxlu7hVliVXzXDtgxCRr7X3/UM92SERO/Sm', NULL, NULL);
INSERT INTO public.clients VALUES (6, 'Nickname123', 'asdasdsad@asdasdasd.ru', '$2a$10$q9GNWmfHb6olXHVVuGZe0ON/RFlUi2BFY5cQEF2XelNZr0spw3/ay', NULL, NULL);
INSERT INTO public.clients VALUES (7, 'user', 'a@h', '$2a$10$/bNqMTpTZ/sjFLdcPwZzo.v1j6eMyNN7JDl3rjO2D6NPfrjrz7uHK', NULL, NULL);
INSERT INTO public.clients VALUES (2, 'Nickname', 'Куда-нибудь', '$2a$10$9oFLizM.X8ccL9wTIqciee.47qJ0NeGKdNeoSkPPEEbcusS1Th8IW', '+8888888888', 'ул. Куда-нибудь, 2');
INSERT INTO public.clients VALUES (8, 'Alexey', 'a@aaa.ru', '$2a$10$n77wD9xhZ.hAaTg.59kxuO3VvrqHkUNyCVnfjg3ZHnpjz/OjrQ9lC', '+7900000000', 'ул. Куда-нибудь, 3');
INSERT INTO public.clients VALUES (9, 'StarGonich', 'a@ya.ru', '$2a$10$UEquLYVsVJtWSYJ8ZUMKH.WA08rb/8DUmVzcR.HKh/jgEpoPDrfz6', '+79008888888', 'Туда');
INSERT INTO public.clients VALUES (11, 'Velichayshiy', 'vel@ya.ru', '$2a$10$RwjM/pnF9PGnQPxvRVzhx.pxMu5.43C3rWP8.8QvD2wnYByUl61Da', '+790000000000', 'Куда-нибудь');


--
-- TOC entry 4932 (class 0 OID 16417)
-- Dependencies: 222
-- Data for Name: employees; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.employees VALUES (4, 'employee3', 'e@e3', '$2a$10$b9DBwhAChOMEA31A8rh8h.FKL.JfJF9jgzIFSbeu6P6V1OrQV4Fxm');
INSERT INTO public.employees VALUES (2, 'employee1', 'e@e.ru', '$2a$10$LCVIYiKdhe7KMyfIybmvtugu4BixRPMpxUuFJpm3.NZH5TrlmHR4S');
INSERT INTO public.employees VALUES (3, 'employee2', 'employee2@e.ru', '$2a$10$mx6hvMijttUw5fjUrVZlye8VZ1gB1u4kFIQBfyHj2Nm98vMkzmWaC');
INSERT INTO public.employees VALUES (5, 'StarGonich', 'alexey8837@yandex.ru', '$2a$10$yhHa.l95qAwF4AFF.EBBKemHSusFhjF6kjkkrw.ku0VeC5HoFCUEi');


--
-- TOC entry 4934 (class 0 OID 16440)
-- Dependencies: 224
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.orders VALUES (21, 2, 7, 1, 'shipped', '2025-07-03 18:41:12.798644+03');
INSERT INTO public.orders VALUES (22, 2, 7, 3, 'shipped', '2025-07-03 19:08:29.09892+03');
INSERT INTO public.orders VALUES (23, 9, 4, 7, 'shipped', '2025-07-08 14:06:05.859808+03');
INSERT INTO public.orders VALUES (24, 9, 7, 2, 'shipped', '2025-07-08 14:06:05.859808+03');
INSERT INTO public.orders VALUES (25, 11, 7, 3, 'shipped', '2025-07-08 14:17:52.701159+03');
INSERT INTO public.orders VALUES (26, 11, 1, 1, 'shipped', '2025-07-08 14:17:52.701159+03');
INSERT INTO public.orders VALUES (27, 11, 6, 1, 'in_cart', '2025-07-08 14:20:26.159259+03');


--
-- TOC entry 4944 (class 0 OID 0)
-- Dependencies: 217
-- Name: books_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.books_id_seq', 7, true);


--
-- TOC entry 4945 (class 0 OID 0)
-- Dependencies: 219
-- Name: clients_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.clients_id_seq', 11, true);


--
-- TOC entry 4946 (class 0 OID 0)
-- Dependencies: 221
-- Name: employees_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.employees_id_seq', 5, true);


--
-- TOC entry 4947 (class 0 OID 0)
-- Dependencies: 223
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.orders_id_seq', 27, true);


--
-- TOC entry 4769 (class 2606 OID 16402)
-- Name: books books_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.books
    ADD CONSTRAINT books_pkey PRIMARY KEY (id);


--
-- TOC entry 4771 (class 2606 OID 16415)
-- Name: clients clients_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT clients_email_key UNIQUE (email);


--
-- TOC entry 4773 (class 2606 OID 16413)
-- Name: clients clients_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT clients_pkey PRIMARY KEY (id);


--
-- TOC entry 4775 (class 2606 OID 16424)
-- Name: employees employees_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT employees_email_key UNIQUE (email);


--
-- TOC entry 4777 (class 2606 OID 16422)
-- Name: employees employees_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT employees_pkey PRIMARY KEY (id);


--
-- TOC entry 4779 (class 2606 OID 16447)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- TOC entry 4780 (class 2606 OID 16453)
-- Name: orders orders_book_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_book_fkey FOREIGN KEY (book_id) REFERENCES public.books(id);


--
-- TOC entry 4781 (class 2606 OID 16448)
-- Name: orders orders_client_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_client_fkey FOREIGN KEY (client_id) REFERENCES public.clients(id);


-- Completed on 2025-07-14 12:15:38

--
-- PostgreSQL database dump complete
--

