package errorHandling;

import java.io.IOException;

public class invalidAddressException extends IOException {
	private static final long serialVersionUID = -2906146256402400318L;

	public invalidAddressException() {
		super("ge�ersiz bir adres kullan�ld�");
	}

	public invalidAddressException(String message) {
		super(message);
	}

}
