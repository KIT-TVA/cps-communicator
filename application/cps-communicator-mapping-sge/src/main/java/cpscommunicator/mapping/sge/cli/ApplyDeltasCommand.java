package cpscommunicator.mapping.sge.cli;

import edu.kit.kastel.tva.deltavar.core.deltametamodel.genericdelta.DEDeltaDialect;
import edu.kit.kastel.tva.deltavar.core.deltametamodel.genericdelta.DEDeltaOperationDefinition;
import cpscommunicator.base.cli.AbstractCommand;
import cpscommunicator.base.io.EObjectIO;
import cpscommunicator.base.io.JsonIO;
import cpscommunicator.mapping.sge.SGEComposedMapping;
import cpscommunicator.mapping.sge.SGEMappingAggregation;
import cpscommunicator.variation.sge.SgePackage;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class ApplyDeltasCommand<S extends EObject, T extends DEDeltaDialect> extends AbstractCommand {
    public ApplyDeltasCommand() {}

    @Override
    public void run(CommandLine cmd) {
        String baseVersion = cmd.getOptionValue("base");
        String[] deltas = cmd.getOptionValues("deltas");
        String outputFilePath = cmd.getOptionValue("output");
        String mapFilePath = cmd.getOptionValue("map");

        S model;
        List<T> deltaDialects = new ArrayList<>();

        try {
            model = EObjectIO.load(baseVersion);
        } catch (IOException e) {
            System.err.println("Error loading base version from file: " + baseVersion);
            return;
        }

        try {
            EObjectIO.save(model, outputFilePath + "V1.xml");
        } catch (IOException e) {
            System.err.println("Error saving model " + model.getClass() + " to file: " + outputFilePath);
            return;
        }

        if (!cmd.hasOption("deltas")) {
            System.err.println("Missing option: deltas");
            return;
        }

        for (String delta : (deltas)) {
            try {
                T deltaDialect = EObjectIO.load(delta);
                deltaDialects.add(deltaDialect);
            } catch (IOException e) {
                System.err.println("Error loading delta " + delta);
                return;
            }
        }


        String aggregatedName = "";
        SGEComposedMapping mapping = null;
        SgePackage.eINSTANCE.eClass();

        try {
            mapping = JsonIO.loadFromJson(mapFilePath, SGEComposedMapping.class);
        } catch (Exception e) {
            System.err.println("Error parsing mapping file: " + mapFilePath);
            System.err.println(e.getMessage());
            return;
        }


        HashSet<Object> baseSet = new HashSet<>(getIdsFromEObject(model));

        assert deltaDialects.size() == deltas.length;
        for (int i = 0; i < deltaDialects.size(); i++) {
            String fileName = getFileName(deltas[i]);
            if (i == 0) aggregatedName = fileName;
            else aggregatedName += "_" + fileName;

            List<DEDeltaOperationDefinition> deltaOperations = deltaDialects.get(i).getDeltaOperationDefinitions();

            SGEMappingAggregation aggregation = mapping.getChangeAssessment(deltaOperations, baseSet);
            aggregation.setName(aggregatedName);
            try {
                JsonIO.saveToJson(aggregation, outputFilePath + "V" + (i + 2) + "_" + fileName + ".json");
            } catch (IOException e) {
                System.err.println("Error saving model " + model.getClass() + " to file: " + outputFilePath);
                return;
            }
        }
    }

    private static String getFileName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }
        int lastSeparatorUnix = filePath.lastIndexOf('/');
        int lastSeparatorWindows = filePath.lastIndexOf('\\');
        int lastSeparator = Math.max(lastSeparatorUnix, lastSeparatorWindows);
        String fileNameWithExtension;
        if (lastSeparator == -1) {
            fileNameWithExtension = filePath;
        } else {
            fileNameWithExtension = filePath.substring(lastSeparator + 1);
        }

        if (fileNameWithExtension.isEmpty()) {
            return "";
        }

        int lastDotIndex = fileNameWithExtension.lastIndexOf('.');

        if (lastDotIndex > 0) {
            return fileNameWithExtension.substring(0, lastDotIndex);
        }
        return fileNameWithExtension;
    }

    List<Integer> getIdsFromEObject(EObject object) {
        List<Integer> ids = new ArrayList<>();

        TreeIterator<EObject> iterator = object.eAllContents();
        while (iterator.hasNext()) {
            EObject obj = iterator.next();
            try {
                Method getIdMethod = obj.getClass().getMethod("getId");
                Class<?> returnType = getIdMethod.getReturnType();
                if (returnType.equals(Integer.class) || returnType.equals(int.class)) {

                    Object result = getIdMethod.invoke(obj);

                    if (result instanceof Integer) {
                        ids.add((Integer) result);
                    }
                } else {
                    System.out.println("  [Skipped] " + obj.toString() + " has getId(), but return type is not Integer.");
                }

            } catch (NoSuchMethodException e) {
                System.out.println("  [Skipped] " + obj.toString() + " does not have the public getId() method.");
            } catch (Exception e) {
                System.err.println("Error calling getId() on object " + obj.toString() + ": " + e.getMessage());
            }
        }
        return ids;
    }


    @Override
    public Options getOptions() {
        Options options = new Options();

        Option baseOption = new Option("b", "base", true, "Base: Path to the base version xml file");
        baseOption.setRequired(true);
        baseOption.setArgs(1);
        baseOption.setType(String.class);
        options.addOption(baseOption);

        Option deltasOption = new Option("d", "deltas", true, "Deltas to apply, each in a separate xml file.");
        deltasOption.setRequired(true);
        deltasOption.setType(String.class);
        deltasOption.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(deltasOption);

        Option baseVersionFileOption = new Option("o", "output", true, "Output folder path");
        baseVersionFileOption.setRequired(true);
        baseVersionFileOption.setType(String.class);
        options.addOption(baseVersionFileOption);

        Option sgeComposedMapOption = new Option("m", "map", true, "Path to sge composed map json file");
        sgeComposedMapOption.setArgs(1);
        sgeComposedMapOption.setType(String.class);
        options.addOption(sgeComposedMapOption);

        return options;
    }
}


