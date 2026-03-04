-- Criação do banco de dados e da tabela necessários para rodar o 'cinema.java'

CREATE DATABASE IF NOT EXISTS cinema;

USE cinema;

CREATE TABLE IF NOT EXISTS ingressos (
    id_ingresso INT AUTO_INCREMENT PRIMARY KEY,
    numero_assento VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL,
    id_sessao INT NOT NULL
);

CREATE TABLE IF NOT EXISTS idcinema (
    idcinema INT AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS sessoes (
    id_sessao INT AUTO_INCREMENT PRIMARY KEY,
    filme VARCHAR(100) NOT NULL,
    data_sessao DATE NOT NULL,
    horario TIME NOT NULL
);