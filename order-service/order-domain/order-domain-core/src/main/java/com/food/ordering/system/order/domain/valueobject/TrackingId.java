package com.food.ordering.system.order.domain.valueobject;

import com.food.ordering.system.entity.BaseEntity;

import java.util.UUID;

public class TrackingId extends BaseEntity<UUID> {

    public TrackingId(UUID uuid) {
        super.setId(uuid);
    }
}
