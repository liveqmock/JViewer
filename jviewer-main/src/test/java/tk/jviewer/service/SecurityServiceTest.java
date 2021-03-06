package tk.jviewer.service;

import org.apache.shale.test.mock.MockFacesContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tk.jviewer.model.UserModel;
import tk.jviewer.model.impl.RequestModel;
import tk.jviewer.model.impl.UserModelImpl;
import tk.jviewer.service.impl.SecurityService;

import javax.faces.context.ExternalContext;
import javax.faces.context.Flash;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Evgeny Mironenko
 */
public class SecurityServiceTest {

    private Authentication authentication;
    private SecurityService securityService;
    private LoginService loginService;
    private RequestModel requestModel;
    private UserModel userModel;
    private String role = "ROLE_USER";

    @Before
    public void init() throws Exception {
        securityService = new SecurityService();
        requestModel = new RequestModel();
        userModel = new UserModelImpl();
        userModel.setRole(role);
        loginService = createStrictMock(LoginService.class);
        authentication = createStrictMock(Authentication.class);
        securityService.setRequestModel(requestModel);
        securityService.setUserModel(userModel);
        securityService.setLoginService(loginService);
    }

    @After
    public void destroy() throws Exception {
        securityService = null;
        requestModel = null;
        userModel = null;
        loginService = null;
    }

    @Test
    public void testAuthenticate_success() throws Exception {
        String name = "user";
        String password = "password";
        expect(authentication.getName()).andReturn(name);
        expect(authentication.getCredentials()).andReturn(password);
        replay(authentication);
        expect(loginService.getData(requestModel)).andReturn(userModel);
        replay(loginService);
        assertEquals(securityService.authenticate(authentication), getAuthentication(name, password));
        verify(authentication);
        verify(loginService);
    }

    @Test(expected = BadCredentialsException.class)
    public void testAuthenticate_fail() throws Exception {
        MockFacesContext facesContext = new MockFacesContext();
        ExternalContext externalContext = createMock(ExternalContext.class);
        Flash flash = createMock(Flash.class);
        expect(externalContext.getFlash()).andReturn(flash);
        replay(externalContext);
        facesContext.setExternalContext(externalContext);
        String name = "user";
        String password = "password";
        expect(authentication.getName()).andReturn(name);
        expect(authentication.getCredentials()).andReturn(password);
        replay(authentication);
        expect(loginService.getData(requestModel)).andReturn(null);
        replay(loginService);
        securityService.authenticate(authentication);
        verify(authentication);
        verify(loginService);
    }

    @Test
    public void testSupports() throws Exception {
        assertTrue(securityService.supports(SecurityService.class));
    }

    private Authentication getAuthentication(String name, String password) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(name, password, authorities);
    }
}
