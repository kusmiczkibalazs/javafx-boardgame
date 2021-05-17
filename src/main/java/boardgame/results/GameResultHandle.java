package boardgame.results;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Class for creating and managing database connection.
 */
public class GameResultHandle {

    private static String filePathString = System.getProperty("user.home") + File.separator + ".gameresult";

    /**
     * Inserts the game results into the database.
     *
     * @param gameResult object containing gameplay data
     */
    public static void insertIntoResultTable(GameResult gameResult) {
        Jdbi jdbi = createDatabaseConnection();
        try(Handle handle = jdbi.open()) {
            GameResultDao dao = handle.attach(GameResultDao.class);
            dao.createTable();
            dao.insertResult(gameResult);
        }
    }

    /**
     *  Returns the content of the database ordered by the game's date in descending order.
     *
     * @return a {@code List} containing the stored gameplay data
     */
    public static List<GameResult> selectFromResultTable() {
        Jdbi jdbi = createDatabaseConnection();
        try(Handle handle = jdbi.open()) {
            GameResultDao dao = handle.attach(GameResultDao.class);
            dao.createTable();
            return dao.listGameResults();
        }
    }

    private static Jdbi createDatabaseConnection() {
        createDatabaseFolder();
        Jdbi jdbi = Jdbi.create("jdbc:h2:file:" + filePathString + File.separator + "result");
        jdbi.installPlugin(new SqlObjectPlugin());
        return jdbi;
    }

    private static void createDatabaseFolder() {
        if (Files.notExists(Path.of(filePathString))) {
            try {
                Files.createDirectory(Path.of(filePathString));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}