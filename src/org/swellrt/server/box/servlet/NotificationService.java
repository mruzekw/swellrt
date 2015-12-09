package org.swellrt.server.box.servlet;

import com.google.inject.Inject;

import org.waveprotocol.box.server.authentication.SessionManager;
import org.waveprotocol.wave.notification.DeviceStore;
import org.waveprotocol.wave.notification.NotificationRegisterStore;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class NotificationService implements SwellRTService {

  @Inject
  private DeviceStore deviceStore;
  @Inject
  private NotificationRegisterStore notificationStore;
  @Inject
  private SessionManager sessionManager;

  public NotificationService() {

  }

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse response) throws IOException {

    Enumeration<String> paramNames = req.getParameterNames();

    if (!paramNames.hasMoreElements()) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No parameters found!");
      return;
    } else {

      String name = paramNames.nextElement();
      String value = req.getParameter(name);

      HttpSession session = req.getSession(false);

      System.out.println(session);
      System.out.println(sessionManager);
      String account = sessionManager.getLoggedInAccount(session).getId().getAddress();

      switch (name) {

        case "registerDevice":
          deviceStore.register(account, value);
          response.setStatus(HttpServletResponse.SC_OK);
          System.out.println(deviceStore.getUserDevices(account));
          break;

        case "unregisterDevice":
          deviceStore.unregister(account, value);
          response.setStatus(HttpServletResponse.SC_OK);
          System.out.println(deviceStore.getUserDevices(account));
          break;

        case "subscribe":
          notificationStore.addSubscriptor(value, account);
          response.setStatus(HttpServletResponse.SC_OK);
          System.out.println(notificationStore.getSubscriptors(value));
          break;

        case "unsubscribe":
          notificationStore.removeSubscriptor(value, account);
          response.setStatus(HttpServletResponse.SC_OK);
          System.out.println(notificationStore.getSubscriptors(value));
          break;

        default:
          response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown operation: " + name);
      }
    }
  }
}