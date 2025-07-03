create schema christmas_nonprod;
create schema christmas_prod;

grant usage on schema christmas_nonprod to nzullo;
grant usage on schema christmas_prod to nzullo;

grant usage on schema christmas_nonprod to pzullo;
grant usage on schema christmas_prod to pzullo;



grant all PRIVILEGES on all tables in schema christmas_nonprod to nzullo;
grant all PRIVILEGES on all tables in schema christmas_nonprod to pzullo;