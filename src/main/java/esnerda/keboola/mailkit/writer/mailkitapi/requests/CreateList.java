/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi.requests;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public class CreateList extends MailkitJsonRequest {

    public CreateList(String name, String description) {
        super("mailkit.mailinglist.create");
        addParameter("name", name);
        if (description != null) {
            addParameter("description", description);
        }
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
