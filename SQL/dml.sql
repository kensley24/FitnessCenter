INSERT INTO Members (mem_id, first_name, goal_weight, goal_fitness, weight, fitness_lvl, memb_fee) 
VALUES 
(DEFAULT, 'Jake', 160, 5, 170, 4, 150),
(DEFAULT, 'Finn', 125, 7, 130, 6, 150),
(DEFAULT, 'BMO', 10, 6, 15, 5, 150);

-- Exercise_routine table
INSERT INTO Exercise_routine (mem_id, squats, lunges, pushups, yoga) 
VALUES 
(1, 50, 30, 20, 10),
(2, 40, 20, 30, 15),
(3, 60, 25, 25, 20);

-- Trainer table
INSERT INTO Trainer (train_id, first_name, fee) 
VALUES 
(DEFAULT, 'Iceking', 80),
(DEFAULT, 'Bubblegum', 75),
(DEFAULT, 'Marceline', 85);

-- Trainer_times table
INSERT INTO Trainer_times (train_id, time_slot, time, day, month, year, scheduled) 
VALUES 
(1, DEFAULT, 10, 9, 'April', 2024, true),
(2, DEFAULT, 14, 12, 'May', 2024, true),
(3, DEFAULT, 16, 15, 'June', 2024, true),
(1, DEFAULT, 12, 25, 'July', 2024, false),
(2, DEFAULT, 13, 20, 'July', 2024, false),
(3, DEFAULT, 8, 19, 'July', 2024, false);

-- Trainer_member table
INSERT INTO Trainer_member (train_id, mem_id, time_slot, charged) 
VALUES 
(1, 3, 1, 80),
(2, 1, 2, 75),
(3, 2, 3, 85);

-- Room table
INSERT INTO Room (num, description) 
VALUES 
(DEFAULT, 'Weightlifting room'),
(DEFAULT, 'Yoga studio'),
(DEFAULT, 'Central gym'),
(DEFAULT, 'Cardio room');

-- Class table
INSERT INTO Class (class_id, description, fee, room_num) 
VALUES 
(DEFAULT, 'Yoga for your heart', 10, 2),
(DEFAULT, 'HIIT cardio Workout', 15, 4),
(DEFAULT, 'LEG DAY', 20, 1);

-- Class_member table
INSERT INTO Class_member (mem_id, class_id, charged) 
VALUES 
(1, 1, 10),
(2, 2, 15),
(3, 3, 20);

-- Staff table
INSERT INTO Staff (staff_id, name) 
VALUES 
(DEFAULT, 'PeppermintB'),
(DEFAULT, 'Prismo'),
(DEFAULT, 'Ricardio');

-- Equipment table
INSERT INTO Equipment (equip_id, description) 
VALUES 
(DEFAULT, 'Treadmill'),
(DEFAULT, 'Dumbbells'),
(DEFAULT, 'Yoga mats'),
(DEFAULT, 'Lifting bars'), 
(DEFAULT, 'Mirrors');
