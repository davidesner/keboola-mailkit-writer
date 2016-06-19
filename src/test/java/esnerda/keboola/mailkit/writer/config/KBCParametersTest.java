/*
 */
package esnerda.keboola.mailkit.writer.config;

import esnerda.keboola.mailkit.writer.mailkitapi.requests.StreamingListImport;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 */
public class KBCParametersTest extends TestCase {

    public KBCParametersTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of validateParametres method, of class KBCParameters.
     *
     * @throws esnerda.keboola.mailkit.writer.config.ValidationException
     */
    @Test(expected = ValidationException.class)
    public void validateParametersShouldThrowExceptionOnInvalidParams() throws ValidationException {
        System.out.println("validateParametres");

        KBCParameters instance = new KBCParameters();
        boolean expResult = false;
        boolean result = instance.validateParametres();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
