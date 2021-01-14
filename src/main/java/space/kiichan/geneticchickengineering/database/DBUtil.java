package space.kiichan.geneticchickengineering.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBUtil {
    private String datadir;
    private Logger logger;
    private Connection conn;
    // Track when we last made changes to save db calls
    private boolean delta = true;
    private List<String[]> cache;

    public DBUtil(String datadir, Logger logger) {
        this.datadir = datadir;
        if (!this.datadir.endsWith(File.separator)) {
            this.datadir = this.datadir + File.separator;
        }
        this.logger = logger;
        this.connect();
    }

    public boolean checkForConnection() {
        return this.conn != null;
    }

    public void connect() {
        this.conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:"+this.datadir+"GCE.db";
            // create a connection to the database
            this.conn = DriverManager.getConnection(url);

            if (this.checkForConnection()) {
                this.conn.setAutoCommit(false);
                this.logger.info("Connection to "+this.datadir+"GCE.db has been established.");
                this.create_table();
            } else {
                this.logger.log(Level.SEVERE, "Connection to the database has failed!");
            }

        } catch (SQLException e) {
            this.logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public void commit() {
        if (!this.checkForConnection()) {
            return;
        }
        try {
            this.conn.commit();
        } catch (SQLException ex) {
            this.logger.log(Level.SEVERE, ex.getMessage());
        }
    }

    public void close() {
        if (!this.checkForConnection()) {
            return;
        }
        try {
            this.conn.commit();
            this.conn.close();
            this.logger.info("Connection to "+this.datadir+"GCE.db has been closed.");
        } catch (SQLException ex) {
            this.logger.log(Level.SEVERE, ex.getMessage());
        }
    }

    public void create_table() {
        if (!this.checkForConnection()) {
            return;
        }
        try{
            String sql = "CREATE TABLE IF NOT EXISTS entities ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "uuid TEXT NOT NULL,"+
                "dna TEXT NOT NULL);";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            this.conn.commit();
        } catch (SQLException e) {
            this.logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public void delete(String uuid) {
        if (!this.checkForConnection()) {
            return;
        }
        String sql = "DELETE FROM entities WHERE uuid = ?;";
        try {
            PreparedStatement s = this.conn.prepareStatement(sql);
            s.setString(1, uuid);
            s.executeUpdate();
            this.delta = true;
        } catch (SQLException e) {
            this.logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public List<String[]> getAll() {
        List<String[]> out = new ArrayList<String[]>();
        if (this.checkForConnection()) {
            if (!this.delta) {
                return this.cache;
            }
            String sql = "SELECT uuid, dna FROM entities;";
            try {
                Statement s = this.conn.createStatement();
                ResultSet rs = s.executeQuery(sql);
                while (rs.next()) {
                    String[] record = new String[2];
                    record[0] = rs.getString("uuid");
                    record[1] = rs.getString("dna");
                    out.add(record);
                }
                this.cache = out;
                this.delta = false;
            } catch (SQLException e) {
                this.logger.log(Level.SEVERE, e.getMessage());
            }
        }
        return out;
    }

    public boolean has(String uuid) {
        // refresh the cache if need be
        this.getAll();
        for (int i=0; i<this.cache.size(); i++) {
            if (uuid.equals(this.cache.get(i)[0])) {
                return true;
            }
        }
        return false;
    }

    public String getDNAOrNull(String uuid) {
        // refresh the cache if need be
        this.getAll();
        for (int i=0; i<this.cache.size(); i++) {
            if (uuid.equals(this.cache.get(i)[0])) {
                return this.cache.get(i)[1];
            }
        }
        return null;
    }

    public void insert(String uuid, String dna) {
        
        String sql = "INSERT INTO entities (uuid, dna) VALUES (?,?);";
        try {
            PreparedStatement s = this.conn.prepareStatement(sql);
            s.setString(1, uuid);
            s.setString(2, dna);
            s.executeUpdate();
            this.delta = true;
        } catch (SQLException e) {
            this.logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
