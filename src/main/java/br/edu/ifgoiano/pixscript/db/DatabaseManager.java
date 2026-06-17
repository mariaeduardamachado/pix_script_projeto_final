package br.edu.ifgoiano.pixscript.db;

import br.edu.ifgoiano.pixscript.model.ErrorEntry;
import br.edu.ifgoiano.pixscript.model.SymbolEntry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DatabaseManager implements AutoCloseable {
    private final Connection connection;

    public DatabaseManager(Path databasePath) throws SQLException {
        try {
            Files.createDirectories(databasePath.getParent());
        } catch (Exception ignored) {
            // O H2 criará o arquivo do banco; este bloco apenas evita falha caso o diretório já exista.
        }
        String url = "jdbc:h2:file:" + databasePath.toAbsolutePath() + ";AUTO_SERVER=FALSE;MODE=MySQL";
        this.connection = DriverManager.getConnection(url, "sa", "");
        createTables();
    }

    private void createTables() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS codeinfo (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        filename VARCHAR(150) NOT NULL,
                        date DATE NOT NULL,
                        time TIME NOT NULL
                    )
                    """);
            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS symbols (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        lexeme VARCHAR(120) NOT NULL,
                        token VARCHAR(60) NOT NULL,
                        line INT NOT NULL,
                        column INT NOT NULL,
                        codeinfo_id BIGINT NOT NULL,
                        CONSTRAINT fk_symbols_codeinfo FOREIGN KEY (codeinfo_id) REFERENCES codeinfo(id),
                        CONSTRAINT uk_symbols_lexeme_per_code UNIQUE (codeinfo_id, lexeme)
                    )
                    """);
            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS errorlog (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        description TEXT NOT NULL,
                        line INT NOT NULL,
                        column INT NOT NULL,
                        type VARCHAR(45) NOT NULL,
                        codeinfo_id BIGINT NOT NULL,
                        CONSTRAINT fk_errorlog_codeinfo FOREIGN KEY (codeinfo_id) REFERENCES codeinfo(id)
                    )
                    """);
        }
    }

    public long insertCodeInfo(String filename) throws SQLException {
        String sql = "INSERT INTO codeinfo(filename, date, time) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, filename);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setTime(3, Time.valueOf(LocalTime.now().withNano(0)));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Não foi possível recuperar o ID do código analisado.");
    }

    public void insertSymbols(long codeInfoId, List<SymbolEntry> symbols) throws SQLException {
        String sql = "INSERT INTO symbols(lexeme, token, line, column, codeinfo_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (SymbolEntry symbol : symbols) {
                ps.setString(1, symbol.lexeme());
                ps.setString(2, symbol.token());
                ps.setInt(3, symbol.line());
                ps.setInt(4, symbol.column());
                ps.setLong(5, codeInfoId);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public void insertErrors(long codeInfoId, List<ErrorEntry> errors) throws SQLException {
        String sql = "INSERT INTO errorlog(description, line, column, type, codeinfo_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (ErrorEntry error : errors) {
                ps.setString(1, error.description());
                ps.setInt(2, error.line());
                ps.setInt(3, error.column());
                ps.setString(4, error.type());
                ps.setLong(5, codeInfoId);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
