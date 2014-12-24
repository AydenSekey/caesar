package nxtim.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class DefautTypePromotionStrategieTest {
	/**
	 * Test que le type short est promouvable en type int.
	 */
	@Test
	public void testShortPromouvableEnInt() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertTrue("Le type short n'est pas promouvable en int.", strategie.isPromouvableEn(TypeElement.SHORT, TypeElement.INT));
	}
	
	/**
	 * Test que le type int est promouvable en type long.
	 */
	@Test
	public void testIntPromouvableEnLong() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertTrue("Le type int n'est pas promouvable en long.", strategie.isPromouvableEn(TypeElement.INT, TypeElement.LONG));
	}

	/**
	 * Test que le type long est promouvable en type float.
	 */
	@Test
	public void testLongPromouvableEnFloat() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertTrue("Le type long n'est pas promouvable en float.", strategie.isPromouvableEn(TypeElement.LONG, TypeElement.FLOAT));
	}

	/**
	 * Test que le type float est promouvable en type double.
	 */
	@Test
	public void testFloatPromouvableEnDouble() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertTrue("Le type float est promouvable en double.", strategie.isPromouvableEn(TypeElement.FLOAT, TypeElement.DOUBLE));
	}

	/**
	 * Test que le type short est promouvable en type long.
	 */
	@Test
	public void testShortPromouvableEnLong() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertTrue("Le type short n'est pas promouvable en long.", strategie.isPromouvableEn(TypeElement.SHORT, TypeElement.LONG));
	}

	/**
	 * Test que le type short est promouvable en type float.
	 */
	@Test
	public void testShortPromouvableEnFloat() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertTrue("Le type short n'est pas promouvable en float.", strategie.isPromouvableEn(TypeElement.SHORT, TypeElement.FLOAT));
	}

	/**
	 * Test que le type short est promouvable en type double.
	 */
	@Test
	public void testShortPromouvableEnDouble() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertTrue("Le type short n'est pas promouvable en double.", strategie.isPromouvableEn(TypeElement.SHORT, TypeElement.DOUBLE));
	}

	/**
	 * Test que le type int est promouvable en type float.
	 */
	@Test
	public void testIntPromouvableEnFloat() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertTrue("Le type int n'est pas promouvable en float.", strategie.isPromouvableEn(TypeElement.INT, TypeElement.FLOAT));
	}

	/**
	 * Test que le type int est promouvable en type double.
	 */
	@Test
	public void testIntPromouvableEnDouble() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertTrue("Le type int n'est pas promouvable en double.", strategie.isPromouvableEn(TypeElement.INT, TypeElement.DOUBLE));
	}

	/**
	 * Test que le type int n'est pas promouvable en type short.
	 */
	@Test
	public void testIntPromouvableEnShort() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertFalse("Le type int est promouvable en short.", strategie.isPromouvableEn(TypeElement.INT, TypeElement.SHORT));
	}

	/**
	 * Test que le type long n'est pas promouvable en type short.
	 */
	@Test
	public void testLongPromouvableEnShort() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertFalse("Le type long est promouvable en short.", strategie.isPromouvableEn(TypeElement.LONG, TypeElement.SHORT));
	}

	/**
	 * Test que le type long est promouvable en type double.
	 */
	@Test
	public void testLongPromouvableEnDouble() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertTrue("Le type long est promouvable en double.", strategie.isPromouvableEn(TypeElement.LONG, TypeElement.DOUBLE));
	}

	/**
	 * Test que le type float n'est pas promouvable en type short.
	 */
	@Test
	public void testFloatPromouvableEnShort() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertFalse("Le type float est promouvable en short.", strategie.isPromouvableEn(TypeElement.FLOAT, TypeElement.SHORT));
	}

	/**
	 * Test que le type float n'est pas promouvable en type int.
	 */
	@Test
	public void testFloatPromouvableEnInt() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertFalse("Le type float est promouvable en int.", strategie.isPromouvableEn(TypeElement.FLOAT, TypeElement.INT));
	}

	/**
	 * Test que le type float n'est pas promouvable en type long.
	 */
	@Test
	public void testFloatPromouvableEnLong() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertFalse("Le type float est promouvable en long.", strategie.isPromouvableEn(TypeElement.FLOAT, TypeElement.LONG));
	}

	/**
	 * Test que le type double n'est pas promouvable en type short.
	 */
	@Test
	public void testDoublePromouvableEnShort() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertFalse("Le type double est promouvable en short.", strategie.isPromouvableEn(TypeElement.DOUBLE, TypeElement.SHORT));
	}

	/**
	 * Test que le type double n'est pas promouvable en type int.
	 */
	@Test
	public void testDoublePromouvableEnInt() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertFalse("Le type double est promouvable en int.", strategie.isPromouvableEn(TypeElement.DOUBLE, TypeElement.INT));
	}

	/**
	 * Test que le type double n'est pas promouvable en type long.
	 */
	@Test
	public void testDoublePromouvableEnLong() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertFalse("Le type double est promouvable en long.", strategie.isPromouvableEn(TypeElement.DOUBLE, TypeElement.LONG));
	}

	/**
	 * Test que le type long n'est pas promouvable en type short.
	 */
	@Test
	public void testLongPromouvableEnInt() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertFalse("Le type long est promouvable en int.", strategie.isPromouvableEn(TypeElement.LONG, TypeElement.INT));
	}

	/**
	 * Test qu'un type est promouvable en lui-même.
	 */
	@Test
	public void testPromouvableEnLuiMeme() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertTrue("Le type short n'est pas promouvable en short.", strategie.isPromouvableEn(TypeElement.SHORT, TypeElement.SHORT));
		assertTrue("Le type int n'est pas promouvable en int.", strategie.isPromouvableEn(TypeElement.INT, TypeElement.INT));
		assertTrue("Le type long n'est pas promouvable en long.", strategie.isPromouvableEn(TypeElement.LONG, TypeElement.LONG));
		assertTrue("Le type float n'est pas promouvable en float.", strategie.isPromouvableEn(TypeElement.FLOAT, TypeElement.FLOAT));
		assertTrue("Le type double n'est pas promouvable en double.", strategie.isPromouvableEn(TypeElement.DOUBLE, TypeElement.DOUBLE));
		assertTrue("Le type bool n'est pas promouvable en bool.", strategie.isPromouvableEn(TypeElement.BOOL, TypeElement.BOOL));
		assertTrue("Le type string n'est pas promouvable en string.", strategie.isPromouvableEn(TypeElement.STRING, TypeElement.STRING));
		assertTrue("Le type list n'est pas promouvable en list.", strategie.isPromouvableEn(TypeElement.LIST, TypeElement.LIST));
	}

	/**
	 * Test que le type double n'est pas promouvable en type float.
	 */
	@Test
	public void testDoublePromouvableEnFloat() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertFalse("Le type double n'est pas promouvable en float.", strategie.isPromouvableEn(TypeElement.DOUBLE, TypeElement.FLOAT));
	}

	/**
	 * Test les types qui doivent être promouvables.
	 */
	@Test
	public void testPromouvable() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertTrue("Le type short n'est pas promouvable", strategie.isPromouvable(TypeElement.SHORT));
		assertTrue("Le type int n'est pas promouvable", strategie.isPromouvable(TypeElement.INT));
		assertTrue("Le type long n'est pas promouvable", strategie.isPromouvable(TypeElement.LONG));
		assertTrue("Le type float n'est pas promouvable", strategie.isPromouvable(TypeElement.FLOAT));
	}
	
	/**
	 * Test les types qui ne doivent pas être promouvables.
	 */
	@Test
	public void testNonPromouvable() {
		TypePromotionStrategie strategie = new DefautTypePromotionStrategie();
		assertFalse("Le type double est promouvable", strategie.isPromouvable(TypeElement.DOUBLE));
		assertFalse("Le type list est promouvable", strategie.isPromouvable(TypeElement.LIST));
		assertFalse("Le type string est promouvable", strategie.isPromouvable(TypeElement.STRING));
	}
}
