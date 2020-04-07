delete from user_role;
delete from usr;

insert into usr(id, active, password, username) values
(1, true, '"$2a$08$d2POd7W6pj/3I4kiB/ijjuybhYXrgaWmNO1y9UBRTPXCNDLxomSAa"', 'admin'),
(2, true, '"$2a$08$d2POd7W6pj/3I4kiB/ijjuybhYXrgaWmNO1y9UBRTPXCNDLxomSAa"', 'test' );

insert into user_role (user_id, roles) values
(1, 'USER'), (1, 'ADMIN'), (2, 'USER');