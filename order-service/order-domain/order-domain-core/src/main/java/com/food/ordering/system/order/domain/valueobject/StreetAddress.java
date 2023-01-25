package com.food.ordering.system.order.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class StreetAddress {
    private final UUID uuid;
    private final String street;
    private final String postalCode;
    private final String city;

}
