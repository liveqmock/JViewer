package ru.spb.herzen.jviewer.service;

import ru.spb.herzen.jviewer.model.RequestModel;
import ru.spb.herzen.jviewer.model.RoomModel;
import ru.spb.herzen.jviewer.model.RoomModelImpl;
import ru.spb.herzen.jviewer.model.UserModel;

import java.util.List;

/**
 * Login service interface.
 * @author Evgeny Mironenko
 */
public interface LoginService {

    /**
     * Gets user data from request model.
     * @param requestModel current request model
     * @return user data object.
     */
    UserModel getData(RequestModel requestModel);

    /**
     * Gets list of all available rooms.
     * @return list of rooms.
     */
    List<RoomModelImpl> getRooms();
}
