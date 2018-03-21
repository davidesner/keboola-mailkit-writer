/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi.responses;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public class ImportListResponse extends MailkitJsonResponse {

    private final String resultMessage;

    public ImportListResponse(String filePath, String shortResponse, String requestType) {
        super(filePath, shortResponse, requestType);
        final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ImportListRespWrapper resp;
        try {
            resp = mapper.readValue(shortResponse, ImportListRespWrapper.class);
        } catch (IOException ex) {
            this.resultMessage = "Cannot parse the response." + ex.getMessage();
            return;
        }
        this.resultMessage = buildMessage(resp);

    }

    private String buildMessage(ImportListRespWrapper resp) {
        String res = "Import finished successfuly. ";
        res += "\n Import info:";
        res += "\n invalid_emails: ";
        for (String e : resp.getInvalid_emails()) {
            res += e + ", ";
        }
        res += "\n count new: " + resp.getCount_new();
        res += "\n count bad: " + resp.getCount_bad();
        res += "\n count all: " + resp.getCount_all();
        res += "\n count updated: " + resp.getCount_updated();

        return res;
    }

    @Override
    public String getResultMessage() {
        return resultMessage;
    }

    private static class ImportListRespWrapper {

        private String[] invalid_emails;
        private String count_new;
        private String count_bad;
        private String count_all;
        private String count_updated;

        public ImportListRespWrapper() {
        }

        public String[] getInvalid_emails() {
            return invalid_emails;
        }

        public void setInvalid_emails(String[] invalid_emails) {
            this.invalid_emails = invalid_emails;
        }

        public String getCount_new() {
            return count_new;
        }

        public void setCount_new(String count_new) {
            this.count_new = count_new;
        }

        public String getCount_bad() {
            return count_bad;
        }

        public void setCount_bad(String count_bad) {
            this.count_bad = count_bad;
        }

        public String getCount_all() {
            return count_all;
        }

        public void setCount_all(String count_all) {
            this.count_all = count_all;
        }

        public String getCount_updated() {
            return count_updated;
        }

        public void setCount_updated(String count_updated) {
            this.count_updated = count_updated;
        }

    }
}
