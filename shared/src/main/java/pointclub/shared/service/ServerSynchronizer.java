package pointclub.shared.service;

import java.util.List;

import pointclub.shared.model.PointclubRecord;
import pointclub.shared.service.log.LogService;
import pointclub.shared.service.log.LogTag;
import retrofit2.Response;

public class ServerSynchronizer {
    private static ServerSynchronizer instance;

    public <T extends PointclubRecord> void synchronizeList(List<T> localList, Response<List<T>> serverList) {
        if (serverList != null && serverList.body() != null && serverList.isSuccessful()) {
            localList.addAll(serverList.body());
        } else {
            LogService.warn(LogTag.REST_ERROR, "Got a bad response %s",
                    serverList == null ? "null" : serverList.toString());
        }
        if (localList.size() > 0) {
            syncWithServer(localList);
        }
    }

    private void syncWithServer(List<? extends PointclubRecord> serverList) {
        List<? extends PointclubRecord> list = PointclubRecord.listAll(serverList.get(0).getClass());
        serverList.forEach(t -> {
            if (!list.contains(t)) {
                t.setId(null);
                t.save();
            }
        });
        list.removeAll(serverList);
        list.forEach(t -> t.delete());
    }

    public static ServerSynchronizer getInstance() {
        if (instance == null) {
            instance = new ServerSynchronizer();
        }
        return instance;
    }
}
