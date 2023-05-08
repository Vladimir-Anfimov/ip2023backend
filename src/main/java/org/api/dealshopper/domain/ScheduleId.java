package org.api.dealshopper.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleId implements Serializable {


    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "day_of_the_week")
    private Integer dayOfWeek;

}
