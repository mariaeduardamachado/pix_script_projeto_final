CREATE TABLE IF NOT EXISTS codeinfo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    filename VARCHAR(150) NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL
);

CREATE TABLE IF NOT EXISTS symbols (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lexeme VARCHAR(120) NOT NULL,
    token VARCHAR(60) NOT NULL,
    line INT NOT NULL,
    column INT NOT NULL,
    codeinfo_id BIGINT NOT NULL,
    CONSTRAINT fk_symbols_codeinfo FOREIGN KEY (codeinfo_id) REFERENCES codeinfo(id),
    CONSTRAINT uk_symbols_lexeme_per_code UNIQUE (codeinfo_id, lexeme)
);

CREATE TABLE IF NOT EXISTS errorlog (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description TEXT NOT NULL,
    line INT NOT NULL,
    column INT NOT NULL,
    type VARCHAR(45) NOT NULL,
    codeinfo_id BIGINT NOT NULL,
    CONSTRAINT fk_errorlog_codeinfo FOREIGN KEY (codeinfo_id) REFERENCES codeinfo(id)
);
