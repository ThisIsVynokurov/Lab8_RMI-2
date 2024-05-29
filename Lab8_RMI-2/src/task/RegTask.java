package task;

import server.UserRecord;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RegTask extends Remote {
    <T> T executeTask (UserRecord t) throws RemoteException;

}