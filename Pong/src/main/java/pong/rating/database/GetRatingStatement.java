
package pong.rating.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * Implements the SQlStatements. Class is responsible of getting the rating of
 * a given player by name.
 */
public class GetRatingStatement extends SqlStatement {

    
    public GetRatingStatement(String url) {
        super(url);
    }

    @Override
    protected void execute() throws SQLException, ClassNotFoundException {
        connect();
        String query = "SELECT rating FROM ratings WHERE name = ? ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            rating = result.getInt("rating");
        }

    }

}
