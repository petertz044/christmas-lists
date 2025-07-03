CREATE TABLE list (
	id serial NOT NULL,
	title text NOT NULL,
	user_id_owner integer NOT null references "user"(id),
	description text NULL,
	is_active boolean NOT NULL,
	dt_crtd timestamp NOT NULL,
	dt_last_modified timestamp NOT NULL,
	user_id_last_modified text null,
	CONSTRAINT list_pk PRIMARY KEY (id)
);
COMMENT ON TABLE list IS 'List of items on the christmas list';

-- Column comments

COMMENT ON COLUMN list.id IS 'PK of List';
COMMENT ON COLUMN list.title IS 'Title of List';
COMMENT ON COLUMN list.user_id_owner IS 'User who owns this list';
COMMENT ON COLUMN list.description IS 'Description of the List';
COMMENT ON COLUMN list.is_active IS 'If this list should be considered by logic';
COMMENT ON COLUMN list.dt_crtd IS 'Date user was created';
COMMENT ON COLUMN list.dt_last_modified IS 'Date user was last modified';
COMMENT ON COLUMN list.user_id_last_modified IS 'User that last modified this record';