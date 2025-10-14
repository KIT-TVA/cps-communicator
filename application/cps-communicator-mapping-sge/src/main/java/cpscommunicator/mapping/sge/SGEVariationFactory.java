package cpscommunicator.mapping.sge;

import cpscommunicator.variation.sge.*;

public class SGEVariationFactory {
    private static SGEVariationFactory instance;
    private static SgeFactory factory;

    private SGEVariationFactory() {
        factory = SgeFactory.eINSTANCE;
    }

    public static SGEVariationFactory getInstance() {
        if (instance == null) {
            instance = new SGEVariationFactory();
        }
        return instance;
    }

    public SGEVariation createAttributeVariation() {
        SGEVariation variation = factory.createSGEVariation();
        variation.setType(SGEVariationType.ATTRIBUTE);
        return variation;
    }

    public SGEVariation createPrincipleVariation() {
        SGEVariation variation = factory.createSGEVariation();
        variation.setType(SGEVariationType.PRINCIPLE);
        return variation;
    }

    public SGEVariation createCarryoverVariation() {
        SGEVariation variation = factory.createSGEVariation();
        variation.setType(SGEVariationType.CARRYOVER);
        return variation;
    }
}
