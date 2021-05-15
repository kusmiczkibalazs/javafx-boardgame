package boardgame.results;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GameResult {

    private String firstPlayer;
    private String secondPlayer;
    private String winner;
    private LocalDateTime gameDate;

}
