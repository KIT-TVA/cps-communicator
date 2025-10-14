package cpscommunicator.mapping.sge;

import cpscommunicator.variation.sge.SGEVariation;

import java.util.HashMap;
import java.util.Map;

public class SGEMappingAggregation {
    private String name;
    private Map<Object, SGEVariation> map;
    private int total;
    private int changed;
    private int numPrinciples;
    private int numAttributes;
    private double share;

    public SGEMappingAggregation() {
        map = new HashMap<>();
    }

    public Map<Object, SGEVariation> getMap() {
        return map;
    }

    public void setMap(Map<Object, SGEVariation> map) {
        this.map = map;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getChanged() {
        return changed;
    }

    public void setChanged(int changed) {
        this.changed = changed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumPrinciples() {
        return numPrinciples;
    }

    public void setNumPrinciples(int numPrinciples) {
        this.numPrinciples = numPrinciples;
    }

    public int getNumAttributes() {
        return numAttributes;
    }

    public void setNumAttributes(int numAttributes) {
        this.numAttributes = numAttributes;
    }

    public double getShare() {
        return share;
    }

    public void setShare(double share) {
        this.share = share;
    }
}
