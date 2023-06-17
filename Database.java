import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Database extends Remote {
    public boolean insertOrUpdate(String key, String value) throws RemoteException;

    public String get(String key) throws RemoteException;

    public void delete(String key) throws RemoteException;

    public List<String> getValues() throws RemoteException;
}