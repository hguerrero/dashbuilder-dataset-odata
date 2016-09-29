package org.dashbuilder.dataprovider.backend.odata.exception;

/**
 * Generic exception for errors in any ODataClient service.
 */
public class ODataClientGenericException extends Exception 
{
	private static final long serialVersionUID = 1L;

	public ODataClientGenericException() {
    }

    public ODataClientGenericException(String message) {
        super(message);
    }

    public ODataClientGenericException(String message, Throwable cause) {
        super(message, cause);
    }

    public ODataClientGenericException(Throwable cause) {
        super(cause);
    }
}