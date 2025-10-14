package cpscommunicator.mapping.sge.cli;

import deltastatemachine.*;
import edu.kit.kastel.tva.deltavar.core.deltametamodel.genericdelta.DERemoveDeltaOperationDefinition;
import cpscommunicator.base.cli.AbstractCommand;
import cpscommunicator.base.io.JsonIO;
import cpscommunicator.mapping.sge.SGEComposedMapping;
import cpscommunicator.mapping.sge.SGEVariationFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.IOException;

public class BuildSGEStatemachineMapping extends AbstractCommand {
    @Override
    public void run(CommandLine cmd) {
        String outputPath = cmd.getOptionValue("output");
        SGEVariationFactory factory = SGEVariationFactory.getInstance();


        SGEComposedMapping mapping = new SGEComposedMapping();
        mapping.getRemoveDeltaOperations().add(DERemoveDeltaOperationDefinition.class);

        //Statemachine Model
        mapping.put(AddToStatesInRegion.class, factory.createAttributeVariation(), "getStateId");
        mapping.put(ModifyAttributeNameInState.class, factory.createAttributeVariation(), "getStateIdOfContainer");
        mapping.put(ModifyAttributeIsInitialInState.class, factory.createAttributeVariation(), "getStateIdOfContainer");
        mapping.put(ModifyAttributeIsFinalInState.class, factory.createPrincipleVariation(), "getStateIdOfContainer");
        mapping.put(RemoveFromStatesInRegion.class, factory.createAttributeVariation(), "getStateId");

        mapping.put(AddToTransitionsInRegion.class, factory.createPrincipleVariation(), "getTransitionId");
        mapping.put(SetSourceStateInTransition.class, factory.createPrincipleVariation(), "getTransitionIdOfContainer");
        mapping.put(SetTargetStateInTransition.class, factory.createPrincipleVariation(), "getTransitionIdOfContainer");
        mapping.put(SetTriggerInTransition.class, factory.createPrincipleVariation(), "getTransitionIdOfContainer");
        mapping.put(SetEffectInTransition.class, factory.createPrincipleVariation(), "getTransitionIdOfContainer");
        mapping.put(ModifyAttributeNameInTransition.class, factory.createAttributeVariation(), "getTransitionIdOfContainer");
        mapping.put(ModifyAttributeEventInTrigger.class, factory.createAttributeVariation(), "getNewValue"); //todo missing id for trigger if wanted
        mapping.put(RemoveFromTransitionsInRegion.class, factory.createPrincipleVariation(), "getTransitionId");

        mapping.put(AddToRegionsInStateMachine.class, factory.createAttributeVariation(), "getRegionId");
        mapping.put(AddToSubRegionInState.class, factory.createAttributeVariation(), "getRegionId");
        mapping.put(ModifyAttributeNameInRegion.class, factory.createAttributeVariation(), "getRegionIdOfContainer");
        mapping.put(RemoveFromSubRegionInState.class, factory.createAttributeVariation(), "getRegionId");
        mapping.put(RemoveFromRegionsInStateMachine.class, factory.createAttributeVariation(), "getRegionId");

        mapping.put(AddToSignalsInStateMachine.class, factory.createAttributeVariation(), "getCommunicationsignalId");
        mapping.put(ModifyAttributeNameInCommunicationSignal.class, factory.createAttributeVariation(), "getCommunicationsignalIdOfContainer");
        mapping.put(RemoveFromSignalsInStateMachine.class, factory.createAttributeVariation(), "getCommunicationsignalId");

        try {
            JsonIO.saveToJson(mapping, outputPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getName() {
        return "buildSgeStatemachineMapping";
    }

    @Override
    public Options getOptions() {
        Options options = new Options();

        Option outputOption = new Option("o", "output", true, "Output JSON file");
        outputOption.setRequired(true);
        outputOption.setArgs(1);
        outputOption.setType(String.class);
        options.addOption(outputOption);

        return options;
    }
}
