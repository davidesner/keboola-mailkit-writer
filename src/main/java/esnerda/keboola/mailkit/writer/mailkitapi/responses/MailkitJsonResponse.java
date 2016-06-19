/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi.responses;

import esnerda.keboola.mailkit.writer.mailkitapi.responses.MailkitResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public class MailkitJsonResponse implements MailkitResponse {

    private ErrorResponse error;
    private final String filePath;
    private final String requestType;

    public MailkitJsonResponse(String filePath, String shortResponse, String requestType) {
        //check if is error
        this.requestType = requestType;
        final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        if (shortResponse != null) {
            try {
                this.error = mapper.readValue(shortResponse, ErrorResponse.class);
                if (this.error.error == null && this.error.error_status == null) {
                    this.error = null;
                }
            } catch (IOException ex) {
                this.error = null;
            }
        } else {
            this.error = null;
        }
        if (this.error != null) {
            this.filePath = null;
        } else {
            this.filePath = filePath;
        }

    }

    @Override
    public boolean isError() {
        return this.error != null;
    }

    @Override
    public String getErrorMessage() {
        return "API response error invoking function: " + this.requestType + ". " + error.getError() + ". Exited with error code:" + error.getError_status();
    }

    public InputStream getInputStream() throws Exception {
        if (filePath != null) {
            return new FileInputStream(filePath);
        } else {
            return null;
        }
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String getResultMessage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static class ErrorResponse {

        @JsonProperty("error_status")
        private Integer error_status;
        @JsonProperty("error")
        private String error;

        public ErrorResponse(Integer error_status, String error) {
            this.error_status = error_status;
            this.error = error;
        }

        public ErrorResponse() {
        }

        public Integer getError_status() {
            return error_status;
        }

        public void setError_status(Integer error_status) {
            this.error_status = error_status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

    }
}
