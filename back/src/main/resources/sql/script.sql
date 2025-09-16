-- Script de données de test pour l'application Yoga
-- Base de données: test

-- Nettoyage des tables (dans l'ordre inverse des dépendances)
DELETE FROM participate;
DELETE FROM sessions;
DELETE FROM teachers;
DELETE FROM users;

-- Insertion des enseignants
INSERT INTO teachers (id, first_name, last_name, created_at, updated_at) VALUES
(1, 'Margot', 'DELAHAYE', NOW(), NOW()),
(2, 'Hélène', 'MARCHAND', NOW(), NOW()),
(3, 'Martial', 'Ménard', NOW(), NOW());

-- Insertion des utilisateurs
INSERT INTO users (id, email, first_name, last_name, password, admin, created_at, updated_at) VALUES
(1, 'yoga@studio.com', 'Admin', 'User', '$2a$10$8gAba5j61jQHMwSXfN3w7ODDN7Vb5FOzXYZALXx914FgfEf0kM/v.', true, NOW(), NOW()),
(2, 'user@studio.com', 'Regular', 'User', '$2a$10$8gAba5j61jQHMwSXfN3w7ODDN7Vb5FOzXYZALXx914FgfEf0kM/v.', false, NOW(), NOW()),
(3, 'teacher@studio.com', 'Teacher', 'User', '$2a$10$8gAba5j61jQHMwSXfN3w7ODDN7Vb5FOzXYZALXx914FgfEf0kM/v.', false, NOW(), NOW());

-- Insertion des sessions
INSERT INTO sessions (id, name, date, description, teacher_id, created_at, updated_at) VALUES
(1, 'Yoga Débutant', '2024-01-15 10:00:00', 'Session de yoga pour débutants', 1, NOW(), NOW()),
(2, 'Yoga Avancé', '2024-01-16 14:00:00', 'Session de yoga avancée', 2, NOW(), NOW()),
(3, 'Méditation', '2024-01-17 18:00:00', 'Session de méditation guidée', 3, NOW(), NOW());

-- Insertion des participations utilisateurs aux sessions
INSERT INTO participate (user_id, session_id) VALUES
(2, 1),
(3, 1),
(3, 3);
