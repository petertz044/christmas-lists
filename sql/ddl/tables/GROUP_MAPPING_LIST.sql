CREATE TABLE group_mapping_list (
	id serial NOT NULL,
	group_id integer NOT null REFERENCES "group"(id),
	list_id integer NOT null REFERENCES "list"(id),
	is_active boolean NOT NULL,
	dt_crtd timestamp NOT NULL,
	dt_last_modified timestamp NOT NULL,
	user_id_last_modified text null,
	CONSTRAINT group_mapping_list_pk PRIMARY KEY (id)
);
COMMENT ON TABLE group_mapping_list IS 'Many to Many of List to Group';

-- Column comments

COMMENT ON COLUMN group_mapping_list.id IS 'PK of group_mapping_list';
COMMENT ON COLUMN group_mapping_list.group_id IS 'FK to Group ID';
COMMENT ON COLUMN group_mapping_list.list_id IS 'FK to List ID';
COMMENT ON COLUMN group_mapping_list.is_active IS 'If this mapping should be considered by logic';
COMMENT ON COLUMN group_mapping_list.dt_crtd IS 'Date entry was created';
COMMENT ON COLUMN group_mapping_list.dt_last_modified IS 'Date entry was last modified';
COMMENT ON COLUMN group_mapping_list.user_id_last_modified IS 'User that last modified this entry';