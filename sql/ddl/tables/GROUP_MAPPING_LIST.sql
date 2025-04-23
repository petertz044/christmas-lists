CREATE TABLE "christmas-list".group_mapping_list (
	id serial NOT NULL,
	group_id integer NOT null REFERENCES "christmas-list"."group"(id),
	list_id integer NOT null REFERENCES "christmas-list"."list"(id),
	dt_crtd timestamp NOT NULL,
	dt_last_modified timestamp NOT NULL,
	user_id_last_modified text null,
	CONSTRAINT group_mapping_list_pk PRIMARY KEY (id)
);
COMMENT ON TABLE "christmas-list".group_mapping_list IS 'Many to Many of List to Group';

-- Column comments

COMMENT ON COLUMN "christmas-list".group_mapping_list.id IS 'PK of group_mapping_list';
COMMENT ON COLUMN "christmas-list".group_mapping_list.group_id IS 'FK to Group ID';
COMMENT ON COLUMN "christmas-list".group_mapping_list.list_id IS 'FK to List ID';
COMMENT ON COLUMN "christmas-list".group_mapping_list.dt_crtd IS 'Date entry was created';
COMMENT ON COLUMN "christmas-list".group_mapping_list.dt_last_modified IS 'Date entry was last modified';
COMMENT ON COLUMN "christmas-list".group_mapping_list.user_id_last_modified IS 'User that last modified this entry';