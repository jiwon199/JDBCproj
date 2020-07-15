/*make artist table.*/
create table Artist(
name varchar(50) not null,
age int not null,
belongGroup varchar(50) not null,
primary key(name)
);
/*make belongGroup index*/
create index group_index on Artist(belongGroup);

/*make album table*/
create table Album(
albumTitle varchar(50) not null,
artist varchar(50) not null,
releaseYear int not null,
primary key(albumTitle),
foreign key(artist) references Artist(name) ON DELETE CASCADE ON UPDATE CASCADE
);
/*make Song table.*/
create table Song(
songTitle varchar(50) not null,
songLength float not null,
albumTitle varchar(50) not null,
primary key(songTitle),
foreign key(albumTitle) references Album(albumTitle) ON DELETE CASCADE ON UPDATE CASCADE
);
/*insert into table in order Artist-Album-Song */
insert into Artist(name,age,belongGroup) values ('Raekwon',50,'Wu-Tang'),
('Paul-McCartney',80,'Beatles'),
('Norah-Jones',41,'Little-Willies'),
('Kei',26,'Lovelyz');


insert into Album(albumTitle,artist,releaseYear) values ('Only-Built-4-cuban-Linx','Raekwon',1995),
('Red-Rose-Speedway','Paul-McCartney',1973),
('Band-On-The-Run','Paul-McCartney',1973),
('Come-Away-With-me','Norah-Jones',2002),
('Over-and-Over','Kei',2018);
 

insert into Song(songTitle,songLength,albumTitle) values ('Ason-Jones',2,'Only-Built-4-cuban-Linx'),
('About-me',1.3,'Only-Built-4-cuban-Linx'),
('Baggin-Crack',1.4,'Only-Built-4-cuban-Linx'),
('Canal-Street',1.5,'Only-Built-4-cuban-Linx'),
('Gihad',1.3,'Only-Built-4-cuban-Linx'),
('Big-Barn-Bed',2,'Red-Rose-Speedway'),
('My-Love',1.5,'Red-Rose-Speedway'),
('Little-lamb-dragonfliy',1.7,'Red-Rose-Speedway'),
('When-the-night',1.5,'Red-Rose-Speedway'),
('Jet',1.9,'Band-On-The-Run'),
('Bluebird',2,'Band-On-The-Run'),
('Mrs-Vandebilt',1.1,'Band-On-The-Run'),
('Let-Me-Roll-It',1.6,'Band-On-The-Run'),
('Mamunia',1.8,'Band-On-The-Run'),
('Seven-Years',1.4,'Come-Away-With-me'),
('Turn-Me-On',1.5,'Come-Away-With-me'),
('Lonestar',1.2,'Come-Away-With-me'),
('Cold-Cold-Heart',1.1,'Come-Away-With-me'),
('I-Go',1.4,'Over-and-Over'),
('Back-in-the-Day',2,'Over-and-Over'),
('Dreaming',1.6,'Over-and-Over');

/*make view mylist. (use Album,Song table)*/
create view mylist as select albumTitle, artist, count(songTitle) as songNum from Album natural join Song group by albumTitle;

 
