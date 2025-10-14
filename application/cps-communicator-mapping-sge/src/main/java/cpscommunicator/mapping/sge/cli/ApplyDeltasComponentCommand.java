package cpscommunicator.mapping.sge.cli;

import deltacomponent.DeltaComponent;
import deltacomponent.DeltacomponentPackage;

public class ApplyDeltasComponentCommand extends ApplyDeltasCommand<component.System, DeltaComponent> {

    public ApplyDeltasComponentCommand() {
        super();
        component.ComponentPackage.eINSTANCE.eClass();
        DeltacomponentPackage.eINSTANCE.eClass();
    }

    @Override
    public String getName() {
        return "aggregateComponentDeltas";
    }
}
