package com.depremnobetcisi.domain.port.input;

import com.depremnobetcisi.domain.model.Earthquake;

public interface NotificationDispatchUseCase {
    void notifyUsersForEarthquake(Earthquake earthquake);
}
