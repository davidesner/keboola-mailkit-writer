/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi;

import esnerda.keboola.mailkit.writer.mailkitapi.responses.MailkitResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Response wrapper for mailkit.campaigns.list call responses
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public class XmlRpcCampaignListResponse implements MailkitResponse {

    private final Map<String, Object>[] responseData;
    private final boolean isError;
    private final String errorMessage;

    public XmlRpcCampaignListResponse(Object responseData) {

        if (responseData instanceof Object[]) {
            this.isError = false;
            this.errorMessage = null;
            this.responseData = Arrays.copyOf((Object[]) responseData, ((Object[]) ((Object[]) responseData)).length, HashMap[].class);

        } else {
            this.responseData = null;
            this.isError = true;

            if (responseData instanceof String) {
                this.errorMessage = (String) responseData;
            } else {
                this.errorMessage = "Unexpected response structure: " + responseData.toString();
            }
        }
    }

    @Override
    public boolean isError() {
        return isError;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public Map<String, Object>[] getResponseData() {
        return responseData;
    }

    @Override
    public String getResultMessage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
