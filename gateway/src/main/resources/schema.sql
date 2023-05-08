CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar(255) NOT NULL,
description varchar(255) NOT NULL,
available boolean,
owner BIGINT NOT NULL REFERENCES users (id),
request_id BIGINT REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS booking (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
item_id BIGINT NOT NULL REFERENCES items (id),
booker_id BIGINT NOT NULL REFERENCES users (id),
status text
);

CREATE TABLE IF NOT EXISTS requests (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
description varchar(255),
requestor_id BIGINT NOT NULL REFERENCES users (id),
created DATE
);

CREATE TABLE IF NOT EXISTS comments (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
text varchar(255),
item_id BIGINT NOT NULL REFERENCES items (id),
author_id BIGINT NOT NULL REFERENCES users (id),
author_name VARCHAR(255),
created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);