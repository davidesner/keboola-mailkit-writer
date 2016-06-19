/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi.responses;

/**
 * Basic contract for MailkitResponse objects
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public interface MailkitResponse {

    public boolean isError();

    public String getErrorMessage();

    public String getResultMessage();

}
