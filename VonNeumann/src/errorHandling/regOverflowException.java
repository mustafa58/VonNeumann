/**
 * 
 */
package errorHandling;

import java.io.IOException;

/**
 * @author mustafa
 *
 */
public class regOverflowException extends IOException {
	private static final long serialVersionUID = 6287424627492626165L;
	public regOverflowException() {
		super("register mevcut de�eri saklamak i�in yetersiz");
	}
	public regOverflowException(String hataMesaj) {
		super(hataMesaj);
	}
}
