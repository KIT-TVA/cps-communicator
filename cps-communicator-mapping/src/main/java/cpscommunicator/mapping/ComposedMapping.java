package cpscommunicator.mapping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import java.util.*;


public abstract class ComposedMapping<T> {
    @JsonIgnore
    private final Map<Class<?>, T> composedMap;

    private final List<AtomicMapping<T>> composedMappings;


    public ComposedMapping() {
        composedMap = new HashMap<>();
        composedMappings = new ArrayList<>();
    }

    public void addAtomicMapping(Class<?> classOfDeltaOperation, T changeDescription) {
        addAtomicMapping(new AtomicMapping<>(classOfDeltaOperation, changeDescription));
    }

    public void addAtomicMapping(AtomicMapping<T> atomicMapping) {
        composedMappings.add(atomicMapping);
        composedMap.put(atomicMapping.getClassOfDeltaOperation(), atomicMapping.getChangeDescriptor());
    }

    public Map<Object, T> getChangeDescriptors(Collection<?> deltaOperations) {
        Map<Object, T> changeDescriptors = new HashMap<>();

        for (Object operation : deltaOperations) {
            T description = findDescriptor(operation);

            if (description != null) {
                changeDescriptors.put(operation, description);
            }
        }
        return changeDescriptors;
    }

    public Class<?> findClass(Object object, List<Class<?>> classes) {
        Class<?> clazz = object.getClass();

        while (clazz != null) {
            if (classes.contains(clazz)) {
                return clazz;
            }

            for (Class<?> interfaceClass : clazz.getInterfaces()) {
                if (classes.contains(interfaceClass)) {
                    return interfaceClass;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    public T findDescriptor(Object object) {
        Class<?> clazz = findClass(object, composedMap.keySet().stream().toList());
        if (clazz != null) {
            return composedMap.get(clazz);
        }
        return null;
    }


    public abstract Object getChangeAssessment(Collection<?> deltaOperations, Set<Object> base); // Object could be wrapped with ChangeAssessment

    public static Collection<EObject> collectAllEObjects(EObject object) {
        Collection<EObject> allObjects = new ArrayList<>();
        TreeIterator<EObject> iterator = object.eAllContents();
        while (iterator.hasNext()) {
            allObjects.add(iterator.next());
        }
        return allObjects;
    }

    public Map<Class<?>, T> getComposedMap() {
        return composedMap;
    }

    public List<AtomicMapping<T>> getComposedMappings() {
        return composedMappings;
    }

    // Custom deserialization logic to populate composedMap from composedMappings
    @JsonProperty("composedMappings")
    private void unpackComposedMappings(List<AtomicMapping<T>> mappings) {
        this.composedMappings.clear();
        this.composedMap.clear();
        if (mappings != null) {
            this.composedMappings.addAll(mappings);
            mappings.forEach(mapping -> this.composedMap.put(mapping.getClassOfDeltaOperation(), mapping.getChangeDescriptor()));
        }
    }

    @JsonProperty("composedMappings")
    public List<AtomicMapping<T>> packComposedMappings() {
        return this.composedMappings;
    }
}

