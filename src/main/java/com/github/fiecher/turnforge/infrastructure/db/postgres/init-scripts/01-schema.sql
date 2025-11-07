create type user_role as enum ('user', 'admin');
create type size_type as enum ('Tiny', 'Small', 'Medium', 'Large', 'Huge', 'Gargantuan');


create table if not exists users
(
    id            bigserial primary key,
    password_hash varchar(128) not null,
    role          user_role    not null default 'user',
    login         varchar(50)  not null unique,
    created_at    timestamptz  not null default current_timestamp,
    updated_at    timestamptz  not null default current_timestamp
);

create table if not exists characters
(
    id                   bigserial primary key,
    user_id              bigint      not null references users (id) on update cascade on delete cascade,
    name                 varchar(50) not null,
    level                smallint    not null default 1 check (level > 0),
    strength             smallint    not null default 0,
    dexterity            smallint    not null default 0,
    constitution         smallint    not null default 0,
    intelligence         smallint    not null default 0,
    wisdom               smallint    not null default 0,
    charisma             smallint    not null default 0,
    description          text,
    image                text,
    class                varchar(30) not null,
    subclass             varchar(30),
    background           varchar(50),
    race                 varchar(30),
    age                  int,
    size                 size_type,
    spellcasting_ability varchar(16),
    money                int         not null default 0,
    created_at           timestamptz not null default current_timestamp,
    updated_at           timestamptz not null default current_timestamp,
    constraint unique_user_character unique (user_id, name)
);

create table if not exists skills
(
    id          bigserial primary key,
    name        varchar(50) not null unique,
    description text,
    created_at  timestamptz not null default current_timestamp,
    updated_at  timestamptz not null default current_timestamp
);

create table if not exists characters_skills
(
    character_id  bigint not null references characters (id) on update cascade on delete cascade,
    skill_id      bigint not null references skills (id) on update cascade on delete cascade,
    is_proficient boolean default false,
    is_expertise  boolean default false,
    primary key (character_id, skill_id)
);

create table if not exists traits
(
    id            bigserial primary key,
    name          varchar(50) not null unique,
    description   text,
    image         text,
    prerequisites text,
    trait_type    varchar(30),
    created_at    timestamptz not null default current_timestamp,
    updated_at    timestamptz not null default current_timestamp
);

create table if not exists characters_traits
(
    character_id bigint not null references characters (id) on update cascade on delete cascade,
    trait_id     bigint not null references traits (id) on update cascade on delete cascade,
    primary key (character_id, trait_id)
);

create table if not exists abilities
(
    id           bigserial primary key,
    name         varchar(50) not null unique,
    description  text,
    image        text,
    damage       varchar(50),
    ability_type varchar(30),
    level        smallint    not null default 0 check (level >= 0),
    time         varchar(30),
    range        varchar(30),
    components   varchar(30),
    duration     varchar(30),
    created_at   timestamptz not null default current_timestamp,
    updated_at   timestamptz not null default current_timestamp
);

create table if not exists characters_abilities
(
    character_id bigint not null references characters (id) on update cascade on delete cascade,
    ability_id   bigint not null references abilities (id) on update cascade on delete cascade,
    primary key (character_id, ability_id)
);

create table if not exists weapons
(
    id          bigserial primary key,
    name        varchar(50) not null unique,
    description text,
    image       text,
    damage      varchar(50),
    type        varchar(30),
    properties  varchar(50),
    weight      numeric(5, 2),
    price       int,
    created_at  timestamptz not null default current_timestamp,
    updated_at  timestamptz not null default current_timestamp
);

create table if not exists characters_weapons
(
    character_id bigint not null references characters (id) on update cascade on delete cascade,
    weapon_id    bigint not null references weapons (id) on update cascade on delete cascade,
    primary key (character_id, weapon_id)
);

create table if not exists items
(
    id          bigserial primary key,
    name        varchar(50) not null unique,
    description text,
    image       text,
    weight      numeric(5, 2),
    price       int,
    created_at  timestamptz not null default current_timestamp,
    updated_at  timestamptz not null default current_timestamp
);

create table if not exists characters_items
(
    character_id bigint not null references characters (id) on update cascade on delete cascade,
    item_id      bigint not null references items (id) on update cascade on delete cascade,
    primary key (character_id, item_id)
);

create table if not exists armor
(
    id          bigserial primary key,
    name        varchar(50) not null unique,
    description text,
    image       text,
    ac          smallint,
    armor_type  varchar(30),
    weight      numeric(5, 2),
    price       int,
    created_at  timestamptz not null default current_timestamp,
    updated_at  timestamptz not null default current_timestamp
);

create table if not exists characters_armor
(
    character_id bigint not null references characters (id) on update cascade on delete cascade,
    armor_id     bigint not null references armor (id) on update cascade on delete cascade,
    primary key (character_id, armor_id)
);

create index on characters (user_id);
create index on characters_skills (character_id, skill_id);
create index on characters_traits (character_id, trait_id);
create index on characters_abilities (character_id, ability_id);
create index on characters_weapons (character_id, weapon_id);
create index on characters_items (character_id, item_id);
create index on characters_armor (character_id, armor_id);

create or replace function set_updated_at()
    returns trigger as
$$
begin
    new.updated_at = now();
    return new;
end;
$$ language plpgsql;

DO
$$
    DECLARE
        r record;
    BEGIN
        FOR r IN
            SELECT table_name
            FROM information_schema.columns
            WHERE column_name = 'updated_at'
              AND table_schema = 'public'
            LOOP
                EXECUTE format('
            CREATE TRIGGER %I_update_ts
            BEFORE UPDATE ON %I
            FOR EACH ROW
            EXECUTE FUNCTION set_updated_at();',
                               r.table_name, r.table_name
                        );
            END LOOP;
    END;
$$;
