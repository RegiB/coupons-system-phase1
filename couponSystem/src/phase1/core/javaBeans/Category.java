package phase1.core.javaBeans;

import java.util.Arrays;

/**
 * this is an enum class, includes coupon categories to be used
 *
 */
public enum Category {

	FOOD, ELECTRICITY, RESTAURANT, VACATION, BEAUTY, HEALTH, BABY, SPORTS;

	public static String categories() {

		Category[] categories = Category.values();
		return Arrays.toString(categories);
	}

}
