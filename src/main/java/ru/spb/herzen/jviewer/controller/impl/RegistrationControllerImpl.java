package ru.spb.herzen.jviewer.controller.impl;

import org.apache.log4j.Logger;
import ru.spb.herzen.jviewer.controller.RegistrationController;
import ru.spb.herzen.jviewer.messages.RegistrationMsg;
import ru.spb.herzen.jviewer.model.impl.LocaleModel;
import ru.spb.herzen.jviewer.model.impl.RequestModel;
import ru.spb.herzen.jviewer.service.RegistrationService;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Registration controller implementation.
 * @author Evgeny Mironenko
 */
public class RegistrationControllerImpl implements RegistrationController, Serializable {

    private final Logger LOG = Logger.getLogger(RegistrationControllerImpl.class);

    private RequestModel requestModel;
    private LocaleModel localeModel;
    private RegistrationService registrationService;

    /**
     * Creates new profile.
     * @return URL for redirection, if registration was successful, and null, if registration failed.
     */
    @Override
    public String regProfile() {
        RegistrationMsg result = registrationService.regProfile(requestModel);
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        if (result == RegistrationMsg.SUCCESS) {
            currentInstance.getExternalContext().getFlash().put("success", localeModel.getLocaleFile().getProperty("registrationSuccessfulMessage"));
            return "index?faces-redirect=true";
        } else {
            if (result == RegistrationMsg.EXIST) {
                currentInstance.addMessage("registrationForm:name", new FacesMessage(localeModel.getLocaleFile().getProperty("userExistMessage")));
            } else if (result == RegistrationMsg.INVITATION_ID) {
                currentInstance.addMessage("registrationForm:inviteID", new FacesMessage(localeModel.getLocaleFile().getProperty("badInvitationIdMessage")));
            }
            return null;
        }
    }

    //
    // Setters for Dependency Injection.
    //

    public void setRequestModel(RequestModel requestModel) {
        this.requestModel = requestModel;
    }

    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    public void setLocaleModel(LocaleModel localeModel) {
        this.localeModel = localeModel;
    }
}
