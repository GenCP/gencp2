package appws;

import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://appws/")
public interface TestGenPortType extends Remote {
    @WebMethod
    public String generarActivityDiagram(@WebParam(name = "arg0")
        String templateFile, @WebParam(name = "arg1")
        String activityDiagramFile) throws RemoteException;

    @WebMethod
    public String generarSecuencias(@WebParam(name = "arg0")
        String templateFile, @WebParam(name = "arg1")
        String testObjectiveFile) throws RemoteException;
}
