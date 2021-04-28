package phase1.core.dailyJob;

import java.util.List;

import phase1.core.dao.CouponsDAO;
import phase1.core.dao.db.CouponsDBDAO;
import phase1.core.javaBeans.Coupon;
import phase1.core.systemExceptions.DaoException;

/**
 * thread class that runs in parallel with the main coupon system
 *
 */
public class CouponExpirationDailyJob implements Runnable {

	private CouponsDAO couponsDAO;
	private boolean quit;

	public CouponExpirationDailyJob() {
		this.couponsDAO = new CouponsDBDAO();
	}

	/**
	 * a method that executes the following on daily basis: deleting purchases of
	 * expired coupons and deleting expired coupons
	 */
	@Override
	public void run() {

		System.out.println("thread is running");

		try {
			while (!quit) {
				List<Coupon> expiredCoupons = couponsDAO.getExpiredCoupons();

				if (!expiredCoupons.isEmpty()) {

					for (Coupon currCoupon : expiredCoupons) {

						System.out.println();
						couponsDAO.deleteCouponPurchases(currCoupon.getId());
						couponsDAO.deleteCoupon(currCoupon.getId());

					}
					System.out.println("expired coupons removed from the system");
				}
				Thread.sleep(24 * 60 * 60 * 1000);
			}
		} catch (DaoException e) {
			System.err.println("removing expired coupons - failed ");
			e.printStackTrace();

		} catch (InterruptedException e) {
			System.out.println("thread stopped running");

		}

	}

	/**
	 * a method that makes the running daily job to stop
	 */
	public void stop() {

		quit = true;

	}

}
