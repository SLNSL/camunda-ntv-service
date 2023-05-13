INSERT INTO "role" (name)
VALUES ('ROLE_BOSS');

create table "privilege"
(
    id        int primary key generated always as identity,
    privilege_name text not null
);

INSERT INTO "privilege" (privilege_name)
VALUES ('CAN_GET_ARTICLES'),    /*1*/
       ('CAN_POST_ARTICLES'),   /*2*/
       ('CAN_PUT_ARTICLES'),    /*3*/
       ('CAN_DELETE_ARTICLES'), /*4*/
       ('CAN_GET_THEMES'),      /*5*/
       ('CAN_POST_THEMES'),     /*6*/
       ('CAN_DELETE_THEMES'),   /*7*/
       ('CAN_GET_JOURNALISTS'), /*8*/
       ('CAN_POST_JOURNALISTS'),/*9*/
       ('CAN_DELETE_JOURNALISTS')/*10*/;

create table role_privilege
(
    role_id integer not null,
    privilege_id integer not null,
    foreign key (role_id) references "role" (id)
        match simple on update no action on delete no action,
    foreign key (privilege_id) references "privilege" (id)
        match simple on update no action on delete no action
);

INSERT INTO role_privilege (role_id, privilege_id)
VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7),
       (2, 1), (2, 5),
       (3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6), (3, 7), (3, 8), (3, 9), (3, 10);
       
UPDATE "role" SET name = 'ROLE_JOURNALIST' WHERE id = 1;