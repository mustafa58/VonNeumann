/**
 * 
 */
package errorHandling;

import java.io.IOException;

/**
 * @author mustafa
 *
 */
public class invalidRAMsizeException extends IOException {
	private static final long serialVersionUID = -5251565980374786421L;
	public invalidRAMsizeException() {
		super("verilen size ile istenilen uyuþmuyor");
	}
	public invalidRAMsizeException(String hataMes) {
		super(hataMes);
	}
}
