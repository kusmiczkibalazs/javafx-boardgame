package boardgame.results;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * DAO interface containing the SQL statements to manage the database of the results.
 */
@RegisterBeanMapper(GameResult.class)
public interface GameResultDao {

    /**
     * Creates a database table containing gameplay data if it had not existed before.
     */
    @SqlUpdate("""
        CREATE TABLE IF NOT EXISTS results (
            id IDENTITY PRIMARY KEY,
            firstPlayer VARCHAR NOT NULL,
            secondPlayer VARCHAR NOT NULL,
            winner VARCHAR NOT NULL,
            gameDate TIMESTAMP NOT NULL
        )
        """
    )
    void createTable();

    /**
     * Inserts the data of the {@code GameResult} object into the database.
     *
     * @param gameResult object containing gameplay data
     * @return the primary key associated with the inserted database record
     */
    @SqlUpdate("""
       INSERT INTO results (firstPlayer, secondPlayer, winner, gameDate)
       VALUES (:firstPlayer, :secondPlayer, :winner, :gameDate)
       """)
    @GetGeneratedKeys
    long insertResult(@BindBean GameResult gameResult);

    /**
     * Method used to query gameplay data from the database.
     *
     * @return a {@code List} containing the stored gameplay data
     */
    @SqlQuery("SELECT * FROM results ORDER BY gameDate DESC")
    List<GameResult> listGameResults();
}
