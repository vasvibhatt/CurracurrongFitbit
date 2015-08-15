package au.edu.sydney.Curracurrong.runtime.api.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FitbitApiSubscriptionStorageInMemoryImpl implements FitbitApiSubscriptionStorage {

	private List<LocalSubscriptionDetail> subscriptions = 
						Collections.synchronizedList(new ArrayList<LocalSubscriptionDetail>());
	
	public void delete(LocalSubscriptionDetail subscription) {
		subscriptions.remove(subscription);
	}

	public LocalSubscriptionDetail getBySubscriptionId(String subscriptionId) {
		for (LocalSubscriptionDetail subscription : subscriptions) {
			if (subscription.getSubscriptionDetail().getSubscriptionId().equals(subscriptionId)) {
				return subscription;
			}
		}
		return null;
	}

	public void save(LocalSubscriptionDetail subscription) {
		if (! subscriptions.contains(subscription)) {
			subscriptions.add(subscription);
		}
	}

	public List<LocalSubscriptionDetail> getAllSubscriptions() {
		return subscriptions;
	}

}
