package core.node;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Utility class for transforming to/from bytes an object,
 * used for sending/receiving objects with RabbitMQ
 */
public class ByteSerializable {
	/**
	 * Serialize an object (Transforms an object into a byte array).
	 * @param <E> this method works for any class that implements {@link Serializable}
	 * @param object The object to serialize
	 * @return the byte array corresponding to the object
	 */
	public static <E> byte[] getBytes (E object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    try {
		    ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.flush();
		    byte b[] = bos.toByteArray();
		    out.close();
		    bos.close();
		    
		    return b;
		}
	    catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Deserialize an object (Transforms a byte array into an Object).
	 * For a complete deserialization, cast the returned object to the desired class.
	 * @param byteArray the object to deserialize
	 * @return the deserialized object
	 */
	public static Object fromBytes(byte[] byteArray) {
		Object obj;
		ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);

		try {
			ObjectInput in = new ObjectInputStream(bis);
			obj = in.readObject();

			return obj;
		}
		catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
};
