package com.tarbus.models.routefinder;

import com.tarbus.models.BusStop;
import lombok.Getter;

@Getter
public class BusStopNode {
  private int nodeNumber;
  private Long firstBustStopId;
  private Long lastBustStopId;
  private Long lineId;
  private double distance;
  private Long timeStart;
  private Long timeEnd;
  private Long timeDelta;

  public static Builder builder() {
    return new Builder();
  }
  
  public static final class Builder {
    private int nodeNumber;
    private Long firstBustStopId;
    private Long lastBustStopId;
    private Long lineId;
    private double distance;
    private Long timeStart;
    private Long timeEnd;
    private Long timeDelta;

    public Builder nodeNumber(int nodeNumber) {
      this.nodeNumber = nodeNumber;
      return this;
    }

    public Builder firstBustStopId(Long firstBustStopId) {
      this.firstBustStopId = firstBustStopId;
      return this;
    }

    public Builder lastBustStopId(Long lastBustStopId) {
      this.lastBustStopId = lastBustStopId;
      return this;
    }

    public Builder lineId(Long lineId) {
      this.lineId = lineId;
      return this;
    }

    public Builder distance(double distance) {
      this.distance = distance;
      return this;
    }

    public Builder time(Long timeStart, Long timeEnd) {
      this.timeStart = timeStart;
      this.timeEnd = timeEnd;
      this.timeDelta = timeEnd - timeStart;
      return this;
    }

    public BusStopNode build() {
      BusStopNode busStopNode = new BusStopNode();
      busStopNode.nodeNumber = this.nodeNumber;
      busStopNode.firstBustStopId = this.firstBustStopId;
      busStopNode.lastBustStopId = this.lastBustStopId;
      busStopNode.lineId = this.lineId;
      busStopNode.distance = this.distance;
      busStopNode.timeStart = this.timeStart;
      busStopNode.timeEnd = this.timeEnd;
      busStopNode.timeDelta = this.timeDelta;
      return busStopNode;
    }
  }
  
  
}
