package cpscommunicator.mapping.sge.cli;

import deltastatemachine.DeltaStatemachine;
import deltastatemachine.DeltastatemachinePackage;
import edu.kit.kastel.tva.bcs.ecorebased.metamodels.statemachine.StateMachine;
import edu.kit.kastel.tva.bcs.ecorebased.metamodels.statemachine.StatemachinePackage;

public class ApplyDeltasStatemachineCommand extends ApplyDeltasCommand<StateMachine, DeltaStatemachine> {

    public ApplyDeltasStatemachineCommand() {
        super();
        StatemachinePackage.eINSTANCE.eClass();
        DeltastatemachinePackage.eINSTANCE.eClass();


    }

    @Override
    public String getName() {
        return "aggregateStatemachineDeltas";
    }
}
