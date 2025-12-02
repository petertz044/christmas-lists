create schema christmas_nonprod;
create schema christmas_prod;

grant usage on schema christmas_nonprod to nzullo;
grant usage on schema christmas_prod to nzullo;

grant usage on schema christmas_nonprod to pzullo;
grant usage on schema christmas_prod to pzullo;


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


CREATE TABLE "group" (
	id serial NOT NULL,
	title text NOT NULL,
	description text NULL,
	is_active boolean NOT NULL,
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
COMMENT ON COLUMN "group".is_active IS 'If this group should be considered by logic';
COMMENT ON COLUMN "group".dt_crtd IS 'Date entry was created';
COMMENT ON COLUMN "group".dt_last_modified IS 'Date entry was last modified';
COMMENT ON COLUMN "group".user_id_last_modified IS 'User that last modified this entry';


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




CREATE TABLE group_mapping_list (
	id serial NOT NULL,
	group_id integer NOT null REFERENCES "group"(id),
	list_id integer NOT null REFERENCES "list"(id),
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
COMMENT ON COLUMN group_mapping_list.dt_crtd IS 'Date entry was created';
COMMENT ON COLUMN group_mapping_list.dt_last_modified IS 'Date entry was last modified';
COMMENT ON COLUMN group_mapping_list.user_id_last_modified IS 'User that last modified this entry';


CREATE TABLE group_mapping_user (
	id serial NOT NULL,
	group_id integer NOT null REFERENCES "group"(id),
	user_id integer NOT null REFERENCES "user"(id),
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


CREATE TABLE list_entry (
	id serial NOT NULL,
	url text NULL,
	title text NOT NULL,
	description text NULL,
	priority char NULL,
	list_id integer NOT null references "list"(id),
	user_id_owner integer NOT null references "user"(id),
	is_purchased boolean NOT NULL,
	user_id_purchased integer NULL,
	is_active boolean NOT NULL,
	dt_crtd timestamp NOT NULL,
	dt_last_modified timestamp NOT NULL,
	user_id_last_modified text null,
	CONSTRAINT list_entry_pk PRIMARY KEY (id)
);
COMMENT ON TABLE list_entry IS 'Individual Item that is a part of a list';

-- Column comments

COMMENT ON COLUMN list_entry.id IS 'PK of list_entry';
COMMENT ON COLUMN list_entry.url IS 'URL of the item';
COMMENT ON COLUMN list_entry.title IS 'Name of the item';
COMMENT ON COLUMN list_entry.description IS 'Detailed description of the item';
COMMENT ON COLUMN list_entry.priority IS 'Priority that the item should be considered with';
COMMENT ON COLUMN list_entry.list_id IS 'List that this item belongs to';
COMMENT ON COLUMN list_entry.user_id_owner IS 'User that owns this item';
COMMENT ON COLUMN list_entry.is_purchased IS 'If this item has been purchased yet';
COMMENT ON COLUMN list_entry.user_id_purchased IS 'User that purchases the item';
COMMENT ON COLUMN list_entry.is_active IS 'If this item should be considered by logic';
COMMENT ON COLUMN list_entry.dt_crtd IS 'Date entry was created';
COMMENT ON COLUMN list_entry.dt_last_modified IS 'Date entry was last modified';
COMMENT ON COLUMN list_entry.user_id_last_modified IS 'User that last modified this entry';


grant all PRIVILEGES on all tables in schema christmas_nonprod to nzullo;
grant all PRIVILEGES on all tables in schema christmas_nonprod to pzullo;

grant all PRIVILEGES on all sequences in schema christmas_nonprod to nzullo;
grant all PRIVILEGES on all sequences in schema christmas_nonprod to pzullo;