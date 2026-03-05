-- Criação do banco de dados e das tabelas para o sistema Cinema Atlas

CREATE DATABASE IF NOT EXISTS cinema;

USE cinema;

-- Tabela para identificação do cinema
CREATE TABLE IF NOT EXISTS idcinema (idcinema INT PRIMARY KEY);

-- Tabela de salas
CREATE TABLE IF NOT EXISTS salas (
    id_sala INT AUTO_INCREMENT PRIMARY KEY,
    numero_sala INT NOT NULL,
    capacidade INT NOT NULL,
    tipo ENUM('2D', '3D', 'VIP') NOT NULL,
    id_cinema INT NOT NULL,
    FOREIGN KEY (id_cinema) REFERENCES idcinema (idcinema)
);

-- Tabela de filmes
CREATE TABLE IF NOT EXISTS filmes (
    id_filme INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    classificacao VARCHAR(10) NOT NULL,
    duracao_minutos INT NOT NULL
);

-- Tabela de sessões
CREATE TABLE IF NOT EXISTS sessoes (
    id_sessao INT AUTO_INCREMENT PRIMARY KEY,
    data_sessao DATE NOT NULL,
    horario TIME NOT NULL,
    valor_ingresso DECIMAL(8, 2) NOT NULL,
    id_filme INT NOT NULL,
    id_sala INT NOT NULL
);

-- Tabela de ingressos
CREATE TABLE IF NOT EXISTS ingressos (
    id_ingresso INT AUTO_INCREMENT PRIMARY KEY,
    numero_assento VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL,
    id_sessao INT NOT NULL,
    FOREIGN KEY (id_sessao) REFERENCES sessoes (id_sessao) ON DELETE CASCADE
);