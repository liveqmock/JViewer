package ru.spb.herzen.jviewer.controller;

import org.apache.shale.test.mock.MockFacesContext;
import org.apache.shale.test.mock.MockHttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.spb.herzen.jviewer.controller.impl.LoginControllerImpl;
import ru.spb.herzen.jviewer.model.RoomModel;
import ru.spb.herzen.jviewer.model.UserModel;
import ru.spb.herzen.jviewer.model.impl.UserModelImpl;
import ru.spb.herzen.jviewer.utils.CommonUtil;

import javax.faces.context.ExternalContext;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Evgeny Mironenko
 */
public class LoginControllerTest {

    private LoginControllerImpl loginController;
    private UserModel userModel;
    private RoomModel roomModel;
    private AuthenticationManager authenticationManager;
    private MockFacesContext facesContext;

    @Before
    public void init() throws Exception {
        facesContext = new MockFacesContext();
        loginController = new LoginControllerImpl();
        userModel = new UserModelImpl();
        userModel.setName("testLogin");
        userModel.setPassword("testPassword");
        roomModel = createStrictMock(RoomModel.class);
        authenticationManager = createStrictMock(AuthenticationManager.class);
        loginController.setUserModel(userModel);
        loginController.setRoomModel(roomModel);
        loginController.setAuthenticationManager(authenticationManager);
    }

    @After
    public void destroy() throws Exception {
        loginController = null;
        userModel = null;
        roomModel = null;
        authenticationManager = null;
        facesContext = null;
    }

    @Test
    public void testLoginUser_success() throws Exception {
        prepareUserSuccess();
        assertEquals(loginController.loginUser(), "rooms?faces-redirect=true");
        verify(roomModel);
    }

    @Test
    public void testLoginUser_fail() throws Exception {
        prepareUserFail();
        assertEquals(loginController.loginUser(), null);
        verify(authenticationManager);
    }

    @Test
    public void testLoginAdmin_success() throws Exception {
        prepareUserSuccessNonEmptyRoomList();
        assertEquals(loginController.loginAdmin(), "admin?faces-redirect=true");
        verify(roomModel);
    }

    @Test
    public void testLoginAdmin_fail() throws Exception {
        prepareUserFail();
        assertEquals(loginController.loginAdmin(), null);
        verify(authenticationManager);
    }

    @Test
    public void testLogout() throws Exception {
        ExternalContext externalContext = createMock(ExternalContext.class);
        externalContext.invalidateSession();
        expectLastCall();
        replay(externalContext);
        facesContext.setExternalContext(externalContext);
        assertEquals(loginController.logout(), "index?faces-redirect=true");
    }

    @Test
     public void testPageRedirect_success() throws Exception {
        String page = "test";
        expect(roomModel.getCurrentRoom()).andReturn(page).times(3);
        replay(roomModel);
        assertEquals(loginController.pageRedirect(page), page + "?faces-redirect=true");
        verify(roomModel);
    }

    @Test
    public void testPageRedirect_fail() throws Exception {
        String page = "test";
        expect(roomModel.getCurrentRoom()).andReturn(null);
        replay(roomModel);
        assertEquals(loginController.pageRedirect(page), null);
        verify(roomModel);
    }

    private void prepareUserSuccess() throws Exception {
        List<String> rooms = createMock(List.class);
        roomModel.refreshRooms();
        expectLastCall();
        expect(roomModel.getRooms()).andReturn(rooms);
        expect(rooms.size()).andReturn(0);
        replay(rooms);
        roomModel.setCurrentRoom("");
        expectLastCall();
        replay(roomModel);
    }

    private void prepareUserSuccessNonEmptyRoomList() throws Exception {
        List<String> rooms = createMock(List.class);
        String roomName = "Test room";
        roomModel.refreshRooms();
        expectLastCall();
        expect(roomModel.getRooms()).andReturn(rooms).times(2);
        expect(rooms.size()).andReturn(1);
        expect(rooms.get(0)).andReturn(roomName);
        replay(rooms);
        roomModel.setCurrentRoom(roomName);
        expectLastCall();
        replay(roomModel);
    }

    private void prepareUserFail() {
        expect(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("testLogin", "testPassword")))
            .andThrow(new BadCredentialsException("Failed login"));
        replay(authenticationManager);
        CommonUtil.replayLogging();
    }

}
