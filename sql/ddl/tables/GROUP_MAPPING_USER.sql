CREATE TABLE group_mapping_user (
	id serial NOT NULL,
	group_id integer NOT null REFERENCES "group"(id),
	user_id integer NOT null REFERENCES "user"(id),
	is_active boolean NOT NULL,
	dt_crtd timestamp NOT NULL,
	dt_last_modified timestamp NOT NULL,
	user_id_last_modified text null,
	CONSTRAINT group_mapping_user_pk PRIMARY KEY (id)
);
COMMENT ON TABLE group_mapping_user IS 'Many to Many of User to Group';

-- Column comments

COMMENT ON COLUMN group_mapping_user.id IS 'PK of group_mapping_user';
COMMENT ON COLUMN group_mapping_user.group_id IS 'FK to Group ID';
COMMENT ON COLUMN group_mapping_user.user_id IS 'FK to User ID';
COMMENT ON COLUMN group_mapping_user.dt_crtd IS 'Date entry was created';
COMMENT ON COLUMN group_mapping_user.dt_last_modified IS 'Date entry was last modified';
COMMENT ON COLUMN group_mapping_user.user_id_last_modified IS 'User that last modified this entry';