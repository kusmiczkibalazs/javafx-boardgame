package boardgame.results;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(GameResult.class)
public interface GameResultDao {

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

    @SqlUpdate("""
       INSERT INTO results (firstPlayer, secondPlayer, winner, gameDate)
       VALUES (:firstPlayer, :secondPlayer, :winner, :gameDate)
       """)
    @GetGeneratedKeys
    long insertResult(@BindBean GameResult gameResult);

    @SqlQuery("SELECT * FROM results ORDER BY gameDate DESC")
    List<GameResult> listGameResults();
}
