DROP TABLE IF EXISTS trainingsunitexercise; 
DROP TABLE IF EXISTS trainingsunit; 
DROP TABLE IF EXISTS exercise; 
CREATE TABLE IF NOT EXISTS exercise (id INTEGER PRIMARY KEY AUTOINCREMENT,age STRING,autorMail STRING,autorName STRING,description STRING,duration INTEGER,graphic STRING,groupsize INTEGER,idLocal INTEGER,keywords STRING,lastChange DATE,material STRING,name STRING,physis INTEGER,rating DOUBLE,sport STRING,tactic INTEGER,technic INTEGER,videolink STRING);
CREATE TABLE IF NOT EXISTS trainingsunit (id INTEGER PRIMARY KEY AUTOINCREMENT,age STRING,autorMail STRING,autorName STRING,description STRING,duration INTEGER,exercise STRING,idLocal INTEGER,keywords STRINGS,lastChange DATE,name STRING,rating DOUBLE);
CREATE TABLE IF NOT EXISTS trainingsunitexercise (exercise INTEGER,trainingsunit INTEGER,duration INTEGER,PRIMARY KEY(exercise,trainingsunit));