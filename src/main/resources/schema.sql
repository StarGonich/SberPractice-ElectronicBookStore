CREATE TYPE public.order_status AS ENUM (
    'in_cart',
    'pending',
    'processing',
    'shipped',
    'delivered',
    'cancelled'
);

CREATE SEQUENCE public.books_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.books (
    id integer NOT NULL DEFAULT nextval('public.books_id_seq'),
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

CREATE SEQUENCE public.clients_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.clients (
    id integer NOT NULL DEFAULT nextval('public.clients_id_seq'),
    nickname character varying(50) NOT NULL,
    email character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    phone character varying(20),
    address text
);

CREATE SEQUENCE public.employees_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.employees (
    id integer NOT NULL DEFAULT nextval('public.employees_id_seq'),
    nickname character varying(50) NOT NULL,
    email character varying(50) NOT NULL,
    password character varying(255) NOT NULL
);

CREATE SEQUENCE public.orders_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.orders (
    id integer NOT NULL DEFAULT nextval('public.orders_id_seq'),
    client_id integer NOT NULL,
    book_id integer NOT NULL,
    count integer DEFAULT 1 NOT NULL,
    status character varying(25) DEFAULT 'in_cart'::public.order_status NOT NULL,
    date timestamp with time zone
);

ALTER SEQUENCE public.books_id_seq RESTART WITH 8;

ALTER SEQUENCE public.clients_id_seq RESTART WITH 12;

ALTER SEQUENCE public.employees_id_seq RESTART WITH 6;

ALTER SEQUENCE public.orders_id_seq RESTART WITH 28;

ALTER TABLE public.books
    ADD CONSTRAINT books_pkey PRIMARY KEY (id);

ALTER TABLE public.clients
    ADD CONSTRAINT clients_email_key UNIQUE (email);

ALTER TABLE public.clients
    ADD CONSTRAINT clients_pkey PRIMARY KEY (id);

ALTER TABLE public.employees
    ADD CONSTRAINT employees_email_key UNIQUE (email);

ALTER TABLE public.employees
    ADD CONSTRAINT employees_pkey PRIMARY KEY (id);

ALTER TABLE public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);

ALTER TABLE public.orders
    ADD CONSTRAINT orders_book_fkey FOREIGN KEY (book_id) REFERENCES public.books(id);

ALTER TABLE public.orders
    ADD CONSTRAINT orders_client_fkey FOREIGN KEY (client_id) REFERENCES public.clients(id);
