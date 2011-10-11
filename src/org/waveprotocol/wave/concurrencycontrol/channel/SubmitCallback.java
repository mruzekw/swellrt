/**
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.waveprotocol.wave.concurrencycontrol.channel;

import org.waveprotocol.wave.concurrencycontrol.common.ChannelException;
import org.waveprotocol.wave.concurrencycontrol.common.ResponseCode;
import org.waveprotocol.wave.model.version.HashedVersion;

/**
 * Receives the result of submitting a delta.
 *
 * @author anorth@google.com (Alex North)
 */
public interface SubmitCallback {
  /**
   * Called when submission succeeds (an acknowledgment).
   *
   * @param opsApplied number of operations applied by the server
   * @param newVersion new server version
   * @param responseCode status of the request
   * @param errorMessage any error message in applying the ops. null if no error.
   * @throws ChannelException if the channel fails when processing the ack
   */
  void onSuccess(int opsApplied, HashedVersion newVersion, ResponseCode responseCode,
      String errorMessage) throws ChannelException;

  /**
   * Called when submission fails due to network or channel failure.
   *
   * @param reason indicates reason for failure
   * @throws ChannelException if the channel fails when processing the failure
   */
  void onFailure(String reason) throws ChannelException;
}
