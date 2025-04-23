CREATE TABLE "christmas-list".list_entry (
	id serial NOT NULL,
	url text NULL,
	title text NOT NULL,
	description text NULL,
	priority char NULL,
	list_id integer NOT null references "christmas-list"."list"(id),
	user_id_owner integer NOT null references "christmas-list"."user"(id),
	is_purchased boolean NOT NULL,
	user_id_purchased integer NULL,
	is_active boolean NOT NULL,
	dt_crtd timestamp NOT NULL,
	dt_last_modified timestamp NOT NULL,
	user_id_last_modified text null,
	CONSTRAINT list_entry_pk PRIMARY KEY (id)
);
COMMENT ON TABLE "christmas-list".list_entry IS 'Individual Item that is a part of a list';

-- Column comments

COMMENT ON COLUMN "christmas-list".list_entry.id IS 'PK of list_entry';
COMMENT ON COLUMN "christmas-list".list_entry.url IS 'URL of the item';
COMMENT ON COLUMN "christmas-list".list_entry.title IS 'Name of the item';
COMMENT ON COLUMN "christmas-list".list_entry.description IS 'Detailed description of the item';
COMMENT ON COLUMN "christmas-list".list_entry.priority IS 'Priority that the item should be considered with';
COMMENT ON COLUMN "christmas-list".list_entry.list_id IS 'List that this item belongs to';
COMMENT ON COLUMN "christmas-list".list_entry.user_id_owner IS 'User that owns this item';
COMMENT ON COLUMN "christmas-list".list_entry.is_purchased IS 'If this item has been purchased yet';
COMMENT ON COLUMN "christmas-list".list_entry.user_id_purchased IS 'User that purchases the item';
COMMENT ON COLUMN "christmas-list".list_entry.is_active IS 'If this item should be considered by logic';
COMMENT ON COLUMN "christmas-list".list_entry.dt_crtd IS 'Date entry was created';
COMMENT ON COLUMN "christmas-list".list_entry.dt_last_modified IS 'Date entry was last modified';
COMMENT ON COLUMN "christmas-list".list_entry.user_id_last_modified IS 'User that last modified this entry';