package ru.spb.herzen.jviewer.dao;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import ru.spb.herzen.jviewer.model.RequestModel;
import ru.spb.herzen.jviewer.model.UserModel;

/**
 * Registration Data Access Object implementation.
 * @author Evgeny Mironenko
 */
public class RegistrationDaoImpl extends JdbcDaoSupport implements RegistrationDao {

    /**
     * @see ru.spb.herzen.jviewer.dao.RegistrationDao#regProfile(String, String, String, String)
     */
    @Override
    public void regProfile(String name, String password, String role, String faculty) {
        getJdbcTemplate().update("insert into users (name, password, role, faculty) values (?, ?, ?, ?)", name, password,
                role, faculty);
    }

    /**
     * @see ru.spb.herzen.jviewer.dao.RegistrationDao#getInvitationID()
     */
    @Override
    public String getInvitationID() {
        return getJdbcTemplate().queryForObject("select invitationID from properties", String.class);
    }
}
