package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import task.RegTask;

public class RegImpl extends UnicastRemoteObject implements RegTask {
    protected RegImpl() throws RemoteException{
    }
    @Override
    public <T> T executeTask(UserRecord t) throws RemoteException{
        ConferenceServer.getInstance().registerUser((UserRecord) t);
        return null;
    }
}