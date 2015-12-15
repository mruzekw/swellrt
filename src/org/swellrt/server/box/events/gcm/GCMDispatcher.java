package org.swellrt.server.box.events.gcm;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.swellrt.server.box.events.Event;
import org.swellrt.server.box.events.EventDispatcherTarget;
import org.waveprotocol.wave.util.logging.Log;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class GCMDispatcher implements EventDispatcherTarget {

  private static final Log LOG = Log.get(GCMDispatcher.class);


  public final static String NAME = "gcm";


  private static final String REGISTRATION_IDS = "registration_Ids";


  private static final String DATA = "data";

  private String authKey;
  private String sendUrl;
  private GCMSubscriptionStore subscriptionManager;


  private HttpClient httpClient;


  @Inject
  public GCMDispatcher(GCMSubscriptionStore subscriptionManager) {
    this.subscriptionManager = subscriptionManager;
  }

  public void initialize(String confFilePath) {

    FileReader fr;
    try {
      fr = new FileReader(confFilePath);
    } catch (FileNotFoundException e) {
      LOG.warning("GCM dispatcher configuration file not found: " + confFilePath);
      return;
    }
    JsonParser jsonParser = new JsonParser();
    JsonElement jsonElement = jsonParser.parse(fr);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonElement gcmElement = jsonObject.get(NAME);

    if (gcmElement == null) {
      LOG.warning("GCM dispatcher configuration error: gcm section not found in " + confFilePath);
      return;
    }

    JsonObject gcmObject = gcmElement.getAsJsonObject();

    if (!(gcmObject.has("authKey")) || !(gcmObject.has("url"))) {
      LOG.warning("GCM dispatcher configuration error: config keys not found " + confFilePath);
      return;
    }

    initialize(gcmObject.get("authKey").getAsString(), gcmObject.get("url").getAsString());

  }

  public void initialize(String authKey, String sendUrl) {
    this.authKey = authKey;
    this.sendUrl = sendUrl;

    this.httpClient = new HttpClient();

    LOG.warning("GCM event dispatcher succesfully configured");
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public void dispatch(Event event, String payload) {
    LOG.info("Event dispatched by GCM: " + event.getWaveletId() + " -> " + payload);


    PostMethod postMethod = new PostMethod(sendUrl);


    postMethod.setRequestHeader("Content-Type", "application/json");
    postMethod.setRequestHeader("Authorization", this.authKey);

    try {

      JSONObject dataPayload = new JSONObject(payload);
      JSONObject completePayload = new JSONObject();

      String waveId = event.getWaveId().toString();
      List<String> subscriptors = subscriptionManager.getSubscriptorsDevices(waveId);

      if (subscriptors.isEmpty()) {
        return;
      }

      completePayload.put(REGISTRATION_IDS, subscriptors);
      completePayload.put(DATA, dataPayload);

      String stringPayload = completePayload.toString();
      RequestEntity requestData = new ByteArrayRequestEntity(stringPayload.getBytes(Charset.forName("UTF-8")));

      postMethod.setRequestEntity(requestData);

      LOG.info("POST: " + postMethod.toString());

      int resultCode = httpClient.executeMethod(postMethod);

      if (resultCode != HttpStatus.SC_OK) {
        throw new IOException("HTTP response code " + resultCode);
      }


    } catch (JSONException e) {
      LOG.severe("Error sending GCM request", e);

    } catch (HttpException e) {
      LOG.severe("Error sending GCM request", e);

    } catch (IOException e) {
      LOG.severe("Error sending GCM request", e);

    } finally {
      postMethod.releaseConnection();
    }
  }

}
