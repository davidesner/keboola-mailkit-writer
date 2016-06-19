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
public class CreateListResponse extends MailkitJsonResponse {

    private final String resultMessage;
    private final String listId;

    public CreateListResponse(String filePath, String shortResponse, String requestType) {
        super(filePath, shortResponse, requestType);
        final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CreateListRespWrapper resp;
        try {
            resp = mapper.readValue(shortResponse, CreateListRespWrapper.class);
        } catch (IOException ex) {
            this.resultMessage = "Cannot parse the response.";
            this.listId = null;
            return;
        }
        if (resp.data != null) {
            this.listId = resp.data;
            this.resultMessage = "List with ID:" + resp.data + " successfuly created.";
        } else {
            this.listId = null;
            this.resultMessage = "Error.";
        }
    }

    public String getListId() {
        return this.listId;
    }

    @Override
    public String getResultMessage() {
        return resultMessage;
    }

    private static class CreateListRespWrapper {

        private String data;

        public CreateListRespWrapper() {
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

    }
}
