/**
 * 
 */
package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl;

/**
 * @author Julian
 *
 */
public class ProgramNotLinkedException extends RuntimeException {


	private static final long serialVersionUID = -6023047330211826448L;

	/**
     * Constructs a new program not linked exception.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param name The name of the shader program.
     */
    public ProgramNotLinkedException(String name) {
        super(name + " is not linked");
    }
	
}
