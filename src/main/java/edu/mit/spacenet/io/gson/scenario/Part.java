package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.threeten.extra.PeriodDuration;

import edu.mit.spacenet.domain.element.PartApplication;
import edu.mit.spacenet.domain.resource.Item;

public class Part implements Cloneable {
  protected UUID resource;
  protected PeriodDuration meanTimeToFailure;
  protected PeriodDuration meanTimeToRepair;
  protected Double massToRepair;
  protected Double quantity;
  protected Double dutyCycle;

  public static Part createFrom(edu.mit.spacenet.domain.element.PartApplication part,
      Context context) {
    Part p = new Part();
    p.resource = context.getJsonIdFromJavaObject(part.getPart());
    if (part.getMeanTimeToFailure() > 0) {
      p.meanTimeToFailure = PeriodDuration.of(Duration.ofSeconds((int) part.getMeanTimeToFailure() * 60 * 60));
    } else {
      p.meanTimeToFailure = null;
    }
    if (part.getMeanTimeToRepair() > 0) {
      p.meanTimeToRepair = PeriodDuration.of(Duration.ofSeconds((int) part.getMeanTimeToRepair() * 60 * 60));
    } else {
      p.meanTimeToRepair = null;
    }
    if (part.getMassToRepair() >= 0) {
      p.massToRepair = part.getMassToRepair();
    } else {
      p.massToRepair = null;
    }
    p.quantity = part.getQuantity();
    p.dutyCycle = part.getDutyCycle();
    return p;
  }

  public static List<Part> createFrom(
      Collection<edu.mit.spacenet.domain.element.PartApplication> parts, Context context) {
    List<Part> ps = new ArrayList<Part>();
    for (edu.mit.spacenet.domain.element.PartApplication p : parts) {
      ps.add(Part.createFrom(p, context));
    }
    return ps;
  }

  public edu.mit.spacenet.domain.element.PartApplication toSpaceNet(Context context) {
    edu.mit.spacenet.domain.element.PartApplication p =
        new edu.mit.spacenet.domain.element.PartApplication();
    p.setPart((Item) context.getJavaObjectFromJsonId(resource));
    if (meanTimeToFailure != null) {
      p.setMeanTimeToFailure(meanTimeToFailure.getDuration().getSeconds() / (60 * 60d));
    } else {
      p.setMeanTimeToFailure(0);
    }
    if (meanTimeToRepair != null) {
      p.setMeanTimeToRepair(meanTimeToRepair.getDuration().getSeconds() / (60 * 60d));
    } else {
      p.setMeanTimeToRepair(0);
    }
    p.setMassToRepair(massToRepair);
    p.setQuantity(quantity);
    p.setDutyCycle(dutyCycle);
    return p;
  }

  public static SortedSet<PartApplication> toSpaceNet(Collection<Part> parts, Context context) {
    SortedSet<PartApplication> ps = new TreeSet<PartApplication>();
    if (parts != null) {
      for (Part part : parts) {
        ps.add(part.toSpaceNet(context));
      }
    }
    return ps;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Part)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final Part other = (Part) obj;
    return new EqualsBuilder().append(resource, other.resource)
        .append(meanTimeToFailure, other.meanTimeToFailure)
        .append(meanTimeToRepair, other.meanTimeToRepair).append(massToRepair, other.massToRepair)
        .append(quantity, other.quantity).append(dutyCycle, other.dutyCycle).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31).append(resource).append(meanTimeToFailure)
        .append(meanTimeToRepair).append(massToRepair).append(quantity).append(dutyCycle)
        .toHashCode();
  }

  @Override
  public Part clone() {
    Part p = new Part();
    p.resource = resource;
    p.meanTimeToFailure = meanTimeToFailure;
    p.meanTimeToRepair = meanTimeToRepair;
    p.massToRepair = massToRepair;
    p.quantity = quantity;
    p.dutyCycle = dutyCycle;
    return p;
  }

  public static List<Part> clone(Collection<Part> parts) {
    if (parts == null) {
      return null;
    }
    List<Part> ps = new ArrayList<Part>();
    for (Part p : parts) {
      ps.add(p.clone());
    }
    return ps;
  }
}
