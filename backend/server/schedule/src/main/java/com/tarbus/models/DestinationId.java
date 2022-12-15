package com.tarbus.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class DestinationId implements Serializable {

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    private Route route;

    @Column(name = "symbol")
    private String symbols;

    public DestinationId(Route id, String symbols) {
        this.route = id;
        this.symbols = symbols;
    }

    public Route getId() {
        return route;
    }

    public void setId(Route id) {
        this.route = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DestinationId that = (DestinationId) o;
        return Objects.equals(route.getId(), that.route.getId()) &&
          Objects.equals(symbols, that.symbols);
    }

    @Override
    public int hashCode() {
        return Objects.hash(route, symbols);
    }

    @Override
    public String toString() {
        return "DestinationId{" +
          "route=" + route +
          ", symbols='" + symbols + '\'' +
          '}';
    }
}