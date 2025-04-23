CREATE TABLE "christmas-list"."user" (
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
COMMENT ON TABLE "christmas-list"."USER" IS 'User Objects';

-- Column comments

COMMENT ON COLUMN "christmas-list"."USER".id IS 'PK of user';
COMMENT ON COLUMN "christmas-list"."USER".username IS 'Username for login';
COMMENT ON COLUMN "christmas-list"."USER"."password" IS 'Hashed Password of user';
COMMENT ON COLUMN "christmas-list"."USER"."role" IS 'Security permissions. (A)dmin or (U)ser';
COMMENT ON COLUMN "christmas-list"."USER".dt_crtd IS 'Date entry was created';
COMMENT ON COLUMN "christmas-list"."USER".dt_last_login IS 'Date user last logged in to the application';
COMMENT ON COLUMN "christmas-list"."USER".dt_last_modified IS 'Date entry was last modified';
COMMENT ON COLUMN "christmas-list"."USER".user_id_last_modified IS 'User that last modified this entry';
