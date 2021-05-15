package boardgame.results;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GameResultHandle {

    private static String filePathString = System.getProperty("user.home") + File.separator + ".gameresult";

    public static void insertIntoResultTable(GameResult gameResult) {
        createDatabaseFolder();
        Jdbi jdbi = Jdbi.create("jdbc:h2:file:" + filePathString + File.separator + "result");
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()) {
            GameResultDao dao = handle.attach(GameResultDao.class);
            dao.createTable();
            dao.insertResult(gameResult);
        }
    }

    public static List<GameResult> selectFromResultTable() {
        createDatabaseFolder();
        Jdbi jdbi = Jdbi.create("jdbc:h2:file:" + filePathString + File.separator + "result");
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()) {
            GameResultDao dao = handle.attach(GameResultDao.class);
            dao.createTable();
            return dao.listGameResults();
        }
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