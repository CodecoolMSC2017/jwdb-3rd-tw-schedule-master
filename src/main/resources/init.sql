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
	FOREIGN KEY (app_user_id) REFERENCES app_user("id")
);

CREATE TABLE day (
	id SERIAL PRIMARY KEY NOT NULL,
	schedule_id INTEGER NOT NULL,
	title TEXT NOT NULL,
	FOREIGN KEY (schedule_id) REFERENCES schedule("id")
);

CREATE TABLE hour (
	id SERIAL PRIMARY KEY NOT NULL,
	day_id INTEGER NOT NULL,
	value INTEGER NOT NULL,
	FOREIGN KEY (day_id) REFERENCES day("id")
);

CREATE TABLE task (
	id SERIAL PRIMARY KEY NOT NULL,
	app_user_id INTEGER NOT NULL,
	title TEXT NOT NULL,
	description TEXT,
	FOREIGN KEY (app_user_id) REFERENCES app_user("id")
);

CREATE TABLE task_hour (
	task_id INTEGER,
    schedule_id INTEGER,
    hour_ids TEXT,
    PRIMARY KEY (task_id, schedule_id),
	FOREIGN KEY (task_id) REFERENCES task("id"),
    FOREIGN KEY (schedule_id) REFERENCES schedule("id")
);

INSERT INTO app_user (email, user_name, password, role)  VALUES
	('bence@gmail.com', 'Bence', 'password1', 'admin'), --1
	('david@gmail.com', 'David', 'password2', 'user'),	--2
	('kenez@gmail.com', 'Kenez', 'password3', 'user'),	--3
	('norbi@gmail.com', 'Norbi', 'password4', 'user');	--4
	
INSERT INTO schedule (app_user_id, title, description) VALUES
	(2, 'Work', 'my working day'),
	(2, 'PS4', 'God of War'),
	(3, 'fap', 'pubg');
	
INSERT INTO day (schedule_id, title) VALUES
	(2, 'prologue'),
	(3, 'one round');
	
INSERT INTO hour (day_id, value) VALUES
    (1,0),
    (1,1),
	(1, 17),
	(1, 18),
	(2, 20);
	
INSERT INTO task (app_user_id, title, description) VALUES
	(2, 'gaming', 'playing god of war'),
	(3, 'fap', 'playing gay games');
	
INSERT INTO task_hour (task_id, schedule_id, hour_ids) VALUES
	(1, 1, '1,2'),
	(1, 2, '3');