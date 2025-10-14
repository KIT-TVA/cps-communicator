package cpscommunicator.mapping.sge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cpscommunicator.mapping.ComposedMapping;
import cpscommunicator.variation.sge.SGEVariation;
import cpscommunicator.variation.sge.SGEVariationType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SGEComposedMapping extends ComposedMapping<SGEComposedMapping.SGEMappingObject> {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SGEMappingObject {
        private SGEVariation variation;
        private String referenceObjectGetterMethodName;

        public SGEVariation getVariation() {
            return variation;
        }

        public void setVariation(SGEVariation variation) {
            this.variation = variation;
        }

        public String getReferenceObjectGetterMethodName() {
            return referenceObjectGetterMethodName;
        }

        public void setReferenceObjectGetterMethodName(String referenceObjectGetterMethodName) {
            this.referenceObjectGetterMethodName = referenceObjectGetterMethodName;
        }
    }

    private final List<Class<?>> removeDeltaOperations;

    public SGEComposedMapping() {
        super();
        removeDeltaOperations = new ArrayList<>();
    }

    @Override
    public SGEMappingAggregation getChangeAssessment(Collection<?> deltaOperations, Set<Object> base) {
        Set<Object> changed = new HashSet<>();
        Set<Object> removed = new HashSet<>();
        Map<Object, SGEVariation> objectDescriptionMap = new HashMap<>();

        List<Class<?>> referenceDeltaObjects = getComposedMap().keySet().stream().toList();


        // implement apply by ourselves, because we already need objectClassMap
        Map<Object, Class<?>> objectClassMap = new HashMap<>();
        objectDescriptionMap = new HashMap<>();
        for (Object operation : deltaOperations) {
            Class<?> clazz = findClass(operation, referenceDeltaObjects);
            if (clazz != null) {
                objectClassMap.put(operation, clazz);
                objectDescriptionMap.put(operation, getComposedMap().get(clazz).variation);

            } else {
                System.out.println("Mapping for class " + operation.getClass() + " or any parent classes was not specified");
            }
        }

        for (Object operation : deltaOperations) {
            SGEVariation variation = objectDescriptionMap.get(operation);
            if (variation == null || variation.getType() == SGEVariationType.CARRYOVER) {
                continue;
            }


            Class<?> clazz = objectClassMap.get(operation);
            if (clazz == null) {
                System.err.println("No class for " + operation + " found");
                continue;
            }

            String methodName = getComposedMap().get(clazz).referenceObjectGetterMethodName;
            if (methodName == null) {
                System.err.println("Not method for " + clazz + " found");
                continue;
            }

            try {
                Method method = operation.getClass().getMethod(methodName);
                Object referenceElement = method.invoke(operation);
                changed.add(referenceElement);

                if (findClass(operation, removeDeltaOperations) != null) {
                    removed.add(referenceElement);
                    removeDeltaOperations.add(operation.getClass());
                }

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                System.err.println(e.getMessage());
            }
        }

        base.addAll(changed);
        base.removeAll(removed);

        SGEMappingAggregation a = new SGEMappingAggregation();
        a.setTotal(base.size());
        a.setChanged(changed.size());
        a.setMap(objectDescriptionMap);

        a.setShare(changed.size() / (double) base.size());

        a.setNumAttributes(0);
        a.setNumPrinciples(0);
        objectDescriptionMap.values().stream().filter(variation -> variation.getType() == SGEVariationType.ATTRIBUTE).forEach(variation -> a.setNumAttributes(a.getNumAttributes() + 1));
        objectDescriptionMap.values().stream().filter(variation -> variation.getType() == SGEVariationType.PRINCIPLE).forEach(variation -> a.setNumPrinciples(a.getNumPrinciples() + 1));

        return a;
    }

    public List<Class<?>> getRemoveDeltaOperations() {
        return removeDeltaOperations;
    }

    public void put(Class<?> deltaOperationClass, SGEVariation variation, String referenceObjectGetterMethodName) {
        SGEMappingObject sgeMappingObject = new SGEMappingObject();
        sgeMappingObject.setVariation(variation);
        sgeMappingObject.setReferenceObjectGetterMethodName(referenceObjectGetterMethodName);
        this.addAtomicMapping(deltaOperationClass, sgeMappingObject);
    }
}
