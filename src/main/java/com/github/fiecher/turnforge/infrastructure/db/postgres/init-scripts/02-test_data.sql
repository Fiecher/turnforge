BEGIN;

INSERT INTO skills (name, description)
SELECT 'Skill_' || i, 'desc ' || i
FROM generate_series(1, 20) AS i;

INSERT INTO traits (name, description, trait_type)
SELECT 'Trait_' || i, 'desc ' || i, 'Test'
FROM generate_series(1, 50) AS i;

INSERT INTO abilities (name, description, damage, ability_type, level)
SELECT 'Ability_' || i, 'desc ' || i, '1d6', 'Feature', 1
FROM generate_series(1, 50) AS i;

INSERT INTO weapons (name, description, damage, type, properties, weight, price)
SELECT 'Weapon_' || i, 'desc ' || i, '1d6', 'Simple Melee', 'Light', 3::numeric, 100
FROM generate_series(1, 50) AS i;

INSERT INTO items (name, description, weight, price)
SELECT 'Item_' || i, 'desc ' || i, 1::numeric, 50
FROM generate_series(1, 50) AS i;

INSERT INTO armor (name, description, ac, armor_type, weight, price)
SELECT 'Armor_' || i, 'desc ' || i, 12, 'Light Armor', 5::numeric, 150
FROM generate_series(1, 50) AS i;

INSERT INTO users (login, password_hash, role)
SELECT 'user_' || i, md5(random()::text), 'user'::user_role
FROM generate_series(1, 50) AS i;

INSERT INTO characters (user_id, name, level, strength, dexterity, constitution,
                        intelligence, wisdom, charisma, class, race, background, size, money)
SELECT id,
       'Character_' || id,
       random() * 20,
       random() * 20,
       random() * 20,
       random() * 20,
       random() * 20,
       random() * 20,
       random() * 20,
       'Fighter',
       'Human',
       'Soldier',
       (SELECT unnest(enum_range(NULL::size_type)) ORDER BY random() LIMIT 1),
       100
FROM users
    LIMIT 50;

INSERT INTO characters_skills (character_id, skill_id, is_proficient, is_expertise)
SELECT c.id, s.id, false, false
FROM characters c
         JOIN skills s ON s.id <= 3;

INSERT INTO characters_traits (character_id, trait_id)
SELECT c.id, t.id
FROM characters c
         JOIN traits t ON t.id <= 3;

INSERT INTO characters_abilities (character_id, ability_id)
SELECT c.id, a.id
FROM characters c
         JOIN abilities a ON a.id <= 3;

INSERT INTO characters_weapons (character_id, weapon_id)
SELECT c.id, w.id
FROM characters c
         JOIN weapons w ON w.id <= 3;

INSERT INTO characters_items (character_id, item_id)
SELECT c.id, i.id
FROM characters c
         JOIN items i ON i.id <= 3;

INSERT INTO characters_armor (character_id, armor_id)
SELECT c.id, ar.id
FROM characters c
         JOIN armor ar ON ar.id <= 3;

COMMIT;

SELECT 'Total records generated: ' || (
    (SELECT count(*) FROM skills) +
    (SELECT count(*) FROM traits) +
    (SELECT count(*) FROM abilities) +
    (SELECT count(*) FROM weapons) +
    (SELECT count(*) FROM items) +
    (SELECT count(*) FROM armor) +
    (SELECT count(*) FROM users) +
    (SELECT count(*) FROM characters) +
    (SELECT count(*) FROM characters_skills) +
    (SELECT count(*) FROM characters_traits) +
    (SELECT count(*) FROM characters_abilities) +
    (SELECT count(*) FROM characters_weapons) +
    (SELECT count(*) FROM characters_items) +
    (SELECT count(*) FROM characters_armor)
    );
