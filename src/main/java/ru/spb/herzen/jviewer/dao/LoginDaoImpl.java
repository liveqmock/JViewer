package ru.spb.herzen.jviewer.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import ru.spb.herzen.jviewer.model.StudentModel;
import ru.spb.herzen.jviewer.model.UserModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eugene
 * Date: 11/2/13
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginDaoImpl extends JdbcDaoSupport implements LoginDao {

    @Override
    public UserModel getData(String name) throws EmptyResultDataAccessException{
        return getJdbcTemplate().query("select * from user where name = ?", new Object[]{name},
                new BeanPropertyRowMapper<StudentModel>(StudentModel.class)).get(0);
    }
}
