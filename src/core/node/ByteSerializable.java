package core.node;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class ByteSerializable {
	public static <E> byte[] getBytes (E obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    try {
		    ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			out.flush();
		    byte b[] = bos.toByteArray();
		    out.close();
		    bos.close();
		    
		    return b;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object fromBytes(byte[] b) {
		Object obj;
		ByteArrayInputStream bis = new ByteArrayInputStream(b);

		try {
			ObjectInput in = new ObjectInputStream(bis);
			obj = in.readObject();

			return obj;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
};
