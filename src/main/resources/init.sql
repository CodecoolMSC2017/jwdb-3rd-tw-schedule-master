DROP TABLE IF EXISTS task_hour;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS hour;
DROP TABLE IF EXISTS day;
DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS app_user;

CREATE TABLE app_user (
	id SERIAL PRIMARY KEY,
	email TEXT NOT NULL,
	user_name TEXT NOT NULL,
	password TEXT NOT NULL,
	role TEXT NOT NULL
);

CREATE TABLE schedule (
	id SERIAL PRIMARY KEY,
	app_user_id INTEGER NOT NULL,
	title TEXT NOT NULL,
	description TEXT NOT NULL,
	FOREIGN KEY (app_user_id) REFERENCES app_user("id") ON DELETE CASCADE
);

CREATE TABLE day (
	id SERIAL PRIMARY KEY NOT NULL,
	schedule_id INTEGER NOT NULL,
	title TEXT NOT NULL,
	FOREIGN KEY (schedule_id) REFERENCES schedule("id") ON DELETE CASCADE
);

CREATE TABLE hour (
	id SERIAL PRIMARY KEY NOT NULL,
	day_id INTEGER NOT NULL,
	value INTEGER NOT NULL,
	FOREIGN KEY (day_id) REFERENCES day("id") ON DELETE CASCADE
);

CREATE TABLE task (
	id SERIAL PRIMARY KEY NOT NULL,
	app_user_id INTEGER NOT NULL,
	title TEXT NOT NULL,
	description TEXT,
	FOREIGN KEY (app_user_id) REFERENCES app_user("id") ON DELETE CASCADE
);

CREATE TABLE task_hour (
	task_id INTEGER,
    schedule_id INTEGER,
    hour_ids TEXT,
    PRIMARY KEY (task_id, schedule_id),
	FOREIGN KEY (task_id) REFERENCES task("id")  ON DELETE CASCADE,
    FOREIGN KEY (schedule_id) REFERENCES schedule("id") ON DELETE CASCADE
);

INSERT INTO app_user (email, user_name, password, role)  VALUES
	('admin@admin.com', 'admin', 'd41d8cd98f00b204e9800998ecf8427e', 'admin'), --1
	('david@gmail.com', 'David', 'd41d8cd98f00b204e9800998ecf8427e', 'user'),	--2
	('kenez@gmail.com', 'Kenez', 'd41d8cd98f00b204e9800998ecf8427e', 'user'),	--3
	('norbi@gmail.com', 'Norbi', 'd41d8cd98f00b204e9800998ecf8427e', 'user');	--4

INSERT INTO schedule (app_user_id, title, description) VALUES
	(2, 'Work', 'my working day'), --1
	(2, 'PS4', 'God of War'), -- 2
	(3, 'fap', 'pubg'); --3

INSERT INTO day (schedule_id, title) VALUES
	(2, 'prologue'), --1
	(3, 'one round'); --2

INSERT INTO hour (day_id, value) VALUES
    (1,0), --1
    (1,1), --2
	(1, 17), --3
	(1, 18), --4
	(2, 20); --5

INSERT INTO task (app_user_id, title, description) VALUES
	(2, 'gaming', 'playing god of war'), --1
	(3, 'fap', 'playing gay games'); --2

INSERT INTO task_hour (task_id, schedule_id, hour_ids) VALUES
	(1, 1, '1,2'),
	(1, 2, '3');