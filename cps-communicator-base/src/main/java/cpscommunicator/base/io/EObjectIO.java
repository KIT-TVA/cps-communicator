package cpscommunicator.base.io;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EObjectIO {
    /**
     * Loads an EObject to a model instance based on an XML file path
     * <p>
     * !! Don't forget to register you EMF package if you just load: EPackage.Registry.INSTANCE.put(MyecorePackage.eINSTANCE.getNsURI(), MyecorePackage.eINSTANCE);
     * </p>
     *
     * @param path to the EObject XML file
     * @param <T>  EObject Model instance
     * @return loaded EObject
     * @throws IOException if something goes wrong loading
     */
    public static <T extends EObject> T load(String path) throws IOException {
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("xml", new XMIResourceFactoryImpl());

        ResourceSet resSet = new ResourceSetImpl();
        try {
            Resource resource = resSet.getResource(URI.createURI(path), true);
            return (T) resource.getContents().get(0);
        } catch (Exception e) {
            throw new IOException("The provided file does not conform to the format of T");
        }
    }

    public static <T extends EObject> void save(T model, String path) throws IOException {
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        XMIResourceFactoryImpl factory = new XMIResourceFactoryImpl();
        m.put("xml", new XMIResourceFactoryImpl());

        Map<Object, Object> options = new HashMap<>();
        options.put(XMLResource.OPTION_KEEP_DEFAULT_CONTENT, Boolean.TRUE);

        ResourceSet resSet = new ResourceSetImpl();
        Resource resource = resSet.createResource(URI.createURI(path));

        resource.getContents().add(model);
        resource.save(options);
    }
}
