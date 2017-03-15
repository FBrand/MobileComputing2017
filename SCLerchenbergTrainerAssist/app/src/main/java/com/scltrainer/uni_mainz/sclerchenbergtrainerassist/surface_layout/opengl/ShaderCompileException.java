/**
 * 
 */
package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

/**
 * @author Julian
 *
 */
public class ShaderCompileException extends Exception {

	private static final long serialVersionUID = 4444002156721040877L;

    /**
     * Constructs a new shader compile exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public ShaderCompileException(String message) {
        super(message);
    }
	
}
