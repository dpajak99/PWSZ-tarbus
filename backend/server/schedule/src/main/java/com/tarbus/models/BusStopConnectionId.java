package com.tarbus.models;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BusStopConnectionId implements Serializable {

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "from_bus_stop", referencedColumnName = "id", nullable = false)
    private BusStop fromBusStopId;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "to_bus_stop", referencedColumnName = "id", nullable = false)
    private BusStop toBusStopId;


    public BusStopConnectionId(BusStop fromBusStopId, BusStop toBusStopId) {
        this.fromBusStopId = fromBusStopId;
        this.toBusStopId = toBusStopId;
    }

    public BusStopConnectionId() {

    }

    public BusStop getFromBusStopId() {
        return fromBusStopId;
    }

    public void setFromBusStopId(BusStop fromBusStopId) {
        this.fromBusStopId = fromBusStopId;
    }

    public BusStop getToBusStopId() {
        return toBusStopId;
    }

    public void setToBusStopId(BusStop toBusStopId) {
        this.toBusStopId = toBusStopId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusStopConnectionId that = (BusStopConnectionId) o;
        return Objects.equals(fromBusStopId, that.fromBusStopId) &&
          Objects.equals(toBusStopId, that.toBusStopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromBusStopId, toBusStopId);
    }
}