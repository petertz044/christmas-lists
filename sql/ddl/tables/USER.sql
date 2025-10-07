CREATE TABLE "user" (
	id serial NOT NULL,
	username text NOT NULL,
	"password" text NOT NULL,
	"role" char NOT NULL,
	dt_crtd timestamp NOT NULL,
	dt_last_login timestamp NULL,
	dt_last_modified timestamp NOT NULL,
	user_id_last_modified text NULL,
	CONSTRAINT user_pk PRIMARY KEY (id)
);
COMMENT ON TABLE "user" IS 'User Objects';

-- Column comments

COMMENT ON COLUMN "user".id IS 'PK of user';
COMMENT ON COLUMN "user".username IS 'Username for login';
COMMENT ON COLUMN "user"."password" IS 'Hashed Password of user';
COMMENT ON COLUMN "user"."role" IS 'Security permissions. (A)dmin or (U)ser';
COMMENT ON COLUMN "user".dt_crtd IS 'Date entry was created';
COMMENT ON COLUMN "user".dt_last_login IS 'Date user last logged in to the application';
COMMENT ON COLUMN "user".dt_last_modified IS 'Date entry was last modified';
COMMENT ON COLUMN "user".user_id_last_modified IS 'User that last modified this entry';
