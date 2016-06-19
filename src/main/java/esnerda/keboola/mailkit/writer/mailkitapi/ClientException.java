/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi;

import esnerda.keboola.mailkit.writer.KBCException;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2015
 */
public class ClientException extends KBCException {

    public ClientException(String message) {
        super(message);
    }
}
