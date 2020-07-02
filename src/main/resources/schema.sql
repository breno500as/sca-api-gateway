CREATE TABLE IF NOT EXISTS users (
   user_id bigint NOT NULL PRIMARY KEY,
   user_name varchar(255) NOT NULL,
   full_name varchar(255) DEFAULT NULL,
   password varchar(255)  NOT NULL,
   email varchar (255)  NOT NULL,
   account_non_expired boolean DEFAULT true,
   account_non_locked boolean DEFAULT true,
   credentials_non_expired boolean DEFAULT true,
   enabled boolean DEFAULT true
);
 
CREATE TABLE  IF NOT EXISTS roles (
  role_id bigint NOT NULL  PRIMARY KEY,
  name varchar(45) NOT NULL
);
 
CREATE TABLE  IF NOT EXISTS users_roles (
  user_id bigint NOT NULL,
  role_id bigint NOT NULL,
  PRIMARY KEY (user_id,role_id),
  CONSTRAINT role_fk FOREIGN KEY (user_id) REFERENCES users (user_id),
  CONSTRAINT user_fk FOREIGN KEY (role_id) REFERENCES roles (role_id)
);