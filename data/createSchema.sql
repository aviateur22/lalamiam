BEGIN;
DROP TABLE IF EXISTS
sc_lalamiam."client_command",
sc_lalamiam."jwt_user",
sc_lalamiam."store_day_schedule",
sc_lalamiam."week_day",
sc_lalamiam."command_product",
sc_lalamiam."product",
sc_lalamiam."status",
sc_lalamiam."command",
sc_lalamiam."store",
sc_lalamiam."role_user",
sc_lalamiam."role",
sc_lalamiam."users";


create table IF NOT EXISTS sc_lalamiam.users(
    "id" BIGINT PRIMARY KEY,
    "email" TEXT NOT NULL,
    "phone" TEXT,
    "name" TEXT,
    "password" TEXT NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

create table IF NOT EXISTS sc_lalamiam.role(
    "id" INT PRIMARY KEY,
    "name" TEXT NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

create table IF NOT EXISTS sc_lalamiam.role_user(
    "id" BIGINT PRIMARY KEY,
    "user_id" BIGINT NOT NULL REFERENCES sc_lalamiam."users"("id") on delete cascade,
    "role_id" BIGINT NOT NULL REFERENCES sc_lalamiam."role"("id") on delete cascade,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

create table IF NOT EXISTS sc_lalamiam.store(
     "id" BIGINT PRIMARY KEY,
     "pro_id" BIGINT NOT NULL REFERENCES sc_lalamiam."users"("id") on delete cascade,
     "name" TEXT NOT NULL,
     "adress" TEXT NOT NULL,
     "city" TEXT NOT NULL,
     "cp" TEXT NOT NULL,
     "photo" TEXT,
     "presentation" TEXT,
     "phone" TEXT,
     "frequence_slot_time" INTEGER NOT NULL DEFAULT 5,
     "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
     "updated_at" TIMESTAMPTZ
);

create table IF NOT EXISTS sc_lalamiam.status(
    "id" INTEGER PRIMARY KEY,
    "status_name" TEXT NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

create table IF NOT EXISTS sc_lalamiam.command(
    "id" BIGINT PRIMARY KEY,
    "store_id" BIGINT NOT NULL REFERENCES sc_lalamiam."store"("id") on delete cascade,
    "command_code" TEXT NOT NULL,
    "slot_time" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "client_phone" TEXT NOT NULL,
    "preparation_time" INTEGER NOT NULL,
    "order_price" NUMERIC NOT NULL,
    "product_quantity" INTEGER NOT NULL,
    "is_ready" BOOLEAN NOT NULL DEFAULT FALSE,
    "prepared_by" BIGINT REFERENCES sc_lalamiam."users"("id") on delete cascade,
    "command_status" INTEGER NOT NULL DEFAULT 1 REFERENCES sc_lalamiam."status"("id") on delete cascade,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

create table IF NOT EXISTS sc_lalamiam.product(
    "id" BIGINT PRIMARY KEY,
    "store_id" INTEGER NOT NULL REFERENCES sc_lalamiam."store"("id") on delete cascade,
    "name" TEXT NOT NULL,
    "price" NUMERIC NOT NULL,
    "description" TEXT NOT NULL,
    "preparation_time" INTEGER NOT NULL,
    "photo" TEXT NOT NULL,
    "is_avail" BOOLEAN NOT NULL DEFAULT TRUE,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

create table IF NOT EXISTS sc_lalamiam.command_product(
    "id" BIGINT PRIMARY KEY,
    "command_id" BIGINT NOT NULL REFERENCES sc_lalamiam."command"("id") on delete cascade,
    "product_id" BIGINT NOT NULL REFERENCES sc_lalamiam."product"("id") on delete cascade,
    "product_quantity" INTEGER NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

create table IF NOT EXISTS sc_lalamiam.week_day(
    "id" BIGINT NOT NULL UNIQUE PRIMARY KEY,
    "day_text" TEXT NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

create table IF NOT EXISTS sc_lalamiam.store_day_schedule (
    "id" INTEGER PRIMARY KEY,
    "opening_time" TIME NOT NULL,
    "closing_time" TIME NOT NULL,
    "store_id" BIGINT NOT NULL REFERENCES sc_lalamiam."store"("id") on delete cascade,
    "week_day_id" INTEGER NOT NULL REFERENCES sc_lalamiam."week_day"("id") on delete cascade,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS sc_lalamiam.jwt_user (
"id" BIGINT NOT NULL UNIQUE PRIMARY KEY,
"user_id" BIGINT NOT NULL REFERENCES sc_lalamiam."users"("id") ON DELETE CASCADE,
"jwt_token" TEXT NOT NULL,
"jwt_id" TEXT NOT NULL,
"is_valid" BOOLEAN NOT NULL DEFAULT FALSE,
"expired_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
"created_at"TIMESTAMPTZ NOT NULL DEFAULT now(),
"updated_at" TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS sc_lalamiam.client_command (
"id" BIGINT NOT NULL UNIQUE PRIMARY KEY,
"client_id" BIGINT NOT NULL REFERENCES sc_lalamiam."users"("id") ON DELETE CASCADE,
"command_id" BIGINT NOT NULL REFERENCES sc_lalamiam."command"("id") on delete cascade,
"created_at"TIMESTAMPTZ NOT NULL DEFAULT now(),
"updated_at" TIMESTAMPTZ
);

ALTER table IF EXISTS sc_lalamiam.command_product OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.role OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.role_user OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.product OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.command OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.store OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.users OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.week_day OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.store_day_schedule OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.jwt_user OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.client_command OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.status OWNER TO lalamiam;
ALTER SCHEMA sc_lalamiam OWNER TO lalamiam;

GRANT ALL ON TABLE sc_lalamiam.command_product TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.role TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.role_user TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.product TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.command TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.status TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.store TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.users TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.week_day TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.store_day_schedule TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.jwt_user TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.client_command TO lalamiam;
GRANT ALL ON SCHEMA sc_lalamiam TO lalamiam;

CREATE SEQUENCE if not exists sc_lalamiam.users_pk_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
ALTER SEQUENCE if exists sc_lalamiam.users_pk_seq OWNER TO lalamiam;
ALTER SEQUENCE if exists sc_lalamiam.users_pk_seq owned by sc_lalamiam.users.id;

CREATE SEQUENCE if not exists sc_lalamiam.role_user_pk_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
ALTER SEQUENCE if exists sc_lalamiam.role_user_pk_seq OWNER TO lalamiam;
ALTER SEQUENCE if exists sc_lalamiam.role_user_pk_seq owned by sc_lalamiam.role_user.id;

CREATE SEQUENCE if not exists sc_lalamiam.store_pk_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
ALTER SEQUENCE if exists sc_lalamiam.store_pk_seq OWNER TO lalamiam;
ALTER SEQUENCE if exists sc_lalamiam.store_pk_seq OWNED by sc_lalamiam.store.id;

CREATE SEQUENCE if not exists sc_lalamiam.product_pk_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
ALTER SEQUENCE if exists sc_lalamiam.product_pk_seq OWNER TO lalamiam;
ALTER SEQUENCE if exists sc_lalamiam.product_pk_seq OWNED by sc_lalamiam.product.id;

CREATE SEQUENCE if not exists sc_lalamiam.command_pk_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
ALTER SEQUENCE if exists sc_lalamiam.command_pk_seq OWNER TO lalamiam;
ALTER SEQUENCE if exists sc_lalamiam.command_pk_seq OWNED by sc_lalamiam.command.id;

CREATE SEQUENCE if not exists sc_lalamiam.command_product_pk_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
ALTER SEQUENCE if exists sc_lalamiam.command_product_pk_seq OWNER TO lalamiam;
ALTER SEQUENCE if exists sc_lalamiam.command_product_pk_seq OWNED by sc_lalamiam.command_product.id;

CREATE SEQUENCE if not exists sc_lalamiam.store_week_day_pk_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
ALTER SEQUENCE if exists sc_lalamiam.store_week_day_pk_seq OWNER TO lalamiam;
ALTER SEQUENCE if exists sc_lalamiam.store_week_day_pk_seq owned by sc_lalamiam.store_day_schedule.id;

CREATE SEQUENCE if not exists sc_lalamiam.jwt_user_pk_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
ALTER SEQUENCE if exists sc_lalamiam.jwt_user_pk_seq OWNER TO lalamiam;
ALTER SEQUENCE if exists sc_lalamiam.jwt_user_pk_seq owned by sc_lalamiam.jwt_user.id;

CREATE SEQUENCE if not exists sc_lalamiam.client_command_pk_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
ALTER SEQUENCE if exists sc_lalamiam.client_command_pk_seq OWNER TO lalamiam;
ALTER SEQUENCE if exists sc_lalamiam.client_command_pk_seq owned by sc_lalamiam.client_command.id;

CREATE SEQUENCE if not exists sc_lalamiam.status_pk_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
ALTER SEQUENCE if exists sc_lalamiam.status_pk_seq OWNER TO lalamiam;
ALTER SEQUENCE if exists sc_lalamiam.status_pk_seq owned by sc_lalamiam.status.id;

ALTER TABLE sc_lalamiam.product ALTER COLUMN id SET DEFAULT NEXTVAL('sc_lalamiam.product_pk_seq');
ALTER TABLE sc_lalamiam.role_user ALTER COLUMN id SET DEFAULT NEXTVAL('sc_lalamiam.role_user_pk_seq');
ALTER TABLE sc_lalamiam.command ALTER COLUMN id SET DEFAULT NEXTVAL('sc_lalamiam.command_pk_seq');
ALTER TABLE sc_lalamiam.users ALTER COLUMN id SET DEFAULT NEXTVAL('sc_lalamiam.users_pk_seq');
ALTER TABLE sc_lalamiam.store ALTER COLUMN id SET DEFAULT NEXTVAL('sc_lalamiam.store_pk_seq');
ALTER TABLE sc_lalamiam.store_day_schedule ALTER COLUMN id SET DEFAULT NEXTVAL('sc_lalamiam.store_week_day_pk_seq');
ALTER TABLE sc_lalamiam.command_product ALTER COLUMN id SET DEFAULT NEXTVAL('sc_lalamiam.command_product_pk_seq');
ALTER TABLE sc_lalamiam.jwt_user ALTER COLUMN id SET DEFAULT NEXTVAL('sc_lalamiam.jwt_user_pk_seq');
ALTER TABLE sc_lalamiam.client_command ALTER COLUMN id SET DEFAULT NEXTVAL('sc_lalamiam.client_command_pk_seq');
ALTER TABLE sc_lalamiam.status ALTER COLUMN id SET DEFAULT NEXTVAL('sc_lalamiam.status_pk_seq');


INSERT INTO sc_lalamiam.week_day ("id", "day_text") values (1, 'monday'), (2, 'tuesday'),  (3, 'wenesday'), (4, 'thrurday'),(5, 'friday'), (6, 'staurday'), (7, 'sunday');
INSERT INTO sc_lalamiam.role ("id", "name") values
 (1, 'ROLE_CLIENT'),
 (2, 'ROLE_EMPLOYE'),
 (3, 'ROLE_PRO'),
 (4, 'ROLE_ADMIN');
INSERT INTO sc_lalamiam.status("status_name") values
('new_command'),('not_started'), ('in_progress'),('ready'), ('paid');

COMMIT;
