CREATE TABLE IF NOT EXISTS permission (
  id bigint NOT NULL PRIMARY KEY,
  description varchar(255) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS users (
   id bigint NOT NULL PRIMARY KEY,
   user_name varchar(255) NOT NULL,
   full_name varchar(255) DEFAULT NULL,
   password varchar(255)  NOT NULL,
   email varchar (255)  NOT NULL,
   account_non_expired boolean DEFAULT NULL,
   account_non_locked boolean DEFAULT NULL,
   credentials_non_expired boolean DEFAULT NULL,
   enabled boolean DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS user_permission (
  id_user bigint NOT NULL,
  id_permission bigint NOT NULL,
  PRIMARY KEY (id_user,id_permission),
  CONSTRAINT fk_user_permission FOREIGN KEY (id_user) REFERENCES users (id),
  CONSTRAINT fk_user_permission_permission FOREIGN KEY (id_permission) REFERENCES permission (id)
);