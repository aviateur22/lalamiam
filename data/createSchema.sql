BEGIN;
DROP TABLE IF EXISTS sc_lalamiam."command_product", sc_lalamiam."schedule", sc_lalamiam."product",  sc_lalamiam."command", sc_lalamiam."store", sc_lalamiam."pro";

create table IF NOT EXISTS sc_lalamiam.pro(
    "id" INTEGER PRIMARY KEY,
    "email" TEXT NOT NULL,
    "phone" TEXT NOT NULL,
    "password" TEXT NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

create table IF NOT EXISTS sc_lalamiam.store(
     "id" INTEGER PRIMARY KEY,
     "pro_id" INTEGER NOT NULL REFERENCES sc_lalamiam."pro"("id") on delete cascade,
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

create table IF NOT EXISTS sc_lalamiam.command(
    "id" INTEGER PRIMARY KEY,
    "store_id" INTEGER NOT NULL REFERENCES sc_lalamiam."store"("id") on delete cascade,
    "command_code" TEXT NOT NULL,
    "slot_time" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "client_phone" TEXT NOT NULL,
    "preparation_time" INTEGER NOT NULL,
    "order_price" NUMERIC NOT NULL,
    "product_quantity" INTEGER NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

create table IF NOT EXISTS sc_lalamiam.product(
    "id" INTEGER PRIMARY KEY,
    "store_id" INTEGER NOT NULL REFERENCES sc_lalamiam."store"("id") on delete cascade,
    "name" TEXT NOT NULL,
    "price" NUMERIC NOT NULL,
    "description" TEXT NOT NULL,
    "preparation_time" INTEGER NOT NULL,
    "photo" TEXT NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

create table IF NOT EXISTS sc_lalamiam.command_product(
    "id" INTEGER PRIMARY KEY,
    "command_id" INTEGER NOT NULL REFERENCES sc_lalamiam."command"("id") on delete cascade,
    "product_id" INTEGER NOT NULL REFERENCES sc_lalamiam."product"("id") on delete cascade,
    "product_quantity" INTEGER NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

create table IF NOT EXISTS sc_lalamiam.schedule(
    "id" INTEGER PRIMARY KEY,
    "store_id" INTEGER NOT NULL REFERENCES sc_lalamiam."store"("id") on delete cascade,
    "opening_time" TIME NOT NULL,
    "closing_time" TIME NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

ALTER table IF EXISTS sc_lalamiam.command_product OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.product OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.command OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.store OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.pro OWNER TO lalamiam;
ALTER table IF EXISTS sc_lalamiam.schedule OWNER TO lalamiam;
ALTER SCHEMA sc_lalamiam OWNER TO lalamiam;

GRANT ALL ON TABLE sc_lalamiam.command_product TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.product TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.command TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.store TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.pro TO lalamiam;
GRANT ALL ON TABLE sc_lalamiam.schedule TO lalamiam;
GRANT ALL ON SCHEMA sc_lalamiam TO lalamiam;

CREATE SEQUENCE if not exists sc_lalamiam.pro_pk_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
ALTER SEQUENCE if exists sc_lalamiam.pro_pk_seq OWNER TO lalamiam;
ALTER SEQUENCE if exists sc_lalamiam.pro_pk_seq owned by sc_lalamiam.pro.id;

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

CREATE SEQUENCE if not exists sc_lalamiam.schedule_pk_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
ALTER SEQUENCE if exists sc_lalamiam.schedule_pk_seq OWNER TO lalamiam;
ALTER SEQUENCE if exists sc_lalamiam.schedule_pk_seq OWNED by sc_lalamiam.schedule.id;

COMMIT;
