CREATE TABLE Members (
    mem_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    goal_weight INTEGER NOT NULL, 
    goal_fitness INTEGER NOT NULL, 
    weight INTEGER NOT NULL, 
    fitness_lvl INTEGER NOT NULL,
    memb_fee INTEGER NOT NULL
);

CREATE TABLE Exercise_routine (
	num SERIAL NOT NULL,
    mem_id INTEGER NOT NULL,
    squats INTEGER NOT NULL,
    lunges INTEGER NOT NULL,
    pushups INTEGER NOT NULL,
    yoga INTEGER NOT NULL, 
	PRIMARY KEY (mem_id, num),
    FOREIGN KEY (mem_id) REFERENCES Members(mem_id)
);

CREATE TABLE Trainer (
    train_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    fee INTEGER NOT NULL
);

CREATE TABLE Trainer_times(
    train_id INTEGER NOT NULL,
    time_slot SERIAL NOT NULL, 
    time INTEGER NOT NULL,
    day INTEGER NOT NULL, 
    month VARCHAR(255) NOT NULL,
    year INTEGER NOT NULL, 
    scheduled BOOLEAN NOT NULL,
    PRIMARY KEY (train_id, time_slot),
    FOREIGN KEY (train_id) REFERENCES Trainer(train_id)
);
CREATE TABLE Trainer_member(
    train_id INTEGER NOT NULL, 
    mem_id INTEGER NOT NULL, 
    time_slot INTEGER NOT NULL,
    charged INTEGER NOT NULL,
     FOREIGN KEY (train_id, time_slot) REFERENCES Trainer_times(train_id, time_slot),
     FOREIGN KEY (mem_id) REFERENCES Members(mem_id)
);

CREATE TABLE Room (
    num SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE Class (
    class_id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    fee INTEGER NOT NULL,
    room_num INTEGER, 
    FOREIGN KEY (room_num) REFERENCES Room(num)
);

CREATE TABLE Class_member(
    mem_id INTEGER NOT NULL, 
    class_id INTEGER NOT NULL, 
    charged INTEGER NOT NULL,
    FOREIGN KEY (mem_id) REFERENCES Members(mem_id),
    FOREIGN KEY (Class_id) REFERENCES Class(class_id)
);

CREATE TABLE Staff(
    staff_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE Equipment(
    equip_id SERIAL PRIMARY KEY, 
    description VARCHAR(255) NOT NULL
);

CREATE TABLE Staff_equipment(
    staff_id INTEGER NOT NULL, 
    equip_id INTEGER NOT NULL, 
    date_maintained DATE NOT NULL, 
    FOREIGN KEY (staff_id) REFERENCES Staff(staff_id),
    FOREIGN KEY (equip_id) REFERENCES Equipment(equip_id) 
);
