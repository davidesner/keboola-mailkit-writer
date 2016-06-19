/*
 */
package esnerda.keboola.mailkit.writer.config;

import esnerda.keboola.mailkit.writer.mailkitapi.requests.StreamingListImport;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 */
@RunWith(JUnit4.class)

public class KBCParametersTest extends TestCase {

    public KBCParametersTest() {
    }

    /**
     * Test of validateParametres method, of class KBCParameters.
     *
     * @throws esnerda.keboola.mailkit.writer.config.ValidationException
     */
    @Test(expected = ValidationException.class)
    public void validateParametersShouldFailOnNullParams() throws ValidationException {
        System.out.println("validateParametres");
        KBCParameters instance = new KBCParameters(null, null, null, null, null);
        assertEquals(false, instance.validateParametres());

    }

    @Test(expected = ValidationException.class)
    public void validateParametersShouldFailOnEmptyParams() throws ValidationException {
        KBCParameters instance = new KBCParameters("", "", "", new ArrayList<>(), new KBCParameters.NewList());
        assertEquals(false, instance.validateParametres());

    }

    @Test(expected = ValidationException.class)
    public void validateParametersShouldFailOnEmptyListId() throws ValidationException {
        KBCParameters instance = new KBCParameters("", "", "", new ArrayList<>(), new KBCParameters.NewList());
        assertEquals(false, instance.validateParametres());
        instance = new KBCParameters("", null, "", new ArrayList<>(), new KBCParameters.NewList());
        assertEquals(false, instance.validateParametres());

    }

}
