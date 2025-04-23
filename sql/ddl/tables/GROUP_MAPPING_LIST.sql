CREATE TABLE "christmas-list".GROUP_MAPPING_LIST (
	id serial NOT NULL,
	group_id integer NOT null REFERENCES "christmas-list"."GROUP"(id),
	list_id integer NOT null REFERENCES "christmas-list"."list"(id),
	dt_crtd timestamp NOT NULL,
	dt_last_modified timestamp NOT NULL,
	user_id_last_modified text null,
	CONSTRAINT group_mapping_list_pk PRIMARY KEY (id)
);
COMMENT ON TABLE "christmas-list".group_mapping_list IS 'Many to Many of List to Group';

-- Column comments

COMMENT ON COLUMN "christmas-list".GROUP_MAPPING_LIST.id IS 'PK of GROUP_MAPPING_LIST';
COMMENT ON COLUMN "christmas-list".GROUP_MAPPING_LIST.group_id IS 'FK to Group ID';
COMMENT ON COLUMN "christmas-list".GROUP_MAPPING_LIST.list_id IS 'FK to List ID';
COMMENT ON COLUMN "christmas-list".GROUP_MAPPING_LIST.dt_crtd IS 'Date entry was created';
COMMENT ON COLUMN "christmas-list".GROUP_MAPPING_LIST.dt_last_modified IS 'Date entry was last modified';
COMMENT ON COLUMN "christmas-list".GROUP_MAPPING_LIST.user_id_last_modified IS 'User that last modified this entry';