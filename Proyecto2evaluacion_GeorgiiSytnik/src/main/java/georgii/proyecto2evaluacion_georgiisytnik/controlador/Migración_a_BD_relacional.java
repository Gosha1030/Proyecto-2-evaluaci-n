/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package georgii.proyecto2evaluacion_georgiisytnik.controlador;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Migración_a_BD_relacional {

    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/bd_SlimeAndFood";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASS = "";

    private static final String MONGO_URI = "mongodb://localhost:27017";

    public static void main(String[] args) {
        try {
            Connection conexion = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
            Statement stmt = conexion.createStatement();

            crearTablas(stmt);
            try {
                stmt.executeUpdate(
                        "ALTER TABLE food DROP FOREIGN KEY fk_food_of_slime"
                );
                stmt.executeUpdate(
                        "ALTER TABLE slime DROP FOREIGN KEY fk_favourite_food"
                );
/*
                stmt.executeUpdate(
                        "ALTER TABLE food DROP FOREIGN KEY fk_type_of_diet"
                );
                stmt.executeUpdate(
                        "ALTER TABLE slime DROP FOREIGN KEY fk_diet_type"
                );
*/
            } catch (SQLException e) {
            }
            stmt.executeUpdate("DELETE FROM food");
            stmt.executeUpdate("DELETE FROM slime");

            try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
                MongoDatabase mongoDB = mongoClient.getDatabase("SlimeAndFood");
                MongoCollection<Document> slimeColl = mongoDB.getCollection("slimes");
                MongoCollection<Document> foodColl = mongoDB.getCollection("foods");

                for (Document s : slimeColl.find()) {
                    String sql = String.format(
                            "INSERT INTO slime VALUES ('%s','%s','%s','%s','%s')",
                            s.getString("_id"),
                            s.getString("name"),
                            s.getString("diet"),
                            s.getString("favouriteFood"),
                            s.getString("type")
                    );
                    stmt.executeUpdate(sql);
                }

                for (Document f : foodColl.find()) {
                    String sql = String.format(
                            "INSERT INTO food VALUES ('%s','%s','%s','%s')",
                            f.getString("_id"),
                            f.getString("name"),
                            f.getString("type"),
                            f.getString("slimeId")
                    );
                    stmt.executeUpdate(sql);
                }
            }
            crearFK(stmt);

            String sql = "SELECT s.name, f.name FROM slime s LEFT JOIN food f ON s.id = f.slime_id";
            boolean isResultSet = stmt.execute(sql);

            if (isResultSet) {
                ResultSet rs = stmt.getResultSet();
                System.out.println("Slimes y sus comidas favoritas");
                while (rs.next()) {
                    System.out.println(rs.getString(1) + " -> " + rs.getString(2));
                }
                rs.close();
            }

            stmt.close();
            conexion.close();
            System.out.println("Fin");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void crearTablas(Statement stmt) throws SQLException {
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS slime ("
                + "id VARCHAR(50) PRIMARY KEY,"
                + "name VARCHAR(50),"
                + "diet VARCHAR(30),"
                + "favourite_food VARCHAR(50) NULL,"
                + "type VARCHAR(30)"
                + ")"
        );

        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS food ("
                + "id VARCHAR(50) PRIMARY KEY,"
                + "name VARCHAR(50),"
                + "type VARCHAR(30),"
                + "slime_id VARCHAR(50) NULL"
                + ")"
        );
    }

    private static void crearFK(Statement stmt) throws SQLException {
        stmt.executeUpdate(
                "UPDATE slime SET favourite_food = NULL "
                + "WHERE favourite_food = 'no' OR favourite_food = ''"
        );

        stmt.executeUpdate(
                "UPDATE food SET slime_id = NULL "
                + "WHERE slime_id = 'no' OR slime_id = ''"
        );

        stmt.executeUpdate(
                "ALTER TABLE food "
                + "ADD CONSTRAINT fk_food_of_slime "
                + "FOREIGN KEY (slime_id) REFERENCES slime(id)"
        );

        stmt.executeUpdate(
                "ALTER TABLE slime "
                + "ADD CONSTRAINT fk_favorite_food "
                + "FOREIGN KEY (favourite_food) REFERENCES food(id)"
        );
/*
        stmt.executeUpdate(
                "ALTER TABLE food "
                + "ADD CONSTRAINT fk_type_of_diet "
                + "FOREIGN KEY (type) REFERENCES slime(diet)"
        );

        stmt.executeUpdate(
                "ALTER TABLE slime "
                + "ADD CONSTRAINT fk_diet_type "
                + "FOREIGN KEY (diet) REFERENCES food(type)"
        );
*/
    }
}
