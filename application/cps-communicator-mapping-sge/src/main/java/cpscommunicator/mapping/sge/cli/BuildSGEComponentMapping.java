package cpscommunicator.mapping.sge.cli;

import deltacomponent.*;
import edu.kit.kastel.tva.deltavar.core.deltametamodel.genericdelta.DERemoveDeltaOperationDefinition;
import cpscommunicator.base.cli.AbstractCommand;
import cpscommunicator.base.io.JsonIO;
import cpscommunicator.mapping.sge.SGEComposedMapping;
import cpscommunicator.mapping.sge.SGEVariationFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.IOException;

public class BuildSGEComponentMapping extends AbstractCommand {
    @Override
    public void run(CommandLine cmd) {
        String outputPath = cmd.getOptionValue("output");
        SGEVariationFactory factory = SGEVariationFactory.getInstance();

        SGEComposedMapping mapping = new SGEComposedMapping();
        mapping.getRemoveDeltaOperations().add(DERemoveDeltaOperationDefinition.class);

        //Component Model
        mapping.put(AddToComponentsInSystem.class, factory.createAttributeVariation(), "getComponentId");
        mapping.put(ModifyAttributeNameInComponent.class, factory.createAttributeVariation(), "getComponentIdOfContainer");
        mapping.put(RemoveFromComponentsInSystem.class, factory.createAttributeVariation(), "getSystemIdOfContainer");

        mapping.put(AddToInputPortsInComponent.class, factory.createAttributeVariation(), "getPortId");
        mapping.put(AddToOutputPortsInComponent.class, factory.createAttributeVariation(), "getPortId");
        mapping.put(ModifyAttributeNameInPort.class, factory.createAttributeVariation(), "getPortIdOfContainer");
        mapping.put(RemoveFromInputPortsInComponent.class, factory.createAttributeVariation(), "getPortId");
        mapping.put(RemoveFromOutputPortsInComponent.class, factory.createAttributeVariation(), "getPortId");

        mapping.put(AddToConnectorsInSystem.class, factory.createAttributeVariation(), "getConnectorId");
        mapping.put(ModifyAttributeNameInConnector.class, factory.createAttributeVariation(), "getConnectorIdOfContainer");
        mapping.put(SetTargetPortInConnector.class, factory.createPrincipleVariation(), "getConnectorIdOfContainer");
        mapping.put(SetSourcePortInConnector.class, factory.createPrincipleVariation(), "getConnectorIdOfContainer");
        mapping.put(RemoveFromConnectorsInSystem.class, factory.createAttributeVariation(), "getSystemIdOfContainer");

        mapping.put(AddToSignalsInSystem.class, factory.createAttributeVariation(), "getSignalId");
        mapping.put(SetSignalInPort.class, factory.createAttributeVariation(), "getPortIdOfContainer");
        mapping.put(RemoveFromSignalsInSystem.class, factory.createAttributeVariation(), "getSignalId");
        mapping.put(ModifyAttributeNameInSignal.class, factory.createAttributeVariation(), "getSignalIdOfContainer");

        try {
            JsonIO.saveToJson(mapping, outputPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getName() {
        return "buildSgeComponentMapping";
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
