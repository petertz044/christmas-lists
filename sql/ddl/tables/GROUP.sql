CREATE TABLE "group" (
	id serial NOT NULL,
	title text NOT NULL,
	description text NULL,
	dt_crtd timestamp NOT NULL,
	dt_last_modified timestamp NOT NULL,
	user_id_last_modified text null,
	CONSTRAINT group_pk PRIMARY KEY (id)
);
COMMENT ON TABLE "group" IS 'Groups that users and lists will belong to';

-- Column comments

COMMENT ON COLUMN "group".id IS 'PK of group';
COMMENT ON COLUMN "group".title IS 'Title of the group';
COMMENT ON COLUMN "group".description IS 'Description of the group';
COMMENT ON COLUMN "group".dt_crtd IS 'Date entry was created';
COMMENT ON COLUMN "group".dt_last_modified IS 'Date entry was last modified';
COMMENT ON COLUMN "group".user_id_last_modified IS 'User that last modified this entry';