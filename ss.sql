drop database if exists SS;
create database SS;
use SS;

create table accounts (
name VARCHAR(30) NOT NULL PRIMARY KEY,
pwdhash varchar(90) NOT NULL,
logged_in bit(1) NOT NULL,
locked bit(1) NOT NULL,
nonce INT NOT NULL);

create table friendships (
	requester VARCHAR(30) NOT NULL,
	accepter VARCHAR(30) NOT NULL,
	FOREIGN KEY (requester)
			REFERENCES accounts(name) ON DELETE CASCADE,
	FOREIGN KEY (accepter)
			REFERENCES accounts(name) ON DELETE CASCADE,
	accepted bit(1) NOT NULL,
	PRIMARY KEY(requester, accepter)
);

create table resources (
	id INT NOT NULL AUTO_INCREMENT primary key,
	owner VARCHAR(30) NOT NULL,
	FOREIGN KEY (owner)
			REFERENCES accounts(name) ON DELETE CASCADE,
	name VARCHAR(40) NOT NULL,
	UNIQUE KEY(owner, name),
	permission VARCHAR(30) NOT NULL
);

create table accesscontrol (
	principal VARCHAR(30) NOT NULL,
    FOREIGN KEY (principal)
        REFERENCES accounts(name) ON DELETE CASCADE,
    resource int NOT NULL,
    FOREIGN KEY (resource)
        REFERENCES resources(id) ON DELETE CASCADE,
    operation VARCHAR(30) NOT NULL,
    UNIQUE KEY(principal,resource,operation)
);

create table details(
	name varchar(30) NOT NULL PRIMARY KEY,
    sex varchar(1),
    work varchar(30),
    birthdate date,
    location varchar(30),
    origin varchar(30),
    email varchar(30),
    phone varchar(30),
    internal_statement text,
    external_statement text,
    FOREIGN KEY (name)
        REFERENCES accounts(name) ON DELETE CASCADE
);

ALTER TABLE accounts 
ADD COLUMN id INT NOT NULL AUTO_INCREMENT first,
ADD UNIQUE INDEX `id_UNIQUE` (id ASC);
INSERT INTO accounts (name, pwdhash, logged_in, locked, nonce) VALUES ('root', 'ma3CMbBFMx5RSlFrS3aA9YjjgjITq+kBc4vDrWey9vyzxk77k9GAAliNPMwaSe+64c4gy0PfNrOGUfEfp1Z46A==', 0, 0,10);
INSERT INTO accounts (name, pwdhash, logged_in, locked, nonce) VALUES ('1', 'ma3CMbBFMx5RSlFrS3aA9YjjgjITq+kBc4vDrWey9vyzxk77k9GAAliNPMwaSe+64c4gy0PfNrOGUfEfp1Z46A==', 0, 0, 23);
INSERT INTO accounts (name, pwdhash, logged_in, locked, nonce) VALUES ('2', 'ma3CMbBFMx5RSlFrS3aA9YjjgjITq+kBc4vDrWey9vyzxk77k9GAAliNPMwaSe+64c4gy0PfNrOGUfEfp1Z46A==', 0, 0, 7);
INSERT INTO accounts (name, pwdhash, logged_in, locked, nonce) VALUES ('3', 'ma3CMbBFMx5RSlFrS3aA9YjjgjITq+kBc4vDrWey9vyzxk77k9GAAliNPMwaSe+64c4gy0PfNrOGUfEfp1Z46A==', 0, 0, 67);
INSERT INTO accounts (name, pwdhash, logged_in, locked, nonce) VALUES ('4', 'ma3CMbBFMx5RSlFrS3aA9YjjgjITq+kBc4vDrWey9vyzxk77k9GAAliNPMwaSe+64c4gy0PfNrOGUfEfp1Z46A==', 0, 0, 21);
INSERT INTO accounts (name, pwdhash, logged_in, locked, nonce) VALUES ('5', 'ma3CMbBFMx5RSlFrS3aA9YjjgjITq+kBc4vDrWey9vyzxk77k9GAAliNPMwaSe+64c4gy0PfNrOGUfEfp1Z46A==', 0, 0, 5);



INSERT INTO friendships VALUES ('1', '2', 0);
INSERT INTO friendships VALUES ('1', '3', 0);
INSERT INTO friendships VALUES ('2', '3', 0);
INSERT INTO friendships VALUES ('2', '4', 0);
INSERT INTO friendships VALUES ('3', '5', 0);

INSERT INTO resources(owner, name, permission) VALUES ('root', 'profileroot', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('root', 'contactsroot', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('root', 'friendsroot', 'private');
INSERT INTO resources(owner, name, permission) VALUES ('root', 'internalroot', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('root', 'user', 'private');

INSERT INTO accesscontrol(principal, resource, operation) select 'root', id, 'create' from resources where owner = 'root' and name = 'user';
INSERT INTO accesscontrol(principal, resource, operation) select 'root', id, 'delete' from resources where owner = 'root' and name = 'user';
INSERT INTO accesscontrol(principal, resource, operation) select 'root', id, 'lock' from resources where owner = 'root' and name = 'user';

INSERT INTO resources(owner, name, permission) VALUES ('1', 'profile1', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('1', 'contacts1', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('1', 'friends1', 'private');
INSERT INTO resources(owner, name, permission) VALUES ('1', 'internal1', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('2', 'profile2', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('2', 'contacts2', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('2', 'friends2', 'private');
INSERT INTO resources(owner, name, permission) VALUES ('2', 'internal2', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('3', 'profile3', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('3', 'contacts3', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('3', 'friends3', 'private');
INSERT INTO resources(owner, name, permission) VALUES ('3', 'internal3', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('4', 'profile4', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('4', 'contacts4', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('4', 'friends4', 'private');
INSERT INTO resources(owner, name, permission) VALUES ('4', 'internal4', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('5', 'profile5', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('5', 'contacts5', 'internal');
INSERT INTO resources(owner, name, permission) VALUES ('5', 'friends5', 'private');
INSERT INTO resources(owner, name, permission) VALUES ('5', 'internal5', 'internal');

INSERT INTO `details` VALUES ('1','M','Student','1991-08-10','Lisbon','Porto','one@gmail.com','961234567','internal','external')
,('2','F','Professor','1976-01-20','Lisbon','Lisbon','two@gmail.com','96123456767','internal','external')
,('3','F','Professor','1976-01-20','Lisbon','Lisbon','three@gmail.com','96123456767','internal','external')
,('4','F','Professor','1976-01-20','Lisbon','Lisbon','four@gmail.com','96123456767','internal','external')
,('5','F','Professor','1976-01-20','Lisbon','Lisbon','five@gmail.com','96123456767','internal','external')
,('root','F','Professor','1976-01-20','Lisbon','Lisbon','root@gmail.com','96123456767','internal','external');

INSERT INTO accesscontrol(principal, resource, operation)
select 'root', id, 'read' from resources 
where owner = 'root' and name in ('profileroot','contactsroot','friendsroot','internalroot');

INSERT INTO accesscontrol(principal, resource, operation)
select '1', id, 'read' from resources 
where owner = '1' and name in ('profile1','contacts1','friends1','internal1');
INSERT INTO accesscontrol(principal, resource, operation)
select '2', id, 'read' from resources 
where owner = '2' and name in ('profile2','contacts2','friends2','internal2');
INSERT INTO accesscontrol(principal, resource, operation)
select '3', id, 'read' from resources 
where owner = '3' and name in ('profile3','contacts3','friends3','internal3');
INSERT INTO accesscontrol(principal, resource, operation)
select '4', id, 'read' from resources 
where owner = '4' and name in ('profile4','contacts4','friends4','internal4');
INSERT INTO accesscontrol(principal, resource, operation)
select '5', id, 'read' from resources 
where owner = '5' and name in ('profile5','contacts5','friends5','internal5');

