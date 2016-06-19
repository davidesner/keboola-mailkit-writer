/*
 */
package esnerda.keboola.mailkit.writer.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public class LastState {

    @JsonProperty("lastRunDate")
    private Instant lastRunDate;

    public LastState(Instant lastRunDate) {
        this.lastRunDate = lastRunDate;
    }

    public Instant getLastRunDate() {
        return lastRunDate;
    }

    public void setLastRunDate(Instant lastRunDate) {
        this.lastRunDate = lastRunDate;
    }

    public LastState() {
    }

}
