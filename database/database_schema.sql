-- Table: public.users

-- DROP TABLE public.users;

CREATE TABLE IF NOT EXISTS users
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying(128) COLLATE pg_catalog."default" NOT NULL,
    username character varying(128) COLLATE pg_catalog."default" NOT NULL,
    age integer,
    gender character varying(1) COLLATE pg_catalog."default",
    location character varying(128) COLLATE pg_catalog."default",
    password character NOT NULL varying(128) COLLATE pg_catalog."default",
    role character varying(32) COLLATE pg_catalog."default" DEFAULT 'ROLE_USER'::character varying,
    register_date timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_username_key UNIQUE (username)
);

-- Table: public.charities

-- DROP TABLE public.charities;

CREATE TABLE IF NOT EXISTS charities
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    creator_id integer NOT NULL,
    title character varying(128) COLLATE pg_catalog."default" NOT NULL,
    description character varying(4096) COLLATE pg_catalog."default" NOT NULL,
    budget_required numeric NOT NULL,
    amount_collected numeric NOT NULL,
    volunteers_required integer NOT NULL,
    volunteers_signed_up integer,
    image bytea,
    CONSTRAINT charities_pkey PRIMARY KEY (id),
    CONSTRAINT unique_title UNIQUE (title),
    CONSTRAINT charities_creator_id_fkey FOREIGN KEY (creator_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE SET NULL
        ON DELETE CASCADE
);


-- Table: public.useractions

-- DROP TABLE public.useractions;

CREATE TABLE IF NOT EXISTS useractions
(
    user_id integer,
    description character varying(4096) COLLATE pg_catalog."default",
	action_date timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT "FK_ACTION_USER" FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE SET NULL
        ON DELETE CASCADE
);


-- Table: public.participants

-- DROP TABLE public.participants;

CREATE TABLE IF NOT EXISTS participants
(
    user_id integer,
    charity_id integer,
    participant_date timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT "FK_PARTICIPANT_CHARITY" FOREIGN KEY (charity_id)
        REFERENCES public.charities (id) MATCH SIMPLE
        ON UPDATE SET NULL
        ON DELETE CASCADE,
    CONSTRAINT participants_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE SET NULL
        ON DELETE CASCADE
);

-- Table: public.donators

-- DROP TABLE public.donators;

CREATE TABLE IF NOT EXISTS donators
(
    user_id integer,
    charity_id integer,
    donated_money DOUBLE PRECISION,
    donate_date timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT "FK_CHARITY_DONATORS" FOREIGN KEY (charity_id)
        REFERENCES public.charities (id) MATCH SIMPLE
        ON UPDATE SET NULL
        ON DELETE CASCADE,
    CONSTRAINT donators_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE SET NULL
        ON DELETE CASCADE
);


