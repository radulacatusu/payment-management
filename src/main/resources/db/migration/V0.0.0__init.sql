CREATE TABLE account (
  id SERIAL PRIMARY KEY,
  name character varying(255) NOT NULL,
  currency_id character(3) NOT NULL,
  balance numeric (15, 2), 
  balance_status character(2),
  balance_timestamp timestamp without time zone,
  date_opened timestamp without time zone,
  account_type character(6)
);

CREATE TABLE transfer (
  id SERIAL PRIMARY KEY,
  ammount numeric (15, 2) NOT NULL,
  currency_id character(3) NOT NULL,
  debit_account_id bigint NOT NULL,
  credit_account_id bigint NOT NULL,
  execution_date timestamp without time zone
);

CREATE INDEX account_name_hidx ON account USING btree (name);
CREATE INDEX account_type_hidx ON account USING btree (account_type);

INSERT INTO account (name,currency_id,balance,balance_status,balance_timestamp,date_opened,account_type) VALUES ('Ledger','EUR','1000000','DE', current_timestamp, current_timestamp,'LEDGER');