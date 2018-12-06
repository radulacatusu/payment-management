CREATE TABLE account (
  id SERIAL PRIMARY KEY,
  name character varying(255) NOT NULL UNIQUE,
  currency_id character(3) NOT NULL,
  balance numeric (15, 2) NOT NULL,
  balance_status character(2) NOT NULL,
  balance_timestamp timestamp NOT NULL,
  date_opened timestamp NOT NULL,
  account_type character(6) NOT NULL
);

alter table account alter column balance set default 0.00;
alter table account alter column balance_status set default 'DR';
alter table account alter column balance_timestamp set default current_timestamp;
alter table account alter column date_opened set default current_timestamp;
alter table account alter column account_type set default 'CLIENT';

CREATE TABLE transfer (
  id SERIAL PRIMARY KEY,
  amount numeric (15, 2) NOT NULL,
  currency_id character(3) NOT NULL,
  debit_account_id bigint NOT NULL,
  credit_account_id bigint NOT NULL,
  execution_date timestamp NOT NULL
);

CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  name character varying(255) NOT NULL,
  password character varying(255) NOT NULL
);

CREATE INDEX account_name_hidx ON account(name);
CREATE INDEX account_type_hidx ON account(account_type);

INSERT INTO account (name,currency_id,balance,balance_status,balance_timestamp,date_opened,account_type) VALUES ('Ledger','EUR','1000000','DR', current_timestamp, current_timestamp,'LEDGER');