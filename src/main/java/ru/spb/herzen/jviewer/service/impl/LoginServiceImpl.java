package ru.spb.herzen.jviewer.service.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import ru.spb.herzen.jviewer.dao.LoginDao;
import ru.spb.herzen.jviewer.dao.ValidationDao;
import ru.spb.herzen.jviewer.model.impl.RequestModel;
import ru.spb.herzen.jviewer.model.impl.RoomModelImpl;
import ru.spb.herzen.jviewer.model.UserModel;
import ru.spb.herzen.jviewer.service.LoginService;

import java.util.List;

/**
 * Login service implementation.
 * @author Evgeny Mironenko
 */
public class LoginServiceImpl implements LoginService {

    private ValidationDao validationDao;
    private LoginDao loginDao;

    /**
     * @see ru.spb.herzen.jviewer.service.LoginService#getData(ru.spb.herzen.jviewer.model.impl.RequestModel)
     * If validation will be failed, return null.
     */
    @Override
    public UserModel getData(RequestModel userModel) {
        try{
            if(validationDao.getUserPassword(userModel.getName()).equals(userModel.getPassword())){
                return loginDao.getData(userModel.getName());
            }
        } catch(EmptyResultDataAccessException e){
            return null;
        }
        return null;
    }

    /**
     * @see ru.spb.herzen.jviewer.service.LoginService#getRooms()
     * If list of rooms will be empty, return null.
     */
    @Override
    public List<RoomModelImpl> getRooms() {
        return loginDao.getRooms();
    }

    //
    // Setters for Dependency Injection.
    //

    public void setValidationDao(ValidationDao validationDao) {
        this.validationDao = validationDao;
    }

    public void setLoginDao(LoginDao loginDao) {
        this.loginDao = loginDao;
    }
}
