import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class DatabaseImpl extends UnicastRemoteObject implements Database {
    Map<String, String> base = new HashMap<String, String>();
    List<String> values;
    
    public DatabaseImpl() throws RemoteException {
        values = new ArrayList<>();
    }

    @Override
    public boolean insertOrUpdate(String key, String value) throws RemoteException
    {
        System.out.println("Verificando existência de value.");
        if (getValues().contains(value)) {
            System.out.println("Value existe, negando operação.");
            return false;
        }
        else
        {
            System.out.println("Value inexistente, continuando operação.");
            base.put(key, value);
            return true;
        }
    }

    @Override
    public String get(String key) throws RemoteException
    {
        return base.get(key);
    }

    @Override
    public void delete(String key) throws RemoteException
    {
        base.remove(key);
    }

    @Override
    public List<String> getValues() throws RemoteException
    {
        values.clear();

        for (String key: base.keySet()) {
            System.out.println("key : " + key);
            System.out.println("value : " + base.get(key));
            values.add(base.get(key));
            System.out.println("---");
        }

        return values;
    }

    public synchronized String drawWinner() throws RemoteException {
        Set<String> keys = base.keySet();
        List<String> keyList = new ArrayList<>(keys);
        int random = new Random().nextInt(keyList.size());
        
        return keyList.get(random);
    }

    public synchronized void reset() throws RemoteException {
        base.clear();
    }
}