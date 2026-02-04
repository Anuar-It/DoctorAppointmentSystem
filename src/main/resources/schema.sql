CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     full_name VARCHAR(80) NOT NULL,
    username VARCHAR(40) UNIQUE NOT NULL,
    password_hash VARCHAR(120) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('PATIENT','DOCTOR','ADMIN')),
    created_at TIMESTAMP NOT NULL DEFAULT now()
    );

CREATE TABLE IF NOT EXISTS specializations (
                                               id SERIAL PRIMARY KEY,
                                               name VARCHAR(60) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS doctors (
                                       id SERIAL PRIMARY KEY,
                                       user_id INT UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    specialization_id INT NOT NULL REFERENCES specializations(id),
    cabinet VARCHAR(20),
    active BOOLEAN NOT NULL DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS appointments (
                                            id SERIAL PRIMARY KEY,
                                            doctor_id INT NOT NULL REFERENCES doctors(id),
    patient_id INT NOT NULL REFERENCES users(id),
    start_at TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('BOOKED','CANCELLED','RESCHEDULED','DONE')),
    reason VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uq_doctor_slot UNIQUE (doctor_id, start_at)
    );

CREATE TABLE IF NOT EXISTS appointment_history (
                                                   id SERIAL PRIMARY KEY,
                                                   appointment_id INT NOT NULL REFERENCES appointments(id) ON DELETE CASCADE,
    action VARCHAR(30) NOT NULL CHECK (action IN ('BOOK','CANCEL','RESCHEDULE','COMPLETE')),
    old_start_at TIMESTAMP,
    new_start_at TIMESTAMP,
    actor_user_id INT NOT NULL REFERENCES users(id),
    note VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT now()
    );
