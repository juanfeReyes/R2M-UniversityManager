CREATE USER keycloak_user WITH SUPERUSER PASSWORD 'password';
CREATE DATABASE keycloak  OWNER keycloak_user;


CREATE USER uni_manager_user WITH SUPERUSER PASSWORD 'password';
CREATE DATABASE uni_manager OWNER uni_manager_user;
GRANT ALL PRIVILEGES ON DATABASE uni_manager TO uni_manager_user;
CREATE SCHEMA IF NOT EXISTS  uni_manager AUTHORIZATION uni_manager_user;
