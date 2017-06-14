package io.github.alivety.conquerors.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generic object converter.
 * <p>
 * <h3>Use examples</h3>
 *
 * <pre>
 * Object o1 = Boolean.TRUE;
 * Integer i = ObjectConverter.convert(o1, Integer.class);
 * System.out.println(i); // 1
 *
 * Object o2 = "false";
 * Boolean b = ObjectConverter.convert(o2, Boolean.class);
 * System.out.println(b); // false
 *
 * Object o3 = new Integer(123);
 * String s = ObjectConverter.convert(o3, String.class);
 * System.out.println(s); // 123
 * </pre>
 *
 * Not all possible conversions are implemented. You can extend the
 * <tt>ObjectConverter</tt> easily by just adding a new method to it, with the
 * appropriate logic. For example:
 *
 * <pre>
 * public static ToObject fromObjectToObject(FromObject fromObject) {
 * 	// Implement.
 * }
 * </pre>
 *
 * The method name doesn't matter. It's all about the parameter type and the
 * return type.
 *
 * @author BalusC
 * @author Tomas Vik <tomas.vik@profinit.eu>
 * @link http://balusc.blogspot.com/2007/08/generic-object-converter.html
 */
public final class ObjectConverter {

	// Init
	// ---------------------------------------------------------------------------------------
	private static final Map<String, Method> CONVERTERS = new HashMap<String, Method>();
	/**
	 * Pattern used for string -> date converison
	 */
	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/**
	 * Date formater for string -> date conversion
	 */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(ObjectConverter.DATE_PATTERN);

	static {
		// Preload converters.
		final Method[] methods = ObjectConverter.class.getDeclaredMethods();
		for (final Method method : methods)
			if (method.getParameterTypes().length == 1)
				// Converter should accept 1 argument. This skips the convert()
				// method.
				ObjectConverter.CONVERTERS.put(method.getParameterTypes()[0].getName() + "_" + method.getReturnType().getName(), method);
	}

	/**
	 * Converts BigDecimal to Double.
	 *
	 * @param value
	 *            The BigDecimal to be converted.
	 * @return The converted Double value.
	 */
	public static Double bigDecimalToDouble(final BigDecimal value) {
		return new Double(value.doubleValue());
	}

	/**
	 * Converts Boolean to Integer. If boolean value is TRUE, then return 1,
	 * else return 0.
	 *
	 * @param value
	 *            The Boolean to be converted.
	 * @return The converted Integer value.
	 */
	public static Integer booleanToInteger(final Boolean value) {
		return value.booleanValue() ? Integer.valueOf(1) : Integer.valueOf(0);
	}

	/**
	 * Converts Boolean to String.
	 *
	 * @param value
	 *            The Boolean to be converted.
	 * @return The converted String value.
	 */
	public static String booleanToString(final Boolean value) {
		return value.toString();
	}

	// Action
	// -------------------------------------------------------------------------------------
	/**
	 * Convert the given object value to the given class.
	 *
	 * @param from
	 *            The object value to be converted.
	 * @param to
	 *            The type class which the given object should be converted to.
	 * @return The converted object value.
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NullPointerException
	 *             If 'to' is null.
	 * @throws UnsupportedOperationException
	 *             If no suitable converter can be found.
	 * @throws RuntimeException
	 *             If conversion failed somehow. This can be caused by at least
	 *             an ExceptionInInitializerError, IllegalAccessException or
	 *             InvocationTargetException.
	 */
	public static <T> Object convert(final Object from, final Class<T> to) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Main.out.debug(from);
		Main.out.debug(to);
		// Null is just null.
		if (from == null)
			return null;

		// Can we cast? Then just do it.
		if (to.isAssignableFrom(from.getClass()))
			return to.cast(from);

		// Lookup the suitable converter.
		final String converterId = from.getClass().getName() + "_" + to.getName();
		final Method converter = ObjectConverter.CONVERTERS.get(converterId);
		if (converter == null)
			throw new UnsupportedOperationException("Cannot convert from " + from.getClass().getName() + " to " + to.getName() + ". Requested converter does not exist.");

		return converter.invoke(ObjectConverter.CONVERTERS.get(converterId), from);

		// // Convert the value.
		// try {
		// return to.cast(converter.invoke(to, from));
		// } catch (Exception e) {
		// throw new RuntimeException("Cannot convert from "
		// + from.getClass().getName() + " to " + to.getName()
		// + ". Conversion failed with " + e.getMessage(), e);
		// }
	}

	/**
	 * Converts Double to BigDecimal.
	 *
	 * @param value
	 *            The Double to be converted.
	 * @return The converted BigDecimal value.
	 */
	public static BigDecimal doubleToBigDecimal(final Double value) {
		return new BigDecimal(value.doubleValue());
	}

	// Converters
	// ---------------------------------------------------------------------------------
	/**
	 * Converts Integer to Boolean. If integer value is 0, then return FALSE,
	 * else return TRUE.
	 *
	 * @param value
	 *            The Integer to be converted.
	 * @return The converted Boolean value.
	 */
	public static Boolean integerToBoolean(final Integer value) {
		return value.intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
	}

	public static int IntegerToInt(final Integer i) {
		return i.intValue();
	}

	/**
	 * Converts Integer to String.
	 *
	 * @param value
	 *            The Integer to be converted.
	 * @return The converted String value.
	 */
	public static String integerToString(final Integer value) {
		return value.toString();
	}

	public static Long intToLong(final Integer integer) {
		return new Long(integer);
	}

	public static BigDecimal stringToBigDecimal(final String value) {
		return new BigDecimal(value);
	}

	public static BigInteger stringToBigInteger(final String value) {
		return new BigInteger(value);
	}

	/**
	 * Converts String to Boolean.
	 *
	 * @param value
	 *            The String to be converted.
	 * @return The converted Boolean value.
	 */
	public static Boolean stringToBoolean(final String value) {
		return Boolean.valueOf(value);
	}

	public static Byte stringToByte(final String value) {
		return new Byte(value);
	}

	public static Character stringToChar(final String value) {
		return value.charAt(0);
	}

	/**
	 * Converts string to date. String has to have format given with constant
	 * DATE_PATTERN
	 *
	 * @param value
	 * @return
	 */
	public static Date stringToDate(final String value) {
		try {
			return ObjectConverter.DATE_FORMAT.parse(value);
		} catch (final ParseException ex) {
			Logger.getLogger(ObjectConverter.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException(ex);
		}
	}

	public static Double stringToDouble(final String value) {
		return new Double(value);
	}

	public static Float stringToFloat(final String value) {
		return new Float(value);
	}

	public static float stringToPrimitiveFloat(final String value) {
		return ObjectConverter.stringToFloat(value).floatValue();
	}

	public static String[] stringToStringArray(final String value) {
		return value.split(";");
	}

	public static int stringToInt(final String s) {
		return Integer.parseInt(s);
	}

	/**
	 * Converts String to Integer.
	 *
	 * @param value
	 *            The String to be converted.
	 * @return The converted Integer value.
	 */
	public static Integer stringToInteger(final String value) {
		return Integer.valueOf(value);
	}

	public static Long stringToLong(final String value) {
		return new Long(value);
	}

	public static Short stringToShort(final String value) {
		return new Short(value);
	}

	private ObjectConverter() {
		// Utility class, hide the constructor.
	}
}