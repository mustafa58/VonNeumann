/**
 * 
 */
package errorHandling;

import java.io.IOException;

/**
 * @author mustafa
 *
 */
public class unknownCommandException extends IOException {
	private static final long serialVersionUID = 6463296500919225054L;
	public unknownCommandException(String msg) {
		super(msg);
	}
	public unknownCommandException() {
		super("Bu komut tanýmlý deðil.");
	}
}
