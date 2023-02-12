package com.adidas.backend.prioritysaleservice.service.external;

import com.adidas.backend.prioritysaleservice.model.AdiClubSubscriptionMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * <p>Ad-hoc persistence services</p>
 * TODO this should be an external service/DB access
 *
 * @author <a href="mailto:andriyharyuk_95@hotmail.com">
 */
@Service
@Slf4j
public class PersistenceService {

    private static final PriorityQueue<AdiClubSubscriptionMember> subQueue = new PriorityQueue<>(Collections.reverseOrder());

    /**
     * <p>Add new subscriber to the sale</p>
     * @param subscription member that subscribes to the sale
     */
    public void add(AdiClubSubscriptionMember subscription) {
        log.info("Adding subscription to the queue{}", subscription);
        subQueue.add(subscription);
    }

    /**
     * @return top-oldest subscriber as winner of the sale and takes him out of the queue
     */
    public AdiClubSubscriptionMember getTopSubscriber() {
        if (!subQueue.isEmpty()) {
            AdiClubSubscriptionMember topSub = subQueue.poll();
            log.info("Top subscription retrieved from the queue {}", topSub);
            return topSub;
        }
        return null;
    }

    public List<AdiClubSubscriptionMember> getAllSubs() {
        return new ArrayList<>(subQueue);
    }
}
