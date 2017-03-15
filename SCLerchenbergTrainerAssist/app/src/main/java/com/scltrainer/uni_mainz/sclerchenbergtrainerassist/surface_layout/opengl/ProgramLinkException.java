/**
 * 
 */
package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

/**
 * @author Julian
 *
 */
public class ProgramLinkException extends Exception {

	private static final long serialVersionUID = -8475803770305624351L;

	/**
     * Constructs a new program link exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public ProgramLinkException(String message) {
        super(message);
    }
	
}
